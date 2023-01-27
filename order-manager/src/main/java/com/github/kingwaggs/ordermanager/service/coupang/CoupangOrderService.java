package com.github.kingwaggs.ordermanager.service.coupang;

import com.github.kingwaggs.ordermanager.coupangsdk.config.CoupangVendorConfig;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.RefundOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping.PrepareShipmentDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import com.github.kingwaggs.ordermanager.domain.*;
import com.github.kingwaggs.ordermanager.domain.dto.MessagePlatform;
import com.github.kingwaggs.ordermanager.domain.dto.Shipping;
import com.github.kingwaggs.ordermanager.domain.dto.TrackingDto;
import com.github.kingwaggs.ordermanager.domain.dto.Welcome;
import com.github.kingwaggs.ordermanager.domain.dto.request.OhmyzipDeliveryRequest;
import com.github.kingwaggs.ordermanager.domain.dto.request.OhmyzipTrackingUpdateRequest;
import com.github.kingwaggs.ordermanager.domain.dto.response.AmazonProductResponse;
import com.github.kingwaggs.ordermanager.domain.dto.response.OhmyzipResponse;
import com.github.kingwaggs.ordermanager.domain.dto.response.ZincResponse;
import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.ordermanager.domain.sheet.*;
import com.github.kingwaggs.ordermanager.exception.CancellationException;
import com.github.kingwaggs.ordermanager.exception.*;
import com.github.kingwaggs.ordermanager.service.*;
import com.github.kingwaggs.ordermanager.service.amazon.AmazonProductValidator;
import com.github.kingwaggs.ordermanager.service.amazon.AmazonPurchaseService;
import com.github.kingwaggs.ordermanager.util.DateTimeConverter;
import com.github.kingwaggs.ordermanager.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangOrderService implements OrderManagerApplicationRunner {

    private static final LinkedBlockingQueue<Runnable> tempTaskQueue = new LinkedBlockingQueue<>(500);
    private static final ExecutorService fixedThreadPool = new ThreadPoolExecutor(16, 64, 600, TimeUnit.SECONDS, tempTaskQueue);
    private static final Integer DEFAULT_TIME_GAP_IN_DAYS = 1;
    private static final Integer GET_FIRST_ORDER = 0;
    private static final long SHIPPING_DAYS = 10L;
    private static final MessagePlatform MESSAGE_PLATFORM = MessagePlatform.KAKAO;

    private final CoupangOrderFetcher coupangOrderFetcher;
    private final CoupangOrderCancelService coupangCancelService;
    private final CoupangOrderShippingService coupangShippingService;
    private final HelloWorldAutoStoreProductService hwasProductService;
    private final OrderSheetService orderSheetService;
    private final KafkaProducerService kafkaProducerService;
    private final AmazonProductValidator amazonProductValidator;
    private final AmazonPurchaseService amazonPurchaseService;
    private final OhmyzipService ohmyzipService;
    private final CoupangVendorConfig vendorConfig;
    private final SlackMessageUtil slackMessageUtil;
    private final OrderContext orderContext;

    @Override
    public void run(ApplicationArguments args) {
        log.info("ApplicationRunner method [run()] starts.");
        syncOrders();
    }

    @Override
    public void syncOrders() {
        try {
            log.info("Sync Orders start");

            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusDays(DEFAULT_TIME_GAP_IN_DAYS);

            boolean isEveryPurchaseAgencyOrderSynced = syncPurchaseAgencyOrders(startTime, endTime);
            Future<Boolean> isEveryCoupangOrderSynced = fixedThreadPool.submit(this::syncSourceOrders);

            if (isEveryPurchaseAgencyOrderSynced && isEveryCoupangOrderSynced.get()) {
                log.info("Sync Orders completed successfully. Setting up Order Context status to [RUNNING]");
                slackMessageUtil.sendOrder("Sync 작업에 성공했습니다. Order Context 상태 값을 [RUNNING]으로 변경합니다.");
                orderContext.updateRunningStatus(RunnerStatus.RUNNING);
                return;
            }
            log.error("Sync Orders were uncompleted. Order Context status is still [STOP]. Try sync orders with controller.");
            slackMessageUtil.sendError("Sync 작업에 실패했습니다. Controller를 통해 재시도 후 Order Context 상태 값을 [RUNNING]으로 변경해주셔야 합니다.");
        } catch (InterruptedException exception) {
            log.error("Exception occurred during sync orders", exception);
            slackMessageUtil.sendError("SyncOrder() 메서드 실행 시 InterruptedException 발생했습니다. Controller를 통해 재시도 후 Order Context 상태 값을 [RUNNING]으로 변경해주셔야 합니다.", exception);
            Thread.currentThread().interrupt();
        } catch (ExecutionException exception) {
            log.error("Exception occurred during sync orders", exception);
            slackMessageUtil.sendError("SyncOrder() 메서드 실행 시 ExecutionException 발생했습니다. Controller를 통해 재시도 후 Order Context 상태 값을 [RUNNING]으로 변경해주셔야 합니다.", exception);
        }
    }

    public boolean syncSourceOrders() {
        try {
            log.info("Syncing Coupang orders start. [status=INSTRUCT]");
            List<CoupangOrder> coupangOrders = coupangOrderFetcher.fetchPrepareShippingOrders(vendorConfig.getVendorId());
            List<CoupangOrder> syncNeededCoupangOrders = coupangOrders.stream()
                    .filter(order -> orderSheetService.findOrderByOrderId(order.getOrderId().toString()) == null)
                    .collect(Collectors.toList());
            if (syncNeededCoupangOrders.isEmpty()) {
                log.info("There are 0 coupang orders. [status=INSTRUCT] There is nothing to sync.");
                return true;
            }

            syncNeededCoupangOrders.forEach(this::restartSyncCoupangOrders);
            return true;
        } catch (CoupangApiException exception) {
            slackMessageUtil.sendError("쿠팡 주문 [상태=상품준비중] sync 시 오류가 발생했습니다. Controller를 통해 재시도가 필요합니다.");
            return false;
        }
    }

    public boolean syncPurchaseAgencyOrders(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Syncing unsynced Zinc orders with the order table between startTime : {}, endTime : {}", startTime, endTime);
        Long startUnixTime = DateTimeConverter.localDateTime2Unix(startTime);
        Long endUnixTime = DateTimeConverter.localDateTime2Unix(endTime);

        List<ZincResponse> completedZincOrders = amazonPurchaseService.getCompletedZincOrderList(startUnixTime, endUnixTime);
        if (completedZincOrders == null) {
            log.error("Failed to sync Zinc orders. Need to try by Controller manually.");
            slackMessageUtil.sendError("Zinc 주문 sync에 실패했습니다. Controller를 통해 재시도가 필요합니다.");
            return false;
        }

        List<ZincResponse> syncNeededZincOrders = completedZincOrders.stream()
                .filter(order -> orderSheetService.findOrderBy(order.getRequestId()) == null)
                .collect(Collectors.toList());
        if (syncNeededZincOrders.isEmpty()) {
            log.info("There are 0 completed-zinc-orders. There is nothing to sync.");
            return true;
        }

        List<CurrentStatus> targetStatus = List.of(CurrentStatus.ERR_PURCHASE_AGENCY_REQUEST, CurrentStatus.SOURCE_STATUS_TO_PREPARE);
        boolean hasUnsyncedZincOrder = syncNeededZincOrders.stream()
                .anyMatch(zincOrder -> hasUnsyncableZincOrder(zincOrder, targetStatus));
        if (hasUnsyncedZincOrder) {
            log.error("Exception occurred during sync purchase agency order.");
            return false;
        }

        log.info("Synced zinc orders successfully.");
        return true;
    }

    private boolean hasUnsyncableZincOrder(ZincResponse zincOrder, List<CurrentStatus> targetStatus) {
        try {
            String zincAsin = zincOrder.getAsin();
            HelloWorldAutoStoreProduct hwasProduct = hwasProductService.getHwasProductBySourceId(zincAsin);
            List<Order> orders = orderSheetService.findOrderBy(hwasProduct, targetStatus);
            if (orders == null || orders.isEmpty()) {
                log.error("There is an unsynced Zinc order. Needs to check why. Purchase Agency Request Id : {}", zincOrder.getRequestId());
                slackMessageUtil.sendError(String.format("Sync에 실패한 Zinc 주문이 있습니다. [Zinc 주문은 존재하지만 테이블에 관련 주문이 없습니다.] 확인이 필요합니다. Purchase Agency Request Id : %s", zincOrder.getRequestId()));
                return true;
            }

            Order order = orders.get(GET_FIRST_ORDER);
            orderSheetService.updatePurchaseAgencyRequest(order, CurrentStatus.PURCHASE_AGENCY_REQUEST, PurchaseAgency.ZINC, zincOrder.getRequestId());
            log.info("Synced successfully. Purchase Agency Id : {}, Source Order Id : {}", zincOrder.getRequestId(), order.getOrderId());
            return false;
        } catch (NoSuchElementException exception) {
            log.error("Exception occurred during sync Zinc order. Unable to find product entity. " +
                    "source_id : {}", zincOrder.getAsin());
            slackMessageUtil.sendError(String.format("Zinc 주문을 Sync 하려는 도중 product 엔티티가 존재하지 않습니다. " +
                    "source_id : %s", zincOrder.getAsin()));
            return false;
        }
    }

    public void processNewOrders() {
        if (!orderContext.isRunning()) {
            log.info("Order Context status is {}. Cannot proceed to next step. [Processing new Coupang orders]", orderContext.getRunning());
            return;
        }

        List<CoupangOrder> coupangOrderList = getCoupangOrderList();
        for (CoupangOrder coupangOrder : coupangOrderList) {
            fixedThreadPool.submit(() -> validateCoupangOrder(coupangOrder));
        }
    }

    public void checkDeliveryAgencyShipping() {
        List<CurrentStatus> currentStatusList = CurrentStatus.getCurrentStatusListContainingShipping();
        List<Order> orders = orderSheetService.getOrderListByCurrentStatusIn(currentStatusList);
        for (Order order : orders) {
            fixedThreadPool.submit(() -> updateDeliveryAgencyShipping(order));
        }
    }

    public void restartErrorOrders() {
        List<CurrentStatus> currentStatusList = CurrentStatus.getCurrentStatusListContainingError();
        List<Order> orders = orderSheetService.getOrderListByCurrentStatusIn(currentStatusList);
        for (Order order : orders) {
            fixedThreadPool.submit(() -> restartErrorOrder(order));
        }
    }

    public void updateRunningStatus(RunnerStatus status) {
        orderContext.updateRunningStatus(status);
    }

    public void checkRunningStatus() {
        if (orderContext.isRunning()) {
            return;
        }
        slackMessageUtil.sendError("`order-manager`::CoupangOrderService OrderContext 상태가 [STOP] 입니다.");
    }

    public void updateZincSucceededRequest(ZincResponse zincResponse) {
        fixedThreadPool.submit(() -> handleZincSucceededRequest(zincResponse));
    }

    public void updateZincFailedRequest(ZincResponse zincResponse) {
        fixedThreadPool.submit(() -> handleZincFailedRequest(zincResponse));
    }

    public void updateZincTrackingNumber(ZincResponse zincResponse) {
        fixedThreadPool.submit(() -> updatePurchaseAgencyInvoiceWebhook(zincResponse));
    }

    public void handleZincSucceededRequest(ZincResponse zincResponse) {
        log.info("Zinc succeeded-request from webhook process starts. Request Id : {}", zincResponse.getRequestId());
        String requestId = zincResponse.getRequestId();
        Order order = orderSheetService.getOrderByPurchaseAgencyRequestId(requestId);
        CancelledOrder cancelledOrder = orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(requestId);

        if (order == null && cancelledOrder == null) {
            slackMessageUtil.sendError(String.format("해당 Purchase Agency Id[%s]를 가진 order 혹은 cancelled_order를 찾을 수 없습니다.", requestId));
            return;
        }
        if (order != null && order.getCurrentStatus() == CurrentStatus.PURCHASE_AGENCY_REQUEST) {
            String orderId = order.getOrderId();
            CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorConfig.getVendorId(), Long.parseLong(orderId));
            if (coupangOrder == null) {
                log.error("Failed to get CoupangOrder 3 times in a row, to request delivery agency. Source Order Id : {}", orderId);
                orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_REQUEST);
                return;
            }
            orderSheetService.updatePurchaseAgencyOrder(order, CurrentStatus.PURCHASE_AGENCY_COMPLETE, zincResponse);
            sendKafkaWelcomeMessage(coupangOrder);
            requestDeliverAgency(order, zincResponse);
            return;
        }
        if (cancelledOrder != null) {
            Order orderFromCancelledOrder = cancelledOrder.getOrder();
            if (orderFromCancelledOrder.getCurrentStatus() != CurrentStatus.PURCHASE_AGENCY_CANCEL_REQUEST) {
                return;
            }
            updatePurchaseAgencyCancelWebhook(cancelledOrder, zincResponse);
        }
    }

    public void handleZincFailedRequest(ZincResponse zincResponse) {
        log.info("Zinc failed-request from webhook process starts. Request Id : {}", zincResponse.getRequestId());
        String requestId = zincResponse.getRequestId();
        Order order = orderSheetService.getOrderByPurchaseAgencyRequestId(requestId);
        CancelledOrder cancelledOrder = orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(requestId);

        try {
            if (order == null && cancelledOrder == null) {
                slackMessageUtil.sendError(String.format("해당 Purchase Agency Id[%s]를 가진 order 혹은 cancelled_order를 찾을 수 없습니다.", requestId));
                return;
            }

            if (order != null && order.getCurrentStatus() == CurrentStatus.PURCHASE_AGENCY_REQUEST) {
                amazonPurchaseService.getOrderStatus(requestId);
                return;
            }
            if (cancelledOrder != null) {
                Order orderFromCancelledOrder = cancelledOrder.getOrder();
                if (orderFromCancelledOrder.getCurrentStatus() != CurrentStatus.PURCHASE_AGENCY_CANCEL_REQUEST) {
                    return;
                }
                amazonPurchaseService.getCancelStatus(orderFromCancelledOrder.getPurchaseAgencyRequestId(), zincResponse.getRequestId());
                slackMessageUtil.sendError("Zinc 상품 취소 에러 발생, 에러 내용 : " + zincResponse.getCode());
            }
        } catch (CreditCardDeclinedException exception) {
            slackMessageUtil.sendError("Zinc 카드 거절 예외 발생. 스케쥴 잡을 멈춥니다. Purchase Agency Request Id : " + order.getPurchaseAgencyRequestId());
            orderSheetService.updateStatus(order, CurrentStatus.ERR_PURCHASE_AGENCY_REQUEST);
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        } catch (CancellationException exception) {
            slackMessageUtil.sendError("Zinc 상품 취소에 실패했습니다. 아마존/오마이집에 조회 및 취소 요청이 필요합니다. Source Order Id : " + cancelledOrder.getOrder().getOrderId());
        } catch (ZincApiException exception) {
            slackMessageUtil.sendError("Zinc 상품 구매/취소 요청 시 에러가 발생했습니다. [response=null] ");
            orderSheetService.updateStatus(order, CurrentStatus.ERR_PURCHASE_AGENCY_REQUEST);
        } catch (PaymentVerificationException exception) {
            slackMessageUtil.sendError(String.format("Zinc 결제 중 카드 인증 오류 발생. 스케쥴 잡을 멈춥니다. 직접 아마존에서 해당 상품이 구매되었는지 확인 후 취소해주세요. " +
                    "Source Order Id : %s, Purchase Agency Request Id : %s", order.getOrderId(), order.getPurchaseAgencyRequestId()));
            orderSheetService.updateStatus(order, CurrentStatus.ERR_PURCHASE_AGENCY_REQUEST);
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        } catch (PurchaseAgencyException exception) {
            slackMessageUtil.sendError(String.format("Zinc 구매 시 에러가 발생했습니다. 스케쥴 잡을 멈춥니다. 에러 내용 : %s, Source Order Id : %s", exception.getMessage(), order.getOrderId()));
            orderSheetService.updateStatus(order, CurrentStatus.ERR_PURCHASE_AGENCY_REQUEST);
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        }
    }

    private void restartSyncCoupangOrders(CoupangOrder coupangOrder) {
        try {
            log.info("Continue synced Coupang order. Source Order Id : {}", coupangOrder.getOrderId());
            String coupangOrderId = coupangOrder.getOrderId().toString();

            Long productId = coupangOrder.getProductId();
            if (productId == null) {
                log.error("There are no information for ASIN. Check CoupangOrder's product_id. Source Order Id : {}", coupangOrderId);
                slackMessageUtil.sendError(String.format("쿠팡 주문 [상태=상품준비중] sync 시 오류가 발생했습니다. 쿠팡 상품의 ASIN 정보가 존재하지 않습니다. 쿠팡 주문의 [product_id]를 확인해주세요. Source Order Id : %s", coupangOrderId));
                return;
            }
            HelloWorldAutoStoreProduct hwasProduct = hwasProductService.getHwasProductByDestinationId(productId.toString());
            String asin = hwasProduct.getSourceId();

            AmazonProductResponse amazonProduct = amazonProductValidator.fetchValidAmazonProductByAsin(asin);
            if (amazonProduct == null) {
                slackMessageUtil.sendError(String.format("쿠팡 주문 [상태=상품준비중] sync 시 오류가 발생했습니다. Rainforest API 아마존 상품 조회 호출에 3번 연속 실패했습니다. Source Order Id : %s", coupangOrderId));
                return;
            }
            orderSheetService.saveOrderFrom(hwasProduct, coupangOrder.getOrderId().toString(), CurrentStatus.ERR_SOURCE_STATUS_TO_PREPARE, coupangOrder.getPaidAt());
        } catch (NotEnoughApiCreditException exception) {
            slackMessageUtil.sendError("Rainforest API 호출 크레딧이 부족합니다.");
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        } catch (InvalidAsinException exception) {
            Long productId = coupangOrder.getProductId();
            HelloWorldAutoStoreProduct hwasProduct = hwasProductService.getHwasProductByDestinationId(productId.toString());
            slackMessageUtil.sendError(String.format("Rainforest API 호출에서 존재하지 않는 ASIN을 사용했습니다. " +
                    "source_id : %s", hwasProduct.getSourceId()));
//            cancelSourceOrder(coupangOrder, hwasProduct, CurrentStatus.CANCEL_COMPLETE_INVALID_ASIN);
        } catch (NoSuchElementException exception) {
            log.error("Exception occurred during sync Coupang order. Unable to find product entity. " +
                    "destination_id : {}, order_id : {}", coupangOrder.getProductId(), coupangOrder.getOrderId());
            slackMessageUtil.sendError(String.format("Sync 해야할 product 엔티티가 존재하지 않습니다. " +
                    "destination_id : %s, order_id : %s", coupangOrder.getProductId(), coupangOrder.getOrderId()));
        }
    }

    private List<CoupangOrder> getCoupangOrderList() {
        try {
            return coupangOrderFetcher.fetchPaymentCompleteOrders(vendorConfig.getVendorId());
        } catch (CoupangApiException exception) {
            slackMessageUtil.sendError("쿠팡 [결제완료] 주문 목록 호출을 3번 연속 실패했습니다.");
            return new ArrayList<>();
        }
    }

    protected void validateCoupangOrder(CoupangOrder coupangOrder) {
        try {
            Long coupangOrderId = coupangOrder.getOrderId();
            log.info("Updating Coupang order status to Prepare-Shipping. Source Order Id : {}", coupangOrderId);
            Long productId = coupangOrder.getProductId();
            if (productId == null) {
                log.error("There are no information for ASIN. Check CoupangOrder's product_id. Source Order Id : {}", coupangOrderId);
                slackMessageUtil.sendError(String.format("쿠팡 주문 정보 중 ASIN 정보(product_id)가 부재합니다. 확인이 필요합니다. Source Order Id : %s", coupangOrderId));
                return;
            }
            HelloWorldAutoStoreProduct hwasProduct = hwasProductService.getHwasProductByDestinationId(productId.toString());
            String asin = hwasProduct.getSourceId();
            AmazonProductResponse amazonProduct = amazonProductValidator.fetchValidAmazonProductByAsin(asin);
            if (amazonProduct == null) {
                slackMessageUtil.sendError("Rainforest API 아마존 상품 조회 호출에 3번 연속 실패했습니다.");
                return;
            }

            ValidationStatus status = amazonProductValidator.validateAmazonProduct(amazonProduct.getQuantity(), amazonProduct.getPrice(),
                    hwasProduct.getSourceOriginalPrice(), hwasProduct.getSourceId(), coupangOrderId);
            switch (status) {
                case COMPLETE:
                    String message = String.format("`New-Order`:: 새로운 주문이 접수되었습니다." +
                                    "```\n상품명: %s\n주문 번호: %s\n고객 성함: %s\n고객 연락처: %s\nASIN: %s```",
                            coupangOrder.getVendorItemName(), coupangOrder.getOrderId(), coupangOrder.getOrdererName(),
                            coupangOrder.getPhoneNumber(), coupangOrder.getAsin());
                    slackMessageUtil.sendOrder(message);
                    prepareShipment(coupangOrder, hwasProduct, amazonProduct);
                    return;
                case ERR_QUANTITY:
                    slackMessageUtil.sendError("해당 쿠팡 주문 건에 대한 제품 구매가 불가능합니다. 메뉴얼한 대응이 필요합니다. Amazon 재고 이슈, Source Order Id : " + coupangOrderId);
//                    cancelSourceOrder(coupangOrder, hwasProduct, CurrentStatus.CANCEL_COMPLETE_INVALID_QUANTITY);
                    break;
                case ERR_PRICE:
                    slackMessageUtil.sendError("해당 쿠팡 주문 건에 대한 제품 구매가 불가능합니다. 메뉴얼한 대응이 필요합니다. Amazon 가격 이슈, Source Order Id : " + coupangOrderId);
//                    cancelSourceOrder(coupangOrder, hwasProduct, CurrentStatus.CANCEL_COMPLETE_INVALID_PRICE);
                    break;
                case ERR_RESPONSE:
                    break;
            }
        } catch (NoSuchElementException exception) {
            log.error("Exception occurred during processing new Coupang order. Unable to find product entity. " +
                    "destination_id : {}, order_id : {}", coupangOrder.getProductId(), coupangOrder.getOrderId());
            slackMessageUtil.sendError(String.format("새로 접수된 쿠팡 주문 건에 해당하는 product 엔티티가 존재하지 않습니다. " +
                    "destination_id : %s, orderId : %s", coupangOrder.getProductId(), coupangOrder.getOrderId()));
        } catch (NotEnoughApiCreditException exception) {
            slackMessageUtil.sendError("Rainforest API 호출 크레딧이 부족합니다.");
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        } catch (InvalidAsinException exception) {
            Long productId = coupangOrder.getProductId();
            HelloWorldAutoStoreProduct hwasProduct = hwasProductService.getHwasProductByDestinationId(productId.toString());
            slackMessageUtil.sendError("Rainforest API 호출에서 존재하지 않는 ASIN을 사용했습니다. ASIN : " + hwasProduct.getSourceId());
//            cancelSourceOrder(coupangOrder, hwasProduct, CurrentStatus.CANCEL_COMPLETE_INVALID_ASIN);
        }
    }

    protected void prepareShipment(CoupangOrder coupangOrder, HelloWorldAutoStoreProduct hwasProduct, AmazonProductResponse amazonProduct) {
        try {
            Long coupangOrderId = coupangOrder.getOrderId();
            PrepareShipmentDto.Response response = coupangShippingService.updateStatusToPrepareShipment(vendorConfig.getVendorId(), coupangOrderId, coupangOrder.getShipmentBoxId());
            if (response == null) {
                slackMessageUtil.sendError(String.format("쿠팡 주문 상태 변경에 [결제완료 -> 상품준비중] 실패했습니다. Source Order Id : %d", coupangOrderId));
                return;
            }

            log.info("Coupang order status updated into Prepare-Shipping. Source Order Id : {}", coupangOrderId);
            Order order = orderSheetService.saveOrderFrom(hwasProduct, coupangOrderId.toString(), CurrentStatus.SOURCE_STATUS_TO_PREPARE, coupangOrder.getPaidAt());
            requestPurchaseAgency(order, amazonProduct);
        } catch (CoupangApiException exception) {
            boolean paymentComplete = coupangOrderFetcher.isStatusPaymentComplete(vendorConfig.getVendorId(), coupangOrder.getOrderId());
            if (!paymentComplete) {
                log.error("Coupang client cancelled order when order status was payment-complete.");
                slackMessageUtil.sendError("결제완료 -> 상품준비중 처리 과정 중 고객이 주문을 취소했습니다. Source Order Id : " + coupangOrder.getOrderId());
                return;
            }
            slackMessageUtil.sendError("결제완료 -> 상품준비중 처리에 실패했습니다. Source Order Id : " + coupangOrder.getOrderId());
        }
    }

    private void requestPurchaseAgency(Order order, AmazonProductResponse amazonProduct) {
        String orderId = order.getOrderId();
        String requestId = amazonPurchaseService.purchaseOrder(orderId, amazonProduct.getAsin(), amazonProduct.getPrice());
        if (requestId == null) {
            slackMessageUtil.sendError("Zinc 상품 구매 요청에 실패했습니다. Source Order Id : " + orderId);
            orderSheetService.updateStatus(order, CurrentStatus.ERR_PURCHASE_AGENCY_REQUEST);
            return;
        }
        orderSheetService.updatePurchaseAgencyRequest(order, CurrentStatus.PURCHASE_AGENCY_REQUEST, PurchaseAgency.ZINC, requestId);
    }

    protected void requestDeliverAgency(Order order, ZincResponse zincOrder) {
        try {
            String orderId = order.getOrderId();
            log.info("Check if there's a refund request from Coupang client before request delivery agency. Source Order Id : {}", orderId);
            RefundOrder.Item refundItem = coupangCancelService.getRefundItemByOrderId(vendorConfig.getVendorId(), Long.parseLong(orderId));
            if (refundItem != null) {
                log.info("Refund request has made from Coupang client. Source Order Id : {}", orderId);
                Long cancelId = refundItem.getCancelId();
                CancelledOrder cancelledOrder = orderSheetService.saveCancelFrom(order, cancelId.toString());

                String cancelOrderRequestId = amazonPurchaseService.cancelOrder(zincOrder.getRequestId(), zincOrder.getMerchantOrderId());
                if (cancelOrderRequestId == null) {
                    log.error("Failed to handle refund request from Coupang client. Source Order Id : {}", orderId);
                    slackMessageUtil.sendError("Zinc 상품 취소에 실패했습니다. Purchase Agency Request Id : " + zincOrder.getRequestId());
                    orderSheetService.updateStatus(order, CurrentStatus.ERR_PURCHASE_AGENCY_CANCEL_REQUEST);
                    return;
                }
                log.info("Source client refund request completed. Source Order Id : {}", order.getOrderId());
                orderSheetService.updatePurchaseAgencyCancel(order, CurrentStatus.PURCHASE_AGENCY_CANCEL_REQUEST, cancelledOrder, cancelOrderRequestId);
                return;
            }

            log.info("There were no source client refund request. Fetch CoupangOrder to request delivery agency. Source Order Id : {}", orderId);
            CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorConfig.getVendorId(), Long.parseLong(orderId));
            if (coupangOrder == null) {
                log.error("Failed to get CoupangOrder 3 times in a row, to request delivery agency. Source Order Id : {}", orderId);
                slackMessageUtil.sendError("쿠팡 주문 호출을 3번 연속 실패했습니다. Source Order Id : " + orderId);
                orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_REQUEST);
                return;
            }


            Long productId = coupangOrder.getProductId();
            HelloWorldAutoStoreProduct hwasProduct = hwasProductService.getHwasProductByDestinationId(productId.toString());

            AmazonProductResponse amazonProductResponse = amazonProductValidator.fetchValidAmazonProductByAsin(hwasProduct.getSourceId());
            String amazonProductTitle = amazonProductResponse.getTitle();

            OhmyzipDeliveryRequest ohmyzipRequest =
                    OhmyzipDeliveryRequest.create(coupangOrder, zincOrder, hwasProduct, amazonProductTitle);
            OhmyzipResponse ohmyzipResponse = ohmyzipService.requestDeliverAgency(ohmyzipRequest, order.getOrderId());
            if (ohmyzipResponse == null) {
                slackMessageUtil.sendError("배대지 신청 오류. Source Order Id : " + order.getOrderId());
                orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_REQUEST);
                return;
            }
            orderSheetService.updateDeliveryAgencyResponse(order, CurrentStatus.DELIVERY_AGENCY_REQUEST, DeliveryAgency.OH_MY_ZIP, ohmyzipResponse);
            updateInvoice(order, coupangOrder);
        } catch (NoSuchElementException exception) {
            log.error("Exception occurred during processing new Coupang order. Unable to find product entity. " +
                    "order_id : {}", order.getOrderId());
            slackMessageUtil.sendError(String.format("배대지 신청할 product 엔티티가 존재하지 않습니다. order_id : %s", order.getOrderId()));
            orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_REQUEST);
        } catch (OhmyzipApiException exception) {
            slackMessageUtil.sendError(String.format("배대지 신청 오류 발생. 에러 메세지 : %s, Source Order Id : %s", exception.getMessage(), order.getOrderId()));
            orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_REQUEST);
        } catch (InvalidAsinException exception) {
            slackMessageUtil.sendError("Rainforest API 호출에서 존재하지 않는 ASIN을 사용했습니다. Source Order Id : " + order.getOrderId());
            orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_REQUEST);
        } catch (NotEnoughApiCreditException exception) {
            slackMessageUtil.sendError("Rainforest API 호출 크레딧이 부족합니다.");
            orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_REQUEST);
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        }
    }

    protected void updateInvoice(Order order, CoupangOrder coupangOrder) {
        try {
            String trackingNumber = order.getDomesticTrackingNumber();
            PrepareShipmentDto.Response response = coupangShippingService.updateInvoice(vendorConfig.getVendorId(), coupangOrder, trackingNumber);
            if (response == null) {
                slackMessageUtil.sendError(String.format("쿠팡 주문 송장 업데이트 시 오류가 발생했습니다. Source Order Id : %s, Domestic Tracking Number : %s", order.getOrderId(), trackingNumber));
                orderSheetService.updateStatus(order, CurrentStatus.ERR_SOURCE_INVOICE_UPDATE);
                return;
            }
            orderSheetService.updateStatus(order, CurrentStatus.SOURCE_INVOICE_UPDATE);
        } catch (CoupangApiException exception) {
            slackMessageUtil.sendError(String.format("쿠팡 송장 업로드 중 송장번호 이상으로 오류가 발생했습니다. Source Order Id : %s, Domestic Tracking Number : %s",
                    order.getOrderId(), order.getDomesticTrackingNumber()));
            orderSheetService.updateStatus(order, CurrentStatus.ERR_SOURCE_INVOICE_UPDATE);
        }
    }

    public void updatePurchaseAgencyInvoiceWebhook(ZincResponse zincResponse) {
        log.info("Updating Purchase Agency invoice status from Zinc Webhook. Purchase Agency Request Id : {}", zincResponse.getRequestId());
        String zincRequestId = zincResponse.getRequestId();
        Order order = orderSheetService.getOrderByPurchaseAgencyRequestId(zincRequestId);
        if (order == null) {
            slackMessageUtil.sendError(String.format("해당 Purchase Agency Request Id[%s]를 가진 order를 찾을 수 없습니다.", zincRequestId));
            return;
        }

        try {
            CurrentStatus currentStatus = order.getCurrentStatus();
            switch (currentStatus) {
                case SOURCE_INVOICE_UPDATE:
                    if (needRefund(order)) {
                        return;
                    }
                    String orderId = order.getOrderId();
                    CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorConfig.getVendorId(), Long.parseLong(orderId));
                    if (coupangOrder == null) {
                        log.error("Failed to get CoupangOrder 3 times in a row, to request delivery agency. Source Order Id : {}", orderId);
                        orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_UPDATE);
                        return;
                    }
                    sendKafkaShippingMessage(coupangOrder, order.getDomesticTrackingNumber());

                    String trackingNumber = zincResponse.getTrackingNumber();
                    if (orderSheetService.existsByInternationalTracking(trackingNumber)) {
                        slackMessageUtil.sendOrder("`상품 분할 필요`:: 중복된 (해외)송장 번호가 존재합니다. 상품 분할 요청이 필요합니다. " +
                                "international_tracking_number: " + trackingNumber);
                    }
                    updateDeliveryAgencyInvoice(order, zincResponse);
                    break;
                case DELIVERY_AGENCY_SHIPPING_WAIT:
                case DELIVERY_AGENCY_SHIPPING_START:
                case DELIVERY_AGENCY_SHIPPING_CUSTOMS:
                case DELIVERY_AGENCY_COMPLETE:
                    log.info("Shipping from Purchase Agency is completed. purchase_agency_request_id : {}", zincRequestId);
                    return;
                default:
                    log.error("Unstated order's shipping information received. purchase_agency_request_id : {}",
                            zincRequestId);
                    slackMessageUtil.sendError(String.format("Purchase Agency에서 수신한 배송 정보가 주문 상태와 매칭되지 않습니다. " +
                            "purchase_agency_request_id : %s, order_id : %s", zincRequestId, order.getOrderId()));
            }
        } catch (CreditCardDeclinedException exception) {
            grantRefundClaim(order);
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        }
    }

    private void grantRefundClaim(Order order) {
        CancelledOrder cancelledOrder = orderSheetService.getCancelByOrder(order);
        String cancelId = cancelledOrder.getCancelId();
        String vendorId = vendorConfig.getVendorId();
        boolean isRefundComplete = coupangCancelService.isRefundComplete(vendorId, Long.parseLong(cancelId));
        if (isRefundComplete) {
            slackMessageUtil.sendOrder("쿠팡의 환불 요청으로 Zinc 상품 취소 확인 중, [카드 잔액 오류 발생].\nZinc 에서 구매가 이뤄지지 않아 쿠팡 환불 요청건은 자동으로 처리되었습니다.");
            orderSheetService.updateSourceRefund(order, cancelledOrder);
            return;
        }
        orderSheetService.updateStatus(order, CurrentStatus.HANDLE_MANUALLY);
        slackMessageUtil.sendError("쿠팡의 환불 요청으로 Zinc 상품 취소 확인 중, [카드 잔액 오류 발생].\n쿠팡 자동 환불에 실패했습니다. 직접 확인 후 환불 작업 진행이 필요합니다. Source Cancel Id : " + cancelId);
    }

    private void updateDeliveryAgencyInvoice(Order order, ZincResponse zincShipping) {
        try {
            String sourceId = zincShipping.getAsin();
            AmazonProductResponse amazonProductResponse = amazonProductValidator.fetchValidAmazonProductByAsin(sourceId);
            String amazonProductTitle = amazonProductResponse.getTitle();

            OhmyzipTrackingUpdateRequest ohmyzipRequest
                    = OhmyzipTrackingUpdateRequest.create(order.getDeliveryAgencyRequestId(), amazonProductTitle, zincShipping);
            OhmyzipResponse ohmyzipResponse = ohmyzipService.updateInvoice(ohmyzipRequest, order.getOrderId());
            if (ohmyzipResponse == null) {
                slackMessageUtil.sendError(String.format("오마이집 송장 업데이트 시 오류가 발생했습니다. Source Order Id : %s", order.getOrderId()));
                orderSheetService.updatePurchaseAgencyShipping(order, CurrentStatus.ERR_DELIVERY_AGENCY_UPDATE, zincShipping);
                return;
            }
            orderSheetService.updatePurchaseAgencyShipping(order, CurrentStatus.DELIVERY_AGENCY_SHIPPING_WAIT, zincShipping);
        } catch (OhmyzipApiException exception) {
            slackMessageUtil.sendError(String.format("배대지 송장 업데이트 오류 발생. 오마이집에서 직접 확인이 필요합니다. Source Order Id : %s", order.getOrderId()));
            orderSheetService.updatePurchaseAgencyShipping(order, CurrentStatus.ERR_DELIVERY_AGENCY_UPDATE, zincShipping);
        } catch (NoSuchElementException exception) {
            log.error("Exception occurred during updating invoice on Ohmyzip. Unable to find product entity. " +
                    "source_id : {}, Source Order Id : {}", zincShipping.getAsin(), order.getOrderId());
            slackMessageUtil.sendError(String.format("배대지 송장 업데이트 할 product 엔티티가 존재하지 않습니다. " +
                    "source_id : %s, Source Order Id : %s", zincShipping.getAsin(), order.getOrderId()));
            orderSheetService.updatePurchaseAgencyShipping(order, CurrentStatus.ERR_DELIVERY_AGENCY_UPDATE, zincShipping);
        } catch (InvalidAsinException exception) {
            slackMessageUtil.sendError("Rainforest API 호출에서 존재하지 않는 ASIN을 사용했습니다. Source Order Id : " + order.getOrderId());
            orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_UPDATE);
        } catch (NotEnoughApiCreditException exception) {
            slackMessageUtil.sendError("Rainforest API 호출 크레딧이 부족합니다.");
            orderSheetService.updateStatus(order, CurrentStatus.ERR_DELIVERY_AGENCY_UPDATE);
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        }
    }

    private void updatePurchaseAgencyCancelWebhook(CancelledOrder cancelledOrder, ZincResponse zincResponse) {
        Order order = cancelledOrder.getOrder();
        slackMessageUtil.sendOrder(String.format("Zinc 상품 취소 완료. Source Order Id : %s, Purchase Agency Cancel Id : %s", order.getOrderId(), zincResponse.getRequestId()));
        stopReleaseSourceOrder(cancelledOrder, order);
    }

    private void stopReleaseSourceOrder(CancelledOrder cancelledOrder, Order order) {
        String cancelId = cancelledOrder.getCancelId();
        boolean ableToCompleteCancellation = coupangCancelService.canStopRelease(vendorConfig.getVendorId(), Long.parseLong(cancelId));
        if (!ableToCompleteCancellation) {
            slackMessageUtil.sendError("쿠팡 출고 중지 실패. Source Order Id : " + order.getOrderId());
            return;
        }

        orderSheetService.updateStatus(order, CurrentStatus.PURCHASE_AGENCY_CANCEL_COMPLETE);
        orderSheetService.updateCancelDate(cancelledOrder);
        slackMessageUtil.sendOrder(String.format("쿠팡 출고 중지 성공. Source Order Id : %s, Cancel Id : %s", order.getOrderId(), cancelId));
    }

    protected void cancelSourceOrder(CoupangOrder coupangOrder, HelloWorldAutoStoreProduct hwasProduct, CurrentStatus status) {
        log.info("Cancel Coupang order start. Source Order Id : {}", coupangOrder.getOrderId());
        Long orderId = coupangOrder.getOrderId();

        String cancelId = coupangCancelService.cancelOrder(orderId, coupangOrder.getVendorItemId(), vendorConfig.getVendorId(), vendorConfig.getVendorUserId());
        if (cancelId == null) {
            slackMessageUtil.sendError("쿠팡 상품 취소를 위한 API 호출 시 오류 발생. Source Order Id : " + orderId);
            return;
        }

        Order order = orderSheetService.saveOrderFrom(hwasProduct, orderId.toString(), status, coupangOrder.getPaidAt());
        orderSheetService.saveCancelFrom(order, cancelId, LocalDateTime.now());

        log.info("Cancel Coupang order complete. Source Order Id : {}", orderId);
        slackMessageUtil.sendOrder(String.format("쿠팡 상품 취소 완료. Source Order Id : %s, Coupang Cancel Id : %s", orderId, cancelId));
    }

    protected void updateDeliveryAgencyShipping(Order order) {
        try {
            String orderId = order.getOrderId();
            log.info("Updating shipping status from delivery agency. Source Order Id : {}", orderId);
            if (needRefund(order)) {
                return;
            }

            String shippingStatus = ohmyzipService.getShippingStatus(order.getDeliveryAgencyRequestId(), orderId);
            CurrentStatus updatedStatus = DeliveryAgencyShippingStatus.findCurrentStatus(shippingStatus);
            if (order.getCurrentStatus() == updatedStatus) {
                log.info("Shipping status has no change since last update. Source Order Id: {}", orderId);
                return;
            }
            orderSheetService.updateStatus(order, updatedStatus);
            switch (updatedStatus) {
                case DELIVERY_AGENCY_SHIPPING_WAIT:
                    LocalDate estimatedArrivalDate = order.getPurchaseAgencyRequestComplete()
                            .toLocalDate()
                            .plusDays(SHIPPING_DAYS);
                    if (estimatedArrivalDate.isBefore(LocalDate.now())) {
                        String message = String.format("`order-manager`::`배송 지연`이 발생했습니다. 확인이 필요합니다. `order_id: %s`", orderId);
                        slackMessageUtil.sendError(message);
                    }
                    break;
                case DELIVERY_AGENCY_SHIPPING_START:
                    if (order.getDeliveryAgencyShippingStart() == null) {
                        orderSheetService.updateDeliveryShippingStart(order, LocalDateTime.now());
                    }
                    break;
                case DELIVERY_AGENCY_SHIPPING_CUSTOMS:
                case ERR_DELIVERY_AGENCY_STATUS:
                    break;
                case DELIVERY_AGENCY_COMPLETE:
                    log.info("Shipping has been completed. Add one sold_count. Source Order Id: {}", orderId);
                    orderSheetService.updateDeliveryShippingComplete(order, LocalDateTime.now());
                    HelloWorldAutoStoreProduct hwasProduct = order.getHelloWorldAutoStoreProduct();
                    HelloWorldAutoStoreProduct updatedHwasProduct = hwasProductService.updateSoldProduct(hwasProduct);
                    log.info("Updated hwasProduct detail : {}", updatedHwasProduct);
                    break;
                default:
                    throw new RuntimeException("Unreachable statement.");
            }
            orderSheetService.updateStatus(order, updatedStatus);
        } catch (CreditCardDeclinedException exception) {
            orderSheetService.updateStatus(order, CurrentStatus.HANDLE_MANUALLY);
            slackMessageUtil.sendError("쿠팡의 환불 요청으로 Zinc 상품 취소 확인 중, 카드 잔액 오류 발생. (Zinc 에서 구매가 이뤄지지 않아 환불 요청건은 자동으로 처리됩니다.)");
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        }
    }

    private boolean needRefund(Order order) throws CreditCardDeclinedException {
        RefundOrder.Item refundItem = coupangCancelService.getRefundItemByOrderId(vendorConfig.getVendorId(), Long.parseLong(order.getOrderId()));
        if (refundItem != null) {
            log.info("Refund request has made from Coupang client. Source Order Id : {}", order.getOrderId());
            Long cancelId = refundItem.getCancelId();
            CancelledOrder cancelledOrder = orderSheetService.saveCancelFrom(order, cancelId.toString());

            String requestId = order.getPurchaseAgencyRequestId();
            String cancelOrderRequestId = amazonPurchaseService.cancelOrderByRequestId(requestId);

            if (cancelOrderRequestId == null) {
                slackMessageUtil.sendOrder(("Zinc 구매가 완료된 상품의 환불 요청이 접수되었습니다. Zinc에 구매 취소 요청했으나 실패했습니다. 이후 확인이 필요합니다. Source Order Id : " + order.getOrderId()));
                orderSheetService.updateStatus(order, CurrentStatus.HANDLE_MANUALLY);
                return true;
            }
            slackMessageUtil.sendOrder(("Zinc 구매가 완료된 상품의 환불 요청이 접수되었습니다. Zinc에 취소 요청까지 처리되었습니다. 이후 확인이 필요합니다. Source Order Id : " + order.getOrderId()));
            orderSheetService.updatePurchaseAgencyCancel(order, CurrentStatus.HANDLE_MANUALLY, cancelledOrder, cancelOrderRequestId);
            return true;
        }
        return false;
    }

    protected void restartErrorOrder(Order order) {
        try {
            String orderId = order.getOrderId();
            CurrentStatus status = order.getCurrentStatus();
            log.info("Restarting errored-order. Source Order Id : {}", orderId);
            switch (status) {
                case ERR_SOURCE_STATUS_TO_PREPARE:
                    if (!orderContext.isRunning()) {
                        log.info("Order Context status is {}. Cannot proceed to next step. Source Order Id : {}", orderContext.getRunning(), orderId);
                        return;
                    }

                    CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorConfig.getVendorId(), Long.parseLong(orderId));
                    if (coupangOrder != null) {
                        HelloWorldAutoStoreProduct hwasProduct = order.getHelloWorldAutoStoreProduct();
                        String asin = hwasProduct.getSourceId();
                        AmazonProductResponse amazonProduct = amazonProductValidator.fetchValidAmazonProductByAsin(asin);
                        requestPurchaseAgency(order, amazonProduct);
                        return;
                    }

                    RefundOrder.Item cancelItem = coupangCancelService.getCancelItemByOrderId(vendorConfig.getVendorId(), Long.parseLong(orderId));
                    if (cancelItem != null) {
                        log.info("Coupang order was cancelled by client. [From order status=ACCEPT] Source Order Id : {}", orderId);
                        slackMessageUtil.sendOrder(String.format("쿠팡 고객에 의해 [결제완료] 상태 주문이 취소되었습니다. Source Order Id : %s", orderId));
                        return;
                    }
                    log.error("Cannot fetch coupang order by source_order_id. Even there is no coupang cancel request from a client. Source Order Id : {}", orderId);
                    slackMessageUtil.sendError("쿠팡 주문 호출을 3번 연속 실패했습니다. Source Order Id : " + orderId);
                    break;
                case ERR_PURCHASE_AGENCY_REQUEST:
                    if (!orderContext.isRunning()) {
                        log.info("Order Context status is {}. Cannot proceed to next step. Source Order Id : {}", orderContext.getRunning(), orderId);
                        return;
                    }

                    HelloWorldAutoStoreProduct hwasProduct = order.getHelloWorldAutoStoreProduct();
                    String asin = hwasProduct.getSourceId();
                    AmazonProductResponse amazonProduct = amazonProductValidator.fetchValidAmazonProductByAsin(asin);
                    requestPurchaseAgency(order, amazonProduct);
                    break;
                case ERR_PURCHASE_AGENCY_CANCEL_REQUEST:
                case ERR_SOURCE_REFUND_REQUEST_INQUIRY:
                case ERR_DELIVERY_AGENCY_REQUEST:
                    String requestId = order.getPurchaseAgencyRequestId();
                    ZincResponse zincOrder = amazonPurchaseService.getCompletedOrderStatus(requestId);
                    if (zincOrder == null) {
                        slackMessageUtil.sendError(String.format("Zinc 주문 현황 조회 시 오류가 발생했습니다. Purchase Agency Request Id : %s", requestId));
                        return;
                    }
                    requestDeliverAgency(order, zincOrder);
                    break;
                case ERR_DELIVERY_AGENCY_UPDATE:
                    if (needRefund(order)) {
                        return;
                    }
                    requestId = order.getPurchaseAgencyRequestId();
                    ZincResponse zincShipping = amazonPurchaseService.getShippingStatus(requestId);
                    if (zincShipping == null || zincShipping.isEmpty()) {
                        log.info("Purchase Agency shipping information does not exist yet. Source Order Id = {}", order.getOrderId());
                        return;
                    }
                    updateDeliveryAgencyInvoice(order, zincShipping);
                    break;
                case ERR_DELIVERY_AGENCY_STATUS:
                    updateDeliveryAgencyShipping(order);
                    break;
                case ERR_SOURCE_INVOICE_UPDATE:
                    coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorConfig.getVendorId(), Long.parseLong(orderId));
                    if (coupangOrder == null) {
                        slackMessageUtil.sendError("쿠팡 [결제완료] 주문 호출을 3번 연속 실패했습니다. Source Order Id : " + orderId);
                        return;
                    }
                    updateInvoice(order, coupangOrder);
                    break;
                default:
                    throw new RuntimeException("Unreachable statement.");
            }
        } catch (NotEnoughApiCreditException exception) {
            slackMessageUtil.sendError("Rainforest API 호출 크레딧이 부족합니다.");
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        } catch (InvalidAsinException exception) {
            HelloWorldAutoStoreProduct hwasProduct = order.getHelloWorldAutoStoreProduct();
            slackMessageUtil.sendError("Rainforest API 호출에서 존재하지 않는 ASIN을 사용했습니다. ASIN : " + hwasProduct.getSourceId());

            CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorConfig.getVendorId(),
                    Long.parseLong(order.getOrderId()));
            if (coupangOrder == null) {
                slackMessageUtil.sendError("쿠팡 [결제완료] 주문 호출을 3번 연속 실패했습니다. Source Order Id : " + order.getOrderId());
                return;
            }
//            cancelSourceOrder(coupangOrder, hwasProduct, CurrentStatus.CANCEL_COMPLETE_INVALID_ASIN);
        } catch (CreditCardDeclinedException exception) {
            grantRefundClaim(order);
            orderContext.updateRunningStatus(RunnerStatus.STOP);
        }
    }

    public TrackingDto getTrackingNumber(String orderId) {
        return orderSheetService.getTrackingNumber(orderId);
    }

    public VendorOrder getCoupangOrder(String orderId) {
        log.info("Getting coupang order info start. order_id: {}", orderId);
        long coupangOrderId = Long.parseLong(orderId);
        CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorConfig.getVendorId(), coupangOrderId);
        log.info("Getting coupang order info complete. order_id: {}, coupangOrder: {}", orderId, coupangOrder);
        return coupangOrder;
    }

    private void sendKafkaWelcomeMessage(CoupangOrder coupangOrder) {
        CoupangOrder.Orderer orderer = coupangOrder.getOrderer();
        Welcome welcomeDto = Welcome.builder()
                .orderId(coupangOrder.getOrderId().toString())
                .customerName(orderer.getName())
                .customerPhoneNumber(coupangOrder.getPhoneNumber())
                .productName(coupangOrder.getVendorItemName())
                .messagePlatform(MESSAGE_PLATFORM)
                .build();
        kafkaProducerService.sendWelcomeMessage(welcomeDto);
    }

    private void sendKafkaShippingMessage(CoupangOrder coupangOrder, String trackingNumber) {
        CoupangOrder.Orderer orderer = coupangOrder.getOrderer();
        Shipping shippingDto = Shipping.builder()
                .orderId(coupangOrder.getOrderId().toString())
                .customerName(orderer.getName())
                .customerPhoneNumber(coupangOrder.getPhoneNumber())
                .productName(coupangOrder.getVendorItemName())
                .shippingInvoice(trackingNumber)
                .messagePlatform(MESSAGE_PLATFORM)
                .build();
        kafkaProducerService.sendShippingMessage(shippingDto);
    }

}
