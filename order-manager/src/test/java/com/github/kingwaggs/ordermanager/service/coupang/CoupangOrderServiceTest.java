package com.github.kingwaggs.ordermanager.service.coupang;

import com.github.kingwaggs.ordermanager.coupangsdk.config.CoupangVendorConfig;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.RefundOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping.PrepareShipmentDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import com.github.kingwaggs.ordermanager.domain.OrderContext;
import com.github.kingwaggs.ordermanager.domain.RunnerStatus;
import com.github.kingwaggs.ordermanager.domain.ValidationStatus;
import com.github.kingwaggs.ordermanager.domain.dto.response.AmazonProductResponse;
import com.github.kingwaggs.ordermanager.domain.dto.response.OhmyzipResponse;
import com.github.kingwaggs.ordermanager.domain.dto.response.ZincResponse;
import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.ordermanager.domain.sheet.CancelledOrder;
import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import com.github.kingwaggs.ordermanager.domain.sheet.DeliveryAgency;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import com.github.kingwaggs.ordermanager.exception.*;
import com.github.kingwaggs.ordermanager.service.HelloWorldAutoStoreProductService;
import com.github.kingwaggs.ordermanager.service.KafkaProducerService;
import com.github.kingwaggs.ordermanager.service.OhmyzipService;
import com.github.kingwaggs.ordermanager.service.OrderSheetService;
import com.github.kingwaggs.ordermanager.service.amazon.AmazonProductValidator;
import com.github.kingwaggs.ordermanager.service.amazon.AmazonPurchaseService;
import com.github.kingwaggs.ordermanager.util.SlackMessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoupangOrderServiceTest {

    @InjectMocks
    private CoupangOrderService coupangOrderService;

    @Mock
    private CoupangOrderFetcher coupangOrderFetcher;
    @Mock
    private CoupangOrderCancelService coupangCancelService;
    @Mock
    private CoupangOrderShippingService coupangShippingService;
    @Mock
    private HelloWorldAutoStoreProductService hwasProductService;
    @Mock
    private OrderSheetService orderSheetService;
    @Mock
    private AmazonProductValidator amazonProductValidator;
    @Mock
    private AmazonPurchaseService amazonPurchaseService;
    @Mock
    private OhmyzipService ohmyzipService;
    @Mock
    private CoupangVendorConfig vendorConfig;
    @Mock
    private SlackMessageUtil slackMessageUtil;
    @Mock
    private OrderContext orderContext;
    @Mock
    private KafkaProducerService kafkaProducerService;


    private static final String VENDOR_ID = "vendorId";
    private static final String PAID_AT = "2022-08-17T12:11:23";
    private static final String ASIN = "B09ZP1NRKT";
    private static final String ORDER_ID = "123456789";
    private static final String REQUEST_ID = "192837465";
    private static final String CANCEL_ID = "675849390";
    private CoupangOrder coupangOrder;
    private AmazonProductResponse amazonProduct;
    private HelloWorldAutoStoreProduct hwasProduct;
    private ZincResponse zincResponse;
    private Order order;

    @BeforeEach
    void setUp() {
        coupangOrder = mock(CoupangOrder.class);
        amazonProduct = mock(AmazonProductResponse.class);
        hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        zincResponse = mock(ZincResponse.class);
        order = mock(Order.class);
    }

    @Test
    @DisplayName("[결제완료] 상태 주문 조회 성공 시")
    void processNewOrdersSuccessfully() throws CoupangApiException {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(orderContext.isRunning()).thenReturn(true);
        when(coupangOrderFetcher.fetchPaymentCompleteOrders(anyString())).thenReturn(List.of(coupangOrder));

        // when
        coupangOrderService.processNewOrders();

        // then
        verify(orderContext, times(1)).isRunning();
        verify(vendorConfig, times(1)).getVendorId();
        verify(coupangOrderFetcher, times(1)).fetchPaymentCompleteOrders(anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    @DisplayName("[결제완료] 상태 주문 조회 성공 시 - 로그로 스레드풀 확인")
    void processNewOrdersSuccessfullyWithThreadPool() throws CoupangApiException {
        // given
        CoupangOrder coupangOrder1 = mock(CoupangOrder.class);
        CoupangOrder coupangOrder2 = mock(CoupangOrder.class);
        CoupangOrder coupangOrder3 = mock(CoupangOrder.class);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(orderContext.isRunning()).thenReturn(true);
        when(coupangOrderFetcher.fetchPaymentCompleteOrders(anyString())).thenReturn(Arrays.asList(coupangOrder1, coupangOrder2, coupangOrder3));

        // when
        coupangOrderService.processNewOrders();

        // then
        verify(orderContext, times(1)).isRunning();
        verify(vendorConfig, times(1)).getVendorId();
        verify(coupangOrderFetcher, times(1)).fetchPaymentCompleteOrders(anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    @DisplayName("[결제완료] 상태 주문 조회 실패 시 - order context 예외")
    void processNewOrdersWithOrderContextException() throws CoupangApiException {
        // given
        when(orderContext.isRunning()).thenReturn(false);

        // when
        coupangOrderService.processNewOrders();

        // then
        verify(orderContext, times(1)).isRunning();
        verify(vendorConfig, never()).getVendorId();
        verify(coupangOrderFetcher, never()).fetchPaymentCompleteOrders(anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    @DisplayName("[결제완료] 상태 주문 조회 실패 시 - fetch 3회 연속 실패 예외")
    void processNewOrdersWithCoupangApiException() throws CoupangApiException {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(orderContext.isRunning()).thenReturn(true);
        when(coupangOrderFetcher.fetchPaymentCompleteOrders(anyString())).thenThrow(CoupangApiException.class);

        // when
        coupangOrderService.processNewOrders();

        // then
        verify(orderContext, times(1)).isRunning();
        verify(vendorConfig, times(1)).getVendorId();
        verify(coupangOrderFetcher, times(1)).fetchPaymentCompleteOrders(anyString());
        verify(slackMessageUtil, times(1)).sendError(startsWith("쿠팡 [결제완료]"));
    }

    @Test
    @DisplayName("[쿠팡 주문 검증] 성공 시")
    void validateCoupangOrderSuccessfully() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        when(hwasProduct.getSourceId()).thenReturn(ASIN);
        when(amazonProduct.getQuantity()).thenReturn("in_stock");

        when(hwasProductService.getHwasProductByDestinationId(anyString())).thenReturn(hwasProduct);
        when(amazonProductValidator.fetchValidAmazonProductByAsin(anyString())).thenReturn(amazonProduct);
        when(amazonProductValidator.validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong()))
                .thenReturn(ValidationStatus.COMPLETE);

        // when
        coupangOrderService.validateCoupangOrder(coupangOrder);

        // then
        verify(amazonProductValidator, times(1)).fetchValidAmazonProductByAsin(anyString());
        verify(amazonProductValidator, times(1))
                .validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong());
        verify(hwasProductService, times(1)).getHwasProductByDestinationId(anyString());
        verify(orderSheetService, never()).saveOrderFrom(any(), anyString(), any(), anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
        verify(orderContext, never()).updateRunningStatus(any());
    }

    @Test
    @DisplayName("[쿠팡 주문 검증] 실패 시 - Rainforest NotEnoughCredit 예외")
    void validateCoupangOrderWithNotEnoughCreditException() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        when(hwasProduct.getSourceId()).thenReturn(ASIN);

        when(hwasProductService.getHwasProductByDestinationId(anyString())).thenReturn(hwasProduct);
        when(amazonProductValidator.fetchValidAmazonProductByAsin(anyString()))
                .thenThrow(NotEnoughApiCreditException.class);

        // when
        coupangOrderService.validateCoupangOrder(coupangOrder);

        // then
        verify(amazonProductValidator, times(1)).fetchValidAmazonProductByAsin(anyString());
        verify(amazonProductValidator, never())
                .validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong());
        verify(hwasProductService, times(1)).getHwasProductByDestinationId(anyString());
        verify(orderSheetService, never()).saveOrderFrom(any(), anyString(), any(), anyString());
        verify(slackMessageUtil, times(1)).sendError(eq("Rainforest API 호출 크레딧이 부족합니다."));
        verify(orderContext, times(1)).updateRunningStatus(eq(RunnerStatus.STOP));
    }

    @Test
    @DisplayName("[쿠팡 주문 검증] 실패 시 - Rainforest InvalidAsin 예외")
    void validateCoupangOrderWithInvalidAsinException() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        when(hwasProduct.getSourceId()).thenReturn(ASIN);

        when(hwasProductService.getHwasProductByDestinationId(anyString())).thenReturn(hwasProduct);
        when(amazonProductValidator.fetchValidAmazonProductByAsin(anyString()))
                .thenThrow(InvalidAsinException.class);

        // when
        coupangOrderService.validateCoupangOrder(coupangOrder);

        // then
        verify(amazonProductValidator, times(1)).fetchValidAmazonProductByAsin(anyString());
        verify(amazonProductValidator, never())
                .validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong());
        verify(hwasProductService, times(2)).getHwasProductByDestinationId(anyString());
        verify(slackMessageUtil, times(1)).sendError(startsWith("Rainforest API 호출에서 존재하지 않는"));
        verify(orderContext, never()).updateRunningStatus(eq(RunnerStatus.RUNNING));
    }

    @Test
    @DisplayName("[쿠팡 주문 검증] 실패 시 - amazonProductValidator 예외")
    void validateCoupangOrderWithAmazonProductValidatorException() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        when(hwasProduct.getSourceId()).thenReturn(ASIN);

        when(hwasProductService.getHwasProductByDestinationId(anyString())).thenReturn(hwasProduct);
        when(amazonProductValidator.fetchValidAmazonProductByAsin(anyString())).thenReturn(null);

        // when
        coupangOrderService.validateCoupangOrder(coupangOrder);

        // then
        verify(amazonProductValidator, times(1)).fetchValidAmazonProductByAsin(anyString());
        verify(amazonProductValidator, never())
                .validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong());
        verify(hwasProductService, times(1)).getHwasProductByDestinationId(anyString());
        verify(orderSheetService, never()).saveOrderFrom(any(), anyString(), any(), anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[쿠팡 주문 검증] 실패 시 - amazonProduct 수량 예외")
    void validateCoupangOrderWithAmazonProductQuantityException() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        when(hwasProduct.getSourceId()).thenReturn(ASIN);
        when(amazonProduct.getQuantity()).thenReturn("out_of_stock.");

        when(hwasProductService.getHwasProductByDestinationId(anyString())).thenReturn(hwasProduct);
        when(amazonProductValidator.fetchValidAmazonProductByAsin(anyString())).thenReturn(amazonProduct);
        when(amazonProductValidator.validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong()))
                .thenReturn(ValidationStatus.ERR_QUANTITY);

        // when
        coupangOrderService.validateCoupangOrder(coupangOrder);

        // then
        verify(amazonProductValidator, times(1)).fetchValidAmazonProductByAsin(anyString());
        verify(amazonProductValidator, times(1))
                .validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong());
        verify(hwasProductService, times(1)).getHwasProductByDestinationId(anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[쿠팡 주문 검증] 실패 시 - amazonProduct 가격 예외")
    void validateCoupangOrderWithAmazonProductPriceException() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        when(hwasProduct.getSourceId()).thenReturn(ASIN);
        when(amazonProduct.getQuantity()).thenReturn("in_stock.");

        when(hwasProductService.getHwasProductByDestinationId(anyString())).thenReturn(hwasProduct);
        when(amazonProductValidator.fetchValidAmazonProductByAsin(anyString())).thenReturn(amazonProduct);
        when(amazonProductValidator.validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong()))
                .thenReturn(ValidationStatus.ERR_PRICE);

        // when
        coupangOrderService.validateCoupangOrder(coupangOrder);

        // then
        verify(amazonProductValidator, times(1)).fetchValidAmazonProductByAsin(anyString());
        verify(amazonProductValidator, times(1))
                .validateAmazonProduct(anyString(), anyDouble(), anyDouble(), anyString(), anyLong());
        verify(hwasProductService, times(1)).getHwasProductByDestinationId(anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[상품준비중] 상태로 변경 성공 시")
    void prepareShipmentSuccessfully() throws CoupangApiException {
        // given
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getOrderId()).thenReturn(ORDER_ID);
        when(amazonProduct.getAsin()).thenReturn(ASIN);
        when(coupangOrder.getPaidAt()).thenReturn(PAID_AT);

        when(coupangShippingService.updateStatusToPrepareShipment(anyString(), anyLong(), anyLong())).thenReturn(response);
        when(orderSheetService.saveOrderFrom(any(), anyString(), eq(CurrentStatus.SOURCE_STATUS_TO_PREPARE), anyString())).thenReturn(order);
        when(amazonPurchaseService.purchaseOrder(anyString(), anyString(), anyDouble())).thenReturn("purchaseRequestId");

        // when
        coupangOrderService.prepareShipment(coupangOrder, hwasProduct, amazonProduct);

        // then
        verify(coupangShippingService, times(1)).updateStatusToPrepareShipment(anyString(), anyLong(), anyLong());
        verify(orderSheetService, times(1)).saveOrderFrom(any(), anyString(), eq(CurrentStatus.SOURCE_STATUS_TO_PREPARE), anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
        verify(orderSheetService, never()).updateStatus(eq(order), eq(CurrentStatus.ERR_PURCHASE_AGENCY_REQUEST));
        verify(orderSheetService, times(1)).updatePurchaseAgencyRequest(any(), any(), any(), eq("purchaseRequestId"));
        verify(coupangOrderFetcher, never()).isStatusPaymentComplete(anyString(), anyLong());
    }

    @Test
    @DisplayName("[상품준비중] 상태로 변경 실패 시 - zinc 주문 예외")
    void prepareShipmentWithZincOrderException() throws CoupangApiException {
        // given
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getOrderId()).thenReturn(ORDER_ID);
        when(amazonProduct.getAsin()).thenReturn(ASIN);
        when(coupangOrder.getPaidAt()).thenReturn(PAID_AT);

        when(coupangShippingService.updateStatusToPrepareShipment(anyString(), anyLong(), anyLong())).thenReturn(response);
        when(orderSheetService.saveOrderFrom(any(), anyString(), eq(CurrentStatus.SOURCE_STATUS_TO_PREPARE), anyString())).thenReturn(order);
        when(amazonPurchaseService.purchaseOrder(anyString(), anyString(), anyDouble())).thenReturn(null);

        // when
        coupangOrderService.prepareShipment(coupangOrder, hwasProduct, amazonProduct);

        // then
        verify(coupangShippingService, times(1)).updateStatusToPrepareShipment(anyString(), anyLong(), anyLong());
        verify(orderSheetService, times(1)).saveOrderFrom(any(), anyString(), eq(CurrentStatus.SOURCE_STATUS_TO_PREPARE), anyString());
        verify(slackMessageUtil, times(1)).sendError(startsWith("Zinc 상품 구매 요청에 실패했습니다."));
        verify(orderSheetService, times(1)).updateStatus(eq(order), eq(CurrentStatus.ERR_PURCHASE_AGENCY_REQUEST));
        verify(orderSheetService, never()).updatePurchaseAgencyRequest(any(), any(), any(), anyString());
        verify(coupangOrderFetcher, never()).isStatusPaymentComplete(anyString(), anyLong());
    }

    @Test
    @DisplayName("[상품준비중] 상태로 변경 실패 시 - 주문 상태 변경 불가 예외")
    void prepareShipmentWithUnableToChangeStatusException() throws CoupangApiException {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(coupangShippingService.updateStatusToPrepareShipment(anyString(), anyLong(), anyLong()))
                .thenThrow(CoupangApiException.class);
        when(coupangOrderFetcher.isStatusPaymentComplete(anyString(), anyLong())).thenReturn(false);

        // when
        coupangOrderService.prepareShipment(coupangOrder, hwasProduct, amazonProduct);

        // then
        verify(coupangShippingService, times(1)).updateStatusToPrepareShipment(anyString(), anyLong(), anyLong());
        verify(orderSheetService, never()).saveOrderFrom(any(), anyString(), eq(CurrentStatus.SOURCE_STATUS_TO_PREPARE), anyString());
        verify(slackMessageUtil, times(1)).sendError(startsWith("결제완료 -> 상품준비중 처리 과정 중"));
        verify(orderSheetService, never()).updatePurchaseAgencyRequest(any(), any(), any(), eq("purchaseRequestId"));
        verify(coupangOrderFetcher, times(1)).isStatusPaymentComplete(anyString(), anyLong());
    }

    @Test
    @DisplayName("[상품준비중] 상태로 변경 실패 시 - 주문 상태 변경 예외")
    void prepareShipmentWithChangingStatusException() throws CoupangApiException {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(coupangShippingService.updateStatusToPrepareShipment(anyString(), anyLong(), anyLong()))
                .thenThrow(CoupangApiException.class);
        when(coupangOrderFetcher.isStatusPaymentComplete(anyString(), anyLong())).thenReturn(true);

        // when
        coupangOrderService.prepareShipment(coupangOrder, hwasProduct, amazonProduct);

        // then
        verify(coupangShippingService, times(1)).updateStatusToPrepareShipment(anyString(), anyLong(), anyLong());
        verify(orderSheetService, never()).saveOrderFrom(any(), anyString(), eq(CurrentStatus.SOURCE_STATUS_TO_PREPARE), anyString());
        verify(slackMessageUtil, times(1)).sendError(startsWith("결제완료 -> 상품준비중 처리에"));
        verify(orderSheetService, never()).updatePurchaseAgencyRequest(any(), any(), any(), eq("purchaseRequestId"));
        verify(coupangOrderFetcher, times(1)).isStatusPaymentComplete(anyString(), anyLong());
    }

    @Test
    @DisplayName("[배송 대행 요청] 성공 시")
    void requestDeliverAgencySuccessfully() throws OhmyzipApiException {
        // given
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getOrderId()).thenReturn(ORDER_ID);
        stubOhmyzipResponse();

        when(coupangCancelService.getRefundItemByOrderId(anyString(), anyLong())).thenReturn(null);
        when(hwasProductService.getHwasProductByDestinationId(anyString())).thenReturn(hwasProduct);
        when(coupangOrderFetcher.fetchOrderByOrderId(anyString(), anyLong())).thenReturn(coupangOrder);
        when(ohmyzipService.requestDeliverAgency(any(), anyString())).thenReturn(response);

        // when
        coupangOrderService.requestDeliverAgency(order, zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendError(anyString());
        verify(orderSheetService, times(1))
                .updateDeliveryAgencyResponse(any(), any(), eq(DeliveryAgency.OH_MY_ZIP), any());
        verify(orderSheetService, times(1)).updateStatus(any(), eq(CurrentStatus.ERR_SOURCE_INVOICE_UPDATE));
    }

    private void stubOhmyzipResponse() {
        CoupangOrder.Receiver receiver = mock(CoupangOrder.Receiver.class);
        CoupangOrder.OverseaShippingInfo overseaShippingInfo = mock(CoupangOrder.OverseaShippingInfo.class);

        when(coupangOrder.getReceiver()).thenReturn(receiver);
        when(coupangOrder.getOverseaShippingInfo()).thenReturn(overseaShippingInfo);

        when(receiver.getName()).thenReturn("Name");
        when(overseaShippingInfo.getPersonalCustomsClearanceCode()).thenReturn("P1234");
        when(receiver.getAddress1()).thenReturn("Texas");
        when(receiver.getPostCode()).thenReturn("79912");

        when(hwasProduct.getName()).thenReturn("Macbook Pro");
        when(hwasProduct.getSourceOriginalPrice()).thenReturn(3000D);

        when(zincResponse.getMerchantOrderId()).thenReturn("81298472");
    }

    @Test
    @DisplayName("[배송 대행 요청] 실패 시 - Ohmyzip 예외")
    void requestDeliverAgencyWithOhmyzipException() throws OhmyzipApiException {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getOrderId()).thenReturn(ORDER_ID);
        stubOhmyzipResponse();

        when(coupangCancelService.getRefundItemByOrderId(anyString(), anyLong())).thenReturn(null);
        when(coupangOrderFetcher.fetchOrderByOrderId(anyString(), anyLong())).thenReturn(coupangOrder);
        when(hwasProductService.getHwasProductByDestinationId(anyString())).thenReturn(hwasProduct);
        when(ohmyzipService.requestDeliverAgency(any(), anyString())).thenReturn(null);

        // when
        coupangOrderService.requestDeliverAgency(order, zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendError(startsWith("배대지 신청 오류."));
        verify(orderSheetService, never())
                .updateDeliveryAgencyResponse(any(), any(), eq(DeliveryAgency.OH_MY_ZIP), any());
        verify(orderSheetService, never()).updateStatus(any(), eq(CurrentStatus.ERR_SOURCE_INVOICE_UPDATE));
        verify(orderSheetService, never()).saveCancelFrom(any(), anyString());
        verify(orderSheetService, never()).updatePurchaseAgencyCancel(any(), any(), any(), eq(CANCEL_ID));
    }

    @Test
    @DisplayName("[배송 대행 요청] 실패 시 - 취소/환불 요청 예외")
    void requestDeliverAgencyWithRefundException() {
        // given
        CancelledOrder cancelledOrder = mock(CancelledOrder.class);
        RefundOrder.Item refundItem = mock(RefundOrder.Item.class);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getOrderId()).thenReturn(ORDER_ID);
        when(refundItem.getCancelId()).thenReturn(12345L);
        when(zincResponse.getRequestId()).thenReturn(REQUEST_ID);
        when(zincResponse.getMerchantOrderId()).thenReturn("12358");


        when(coupangCancelService.getRefundItemByOrderId(anyString(), anyLong())).thenReturn(refundItem);
        when(amazonPurchaseService.cancelOrder(anyString(), anyString())).thenReturn(CANCEL_ID);
        when(orderSheetService.saveCancelFrom(any(), anyString())).thenReturn(cancelledOrder);

        // when
        coupangOrderService.requestDeliverAgency(order, zincResponse);

        // then
        verify(slackMessageUtil, never()).sendError(anyString());
        verify(orderSheetService, never())
                .updateDeliveryAgencyResponse(any(), any(), eq(DeliveryAgency.OH_MY_ZIP), any());
        verify(orderSheetService, never()).updateStatus(any(), eq(CurrentStatus.ERR_SOURCE_INVOICE_UPDATE));
        verify(orderSheetService, times(1)).saveCancelFrom(any(), anyString());
        verify(orderSheetService, times(1)).updatePurchaseAgencyCancel(any(), any(), any(), eq(CANCEL_ID));
    }

    @Test
    @DisplayName("[배송 대행 요청] 실패 시 - 취소/환불 요청 후 Zinc 취소 예외")
    void requestDeliverAgencyWithZincCancelException() {
        // given
        CancelledOrder cancelledOrder = mock(CancelledOrder.class);
        RefundOrder.Item refundItem = mock(RefundOrder.Item.class);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getOrderId()).thenReturn(ORDER_ID);
        when(refundItem.getCancelId()).thenReturn(12345L);
        when(zincResponse.getRequestId()).thenReturn(REQUEST_ID);
        when(zincResponse.getMerchantOrderId()).thenReturn("12358");


        when(coupangCancelService.getRefundItemByOrderId(anyString(), anyLong())).thenReturn(refundItem);
        when(amazonPurchaseService.cancelOrder(anyString(), anyString())).thenReturn(null);
        when(orderSheetService.saveCancelFrom(any(), anyString())).thenReturn(cancelledOrder);

        // when
        coupangOrderService.requestDeliverAgency(order, zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendError(startsWith("Zinc 상품 취소에 실패했습니다."));
        verify(orderSheetService, never())
                .updateDeliveryAgencyResponse(any(), any(), eq(DeliveryAgency.OH_MY_ZIP), any());
        verify(orderSheetService, never()).updateStatus(any(), eq(CurrentStatus.ERR_SOURCE_INVOICE_UPDATE));
        verify(orderSheetService, times(1)).saveCancelFrom(any(), anyString());
        verify(orderSheetService, never()).updatePurchaseAgencyCancel(any(), any(), any(), eq(CANCEL_ID));
    }

    @Test
    @DisplayName("[쿠팡 송장 업테이트] 성공 시")
    void updateInvoiceSuccessfully() throws CoupangApiException {
        // given
        PrepareShipmentDto.Response invoiceUpdateResponse = mock(PrepareShipmentDto.Response.class);

        when(order.getDomesticTrackingNumber()).thenReturn("3028492524");
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        CoupangOrder.Orderer orderer = mock(CoupangOrder.Orderer.class);
        when(coupangOrder.getOrderer()).thenReturn(orderer);
        when(orderer.getName()).thenReturn("강상웅");

        when(coupangShippingService.updateInvoice(eq(VENDOR_ID), any(), eq("3028492524"))).thenReturn(invoiceUpdateResponse);

        // when
        coupangOrderService.updateInvoice(order, coupangOrder);

        // then
        verify(orderSheetService, times(1)).updateStatus(eq(order), eq(CurrentStatus.SOURCE_INVOICE_UPDATE));
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    @DisplayName("[쿠팡 송장 업테이트] 실패 시 - Response null 예외")
    void updateInvoiceWithResponseNullException() throws CoupangApiException {
        // given
        when(order.getDomesticTrackingNumber()).thenReturn("3028492524");
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(coupangShippingService.updateInvoice(eq(VENDOR_ID), any(), eq("3028492524"))).thenReturn(null);

        // when
        coupangOrderService.updateInvoice(order, coupangOrder);

        // then
        verify(orderSheetService, times(1)).updateStatus(eq(order), eq(CurrentStatus.ERR_SOURCE_INVOICE_UPDATE));
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[쿠팡 송장 업테이트] 실패 시 - 송장 번호 예외")
    void updateInvoiceWithTrackingNumberException() throws CoupangApiException {
        // given
        when(order.getDomesticTrackingNumber()).thenReturn("3028492524");
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(coupangShippingService.updateInvoice(eq(VENDOR_ID), any(), eq("3028492524")))
                .thenThrow(CoupangApiException.class);

        // when
        coupangOrderService.updateInvoice(order, coupangOrder);

        // then
        verify(orderSheetService, times(1)).updateStatus(eq(order), eq(CurrentStatus.ERR_SOURCE_INVOICE_UPDATE));
        verify(slackMessageUtil, times(1)).sendError(startsWith("쿠팡 송장 업로드 중 송장번호 이상으로"));
    }

    @Test
    @DisplayName("[Purchase Agency 송장 Webhook 수신] 성공 시")
    void updatePurchaseAgencyInvoiceWebhookSuccessfully() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        when(zincResponse.getRequestId()).thenReturn(REQUEST_ID);
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getOrderId()).thenReturn(ORDER_ID);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.SOURCE_INVOICE_UPDATE);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(order);
        when(coupangCancelService.getRefundItemByOrderId(anyString(), anyLong())).thenReturn(null);

        // when
        coupangOrderService.updatePurchaseAgencyInvoiceWebhook(zincResponse);

        // then
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    @DisplayName("[Purchase Agency 송장 Webhook 수신] 실패 시 - Order 부재 예외")
    void updatePurchaseAgencyInvoiceWebhookWithOrderNotExistException() {
        // given
        when(zincResponse.getRequestId()).thenReturn(REQUEST_ID);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(eq(REQUEST_ID))).thenReturn(null);

        // when
        coupangOrderService.updatePurchaseAgencyInvoiceWebhook(zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendError(anyString());
        verify(orderSheetService, never()).updateStatus(any(), any());
    }

    @Test
    @DisplayName("[Purchase Agency 요청 성공 Webhook 수신] 성공 시 - OrderRequest")
    void handleZincSucceededOrderRequestSuccessfully() {
        // given
        when(zincResponse.getRequestId()).thenReturn(REQUEST_ID);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.PURCHASE_AGENCY_REQUEST);
        when(order.getOrderId()).thenReturn(ORDER_ID);
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(order);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(null);

        // when
        coupangOrderService.handleZincSucceededRequest(zincResponse);

        // then
        verify(coupangCancelService, never()).getRefundItemByOrderId(anyString(), anyLong());
    }

    @Test
    @DisplayName("[Purchase Agency 요청 성공 Webhook 수신] 성공 시 - CancelRequest")
    void handleZincSucceededCancelRequestSuccessfully() {
        // given
        CancelledOrder cancelledOrder = mock(CancelledOrder.class);

        when(zincResponse.getRequestId()).thenReturn(CANCEL_ID);
        when(cancelledOrder.getOrder()).thenReturn(order);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.PURCHASE_AGENCY_CANCEL_REQUEST);
        when(cancelledOrder.getCancelId()).thenReturn(CANCEL_ID);
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(null);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(cancelledOrder);
        when(coupangCancelService.canStopRelease(anyString(), anyLong())).thenReturn(true);

        // when
        coupangOrderService.handleZincSucceededRequest(zincResponse);

        // then
        verify(slackMessageUtil, times(2)).sendOrder(anyString());
        verify(orderSheetService, times(1)).updateCancelDate(any());
    }

    @Test
    @DisplayName("[Purchase Agency 요청 성공 Webhook 수신] 실패 시 - 쿠팡 출고 중지 예외")
    void handleZincSucceededCancelRequestWithCoupangStopReleaseException() {
        // given
        CancelledOrder cancelledOrder = mock(CancelledOrder.class);

        when(zincResponse.getRequestId()).thenReturn(CANCEL_ID);
        when(cancelledOrder.getOrder()).thenReturn(order);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.PURCHASE_AGENCY_CANCEL_REQUEST);
        when(cancelledOrder.getCancelId()).thenReturn(CANCEL_ID);
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getOrderId()).thenReturn(ORDER_ID);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(null);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(cancelledOrder);
        when(coupangCancelService.canStopRelease(anyString(), anyLong())).thenReturn(false);

        // when
        coupangOrderService.handleZincSucceededRequest(zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendOrder(anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[Purchase Agency 요청 성공 Webhook 수신] 실패 시 - Order 부재 예외")
    void handleZincSucceededRequestWithOrderNotExistException() {
        // given
        when(zincResponse.getRequestId()).thenReturn(REQUEST_ID);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(null);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(null);

        // when
        coupangOrderService.handleZincSucceededRequest(zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[Purchase Agency 요청 실패 Webhook 수신] 성공 시 - OrderRequest")
    void handleZincFailedOrderRequestSuccessfully() throws CreditCardDeclinedException, PaymentVerificationException, PurchaseAgencyException, ZincApiException {
        // given
        when(zincResponse.getRequestId()).thenReturn(REQUEST_ID);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.PURCHASE_AGENCY_REQUEST);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(order);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(null);

        // when
        coupangOrderService.handleZincFailedRequest(zincResponse);

        // then
        verify(amazonPurchaseService, times(1)).getOrderStatus(anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    @DisplayName("[Purchase Agency 요청 실패 Webhook 수신] 성공 시 - CancelRequest")
    void handleZincFailedCancelRequestSuccessfully() throws CancellationException {
        // given
        CancelledOrder cancelledOrder = mock(CancelledOrder.class);

        when(zincResponse.getRequestId()).thenReturn(CANCEL_ID);
        when(cancelledOrder.getOrder()).thenReturn(order);
        when(order.getPurchaseAgencyRequestId()).thenReturn(REQUEST_ID);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.PURCHASE_AGENCY_CANCEL_REQUEST);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(null);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(cancelledOrder);

        // when
        coupangOrderService.handleZincFailedRequest(zincResponse);

        // then
        verify(amazonPurchaseService, times(1)).getCancelStatus(anyString(), anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[Purchase Agency 요청 실패 Webhook 수신] 실패 시 - Order 부재 예외")
    void handleZincFailedRequestWithOrderNotExistException() {
        // given
        when(zincResponse.getRequestId()).thenReturn(CANCEL_ID);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(null);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(null);

        // when
        coupangOrderService.handleZincFailedRequest(zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[Purchase Agency 요청 실패 Webhook 수신] 실패 시 - CreditCardDeclined 예외")
    void handleZincFailedRequestWithCreditCardDeclinedException() throws CreditCardDeclinedException, PaymentVerificationException, PurchaseAgencyException, ZincApiException {
        // given
        when(zincResponse.getRequestId()).thenReturn(CANCEL_ID);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.PURCHASE_AGENCY_REQUEST);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(order);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(null);
        when(amazonPurchaseService.getOrderStatus(anyString()))
                .thenThrow(CreditCardDeclinedException.class);

        // when
        coupangOrderService.handleZincFailedRequest(zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendError(anyString());
        verify(orderSheetService, times(1)).updateStatus(any(), any());
        verify(orderContext, times(1)).updateRunningStatus(eq(RunnerStatus.STOP));
    }

    @Test
    @DisplayName("[Purchase Agency 요청 실패 Webhook 수신] 실패 시 - Zinc Cancellation 예외")
    void handleZincFailedRequestWithCancellationException() throws CancellationException {
        // given
        CancelledOrder cancelledOrder = mock(CancelledOrder.class);

        when(zincResponse.getRequestId()).thenReturn(CANCEL_ID);
        when(cancelledOrder.getOrder()).thenReturn(order);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.PURCHASE_AGENCY_CANCEL_REQUEST);
        when(order.getPurchaseAgencyRequestId()).thenReturn(REQUEST_ID);

        when(orderSheetService.getOrderByPurchaseAgencyRequestId(anyString())).thenReturn(null);
        when(orderSheetService.getCancelledOrderByPurchaseAgencyCancelId(anyString())).thenReturn(cancelledOrder);
        when(amazonPurchaseService.getCancelStatus(anyString(), anyString()))
                .thenThrow(CancellationException.class);

        // when
        coupangOrderService.handleZincFailedRequest(zincResponse);

        // then
        verify(slackMessageUtil, times(1)).sendError(anyString());
        verify(orderSheetService, never()).updateStatus(any(), any());
    }

    @Test
    @DisplayName("[송장 정보 조회] 성공 시")
    void getTrackingNumberSuccessfully() {
        // given

        // when
        coupangOrderService.getTrackingNumber(ORDER_ID);

        // then
        verify(orderSheetService, times(1)).getTrackingNumber(any());
    }

    @Test
    @DisplayName("[배대지 송장 조회] 성공 시")
    void checkDeliveryAgencyShippingSuccessfully() {
        // given
        when(orderSheetService.getOrderListByCurrentStatusIn(anyList())).thenReturn(List.of(order, order));

        // when
        coupangOrderService.checkDeliveryAgencyShipping();

        // then
        // 로그 확인 (스레드풀 작동)
    }

    @Test
    @DisplayName("[Running Status 변경] 성공 시")
    void updateRunningStatusSuccessfully() {
        // given

        // when
        coupangOrderService.updateRunningStatus(RunnerStatus.STOP);

        // then
        verify(orderContext, times(1)).updateRunningStatus(any());
    }

    @Test
    @DisplayName("[Zinc succeeded-order Webhook 수신] 성공 시")
    void updateZincSucceededRequestSuccessfully() {
        // given

        // when
        coupangOrderService.updateZincSucceededRequest(zincResponse);

        // then
        // 로그 확인 (스레드풀 작동)
    }

    @Test
    @DisplayName("[Zinc failed-order Webhook 수신] 성공 시")
    void updateZincFailedRequestSuccessfully() {
        // given

        // when
        coupangOrderService.updateZincFailedRequest(zincResponse);

        // then
        // 로그 확인 (스레드풀 작동)
    }

    @Test
    @DisplayName("[Purchase Agency 송장 업데이트] 성공 시")
    void updateZincTrackingNumberSuccessfully() {
        // given

        // when
        coupangOrderService.updateZincTrackingNumber(zincResponse);

        // then
        // 로그 확인 (스레드풀 작동)
    }

    @Test
    @DisplayName("[Source에 주문 취소 요청] 성공 시")
    void cancelSourceOrderSuccessfully() {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(coupangOrder.getOrderId()).thenReturn(12345L);
        when(coupangOrder.getVendorItemId()).thenReturn(54321L);
        when(vendorConfig.getVendorUserId()).thenReturn("9ea93x");

        when(coupangCancelService.cancelOrder(any(), anyLong(), anyString(), anyString())).thenReturn(CANCEL_ID);

        // when
        coupangOrderService.cancelSourceOrder(coupangOrder, hwasProduct, CurrentStatus.CANCEL_COMPLETE_INVALID_PRICE);

        // then
        verify(slackMessageUtil, times(1)).sendOrder(anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    @DisplayName("[Source에 주문 취소 요청] 실패 시 - 쿠팡 주문 조회 예외")
    void cancelSourceOrderWithCoupangFetchException() {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);


        // when
        coupangOrderService.cancelSourceOrder(coupangOrder, hwasProduct, CurrentStatus.CANCEL_COMPLETE_INVALID_PRICE);

        // then
        verify(slackMessageUtil, never()).sendOrder(anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[Source에 주문 취소 요청] 실패 시 - 쿠팡 주문 취소 예외")
    void cancelSourceOrderWithCoupangCancelException() {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(coupangOrder.getOrderId()).thenReturn(12345L);
        when(coupangOrder.getVendorItemId()).thenReturn(54321L);
        when(vendorConfig.getVendorUserId()).thenReturn("9ea93x");

        when(coupangCancelService.cancelOrder(any(), anyLong(), anyString(), anyString())).thenReturn(null);

        // when
        coupangOrderService.cancelSourceOrder(coupangOrder, hwasProduct, CurrentStatus.CANCEL_COMPLETE_INVALID_PRICE);

        // then
        verify(slackMessageUtil, never()).sendOrder(anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    @DisplayName("[배대지 송장 업데이트] 성공 시")
    void updateDeliveryAgencyShippingSuccessfully() {
        // given
        when(order.getOrderId()).thenReturn(ORDER_ID);
        when(order.getDeliveryAgencyRequestId()).thenReturn(REQUEST_ID);
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.DELIVERY_AGENCY_SHIPPING_START);

        when(coupangCancelService.getRefundItemByOrderId(anyString(), any())).thenReturn(null);
        when(ohmyzipService.getShippingStatus(anyString(), anyString())).thenReturn("관부가세 결제 대기");

        // when
        coupangOrderService.updateDeliveryAgencyShipping(order);

        // then
        verify(orderSheetService, times(2)).updateStatus(any(), any());
    }

    @Test
    @DisplayName("[배대지 송장 업데이트] 실패 시 - CurrentStatus 예외")
    void updateDeliveryAgencyShippingWithCurrentStatusException() {
        // given
        when(order.getOrderId()).thenReturn(ORDER_ID);
        when(order.getDeliveryAgencyRequestId()).thenReturn(REQUEST_ID);
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(order.getCurrentStatus()).thenReturn(CurrentStatus.DELIVERY_AGENCY_SHIPPING_START);

        when(coupangCancelService.getRefundItemByOrderId(anyString(), any())).thenReturn(null);
        when(ohmyzipService.getShippingStatus(anyString(), anyString())).thenReturn("국제 배송");

        // when
        coupangOrderService.updateDeliveryAgencyShipping(order);

        // then
        verify(orderSheetService, never()).updateStatus(any(), any());
    }

    @Test
    @DisplayName("[중단된 주문 재 시도] 성공 시")
    void restartErrorOrdersSuccessfully() {
        // given

        when(orderSheetService.getOrderListByCurrentStatusIn(anyList())).thenReturn(List.of(order, order));

        // when
        coupangOrderService.restartErrorOrders();

        // then
        // 로그 확인 (스레드 풀 작동)
    }

}