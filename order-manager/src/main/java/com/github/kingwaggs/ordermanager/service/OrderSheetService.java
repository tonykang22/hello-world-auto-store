package com.github.kingwaggs.ordermanager.service;

import com.github.kingwaggs.ordermanager.domain.dto.OrderQuery;
import com.github.kingwaggs.ordermanager.domain.dto.TrackingDto;
import com.github.kingwaggs.ordermanager.domain.dto.response.OhmyzipResponse;
import com.github.kingwaggs.ordermanager.domain.dto.response.ZincResponse;
import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.ordermanager.domain.sheet.*;
import com.github.kingwaggs.ordermanager.repository.CancelledOrderRepository;
import com.github.kingwaggs.ordermanager.repository.OrderRepository;
import com.github.kingwaggs.ordermanager.util.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderSheetService {

    private final OrderRepository orderRepository;
    private final CancelledOrderRepository cancelledOrderRepository;

    public Order getOrderByPurchaseAgencyRequestId(String requestId) {
        log.info("Get order from orderRepository by Purchase Agency Request Id : {}", requestId);
        Optional<Order> orderOptional = orderRepository.findByPurchaseAgencyRequestId(requestId);
        if (orderOptional.isEmpty()) {
            log.error("There are no order_sheet for Purchase Agency Request Id : {}", requestId);
            return null;
        }
        return orderOptional.get();
    }

    public CancelledOrder getCancelledOrderByPurchaseAgencyCancelId(String requestId) {
        log.info("Get order from cancelledOrderRepository by Purchase Agency Cancel Id : {}", requestId);
        Optional<CancelledOrder> cancelledOrderOptional = cancelledOrderRepository.findByPurchaseAgencyCancelId(requestId);
        if (cancelledOrderOptional.isEmpty()) {
            log.error("There are no cancelled_order for Purchase Agency Cancel Id : {}", requestId);
            return null;
        }
        return cancelledOrderOptional.get();
    }

    public List<Order> getOrderListByCurrentStatusIn(List<CurrentStatus> currentStatusList) {
        log.info("Get order list from orderRepository by CurrentStatus IN {}", currentStatusList.toString());
        return orderRepository.findByCurrentStatusIn(currentStatusList);
    }

    public List<Order> getOrderListByCurrentStatus(CurrentStatus status) {
        log.info("Get order list from orderRepository by CurrentStatus : {}", status);
        return orderRepository.findByCurrentStatus(status);
    }

    public CancelledOrder getCancelByOrder(Order order) {
        log.info("Get cancel from cancelRepository by Order Id : {}", order.getOrderId());
        return cancelledOrderRepository.findByOrder(order);
    }

    public Order saveOrderFrom(HelloWorldAutoStoreProduct hwasProduct, String orderId, CurrentStatus currentStatus, String paidAt) {
        log.info("Save order on orderRepository. Source Order Id : {}", orderId);
        Order orderSheet = Order.createFrom(hwasProduct, orderId, currentStatus, paidAt);
        return orderRepository.save(orderSheet);
    }

    public CancelledOrder saveCancelFrom(Order order, String cancelId) {
        log.info("Save cancel on cancelRepository. Source Order Id : {}", order.getOrderId());
        if (cancelledOrderRepository.existsByCancelId(cancelId)) {
            log.info("Cancel entity is already created for cancel_id: {}", cancelId);
            return cancelledOrderRepository.findByCancelId(cancelId);
        }
        CancelledOrder cancelledOrder = CancelledOrder.createFrom(order, cancelId);
        return cancelledOrderRepository.save(cancelledOrder);
    }

    public void saveCancelFrom(Order order, String cancelId, LocalDateTime cancelDate) {
        log.info("Save cancel on cancelRepository. Source Order Id : {}", order.getOrderId());
        CancelledOrder cancelledOrder = CancelledOrder.createFrom(order, cancelId, cancelDate);
        cancelledOrderRepository.save(cancelledOrder);
    }

    public void updateStatus(Order order, CurrentStatus status) {
        log.info("Update order's CurrentStatus on orderRepository. Source Order Id : {}, CurrentStatus : {}", order.getOrderId(), status);
        order.setCurrentStatus(status);
        orderRepository.save(order);
    }

    public void updateDeliveryShippingStart(Order order, LocalDateTime startedAt) {
        log.info("Update delivery shipping start. Source Order Id: {}", order.getOrderId());
        order.setDeliveryAgencyShippingStart(startedAt);
        orderRepository.save(order);
    }

    public void updateDeliveryShippingComplete(Order order, LocalDateTime completedAt) {
        log.info("Update delivery shipping complete. Source Order Id: {}", order.getOrderId());
        order.setDeliveryAgencyShippingComplete(completedAt);
        orderRepository.save(order);
    }

    public void updatePurchaseAgencyRequest(Order order, CurrentStatus status, PurchaseAgency purchaseAgency, String requestId) {
        log.info("Update order's PurchaseAgency OrderRequest response on orderRepository. Source Order Id : {}, Purchase Agency Request Id : {}", order.getOrderId(), requestId);
        order.setCurrentStatus(status);
        order.setPurchaseAgency(purchaseAgency);
        order.setPurchaseAgencyRequestId(requestId);
        order.setPurchaseAgencyRequestStart(LocalDateTime.now());
        orderRepository.save(order);
    }

    public void updatePurchaseAgencyOrder(Order order, CurrentStatus status, ZincResponse zincResponse) {
        log.info("Update order's PurchaseAgency OrderCompleted response on orderRepository. Source Order Id : {}, Purchase Agency Request Id : {}", order.getOrderId(), zincResponse.getRequestId());
        String requestCompletedAt = zincResponse.getRequestCompletedAt();
        LocalDateTime completedAt = DateTimeConverter.zincDateTimeString2LocalDateTime(requestCompletedAt);

        order.setCurrentStatus(status);
        order.setPurchaseAgencyRequestComplete(completedAt);
        orderRepository.save(order);
    }

    public void updatePurchaseAgencyShipping(Order order, CurrentStatus status, ZincResponse zincResponse) {
        log.info("Update order's PurchaseAgency Shipping response on orderRepository. Source Order Id : {}, Purchase Agency Request Id : {}", order.getOrderId(), zincResponse.getRequestId());
        String courier = zincResponse.getCourier();
        String trackingNumber = zincResponse.getTrackingNumber();

        order.setCurrentStatus(status);
        order.setInternationalCourier(courier);
        order.setInternationalTrackingNumber(trackingNumber);
        orderRepository.save(order);
    }

    public void updateDeliveryAgencyResponse(Order order, CurrentStatus status, DeliveryAgency deliveryAgency, OhmyzipResponse ohmyzipResponse) {
        log.info("Update order's DeliveryAgency DeliveryRequest response on orderRepository. Source Order Id : {}, Delivery Agency Request Id : {}", order.getOrderId(), ohmyzipResponse.getRequestId());
        order.setCurrentStatus(status);
        order.setDeliveryAgency(deliveryAgency);
        order.setDeliveryAgencyRequestId(ohmyzipResponse.getRequestId());
        if (deliveryAgency == DeliveryAgency.OH_MY_ZIP) {
            order.setDomesticCourier(Courier.CJ);
        }
        order.setDomesticTrackingNumber(ohmyzipResponse.getInvoiceNumber());
        orderRepository.save(order);
    }

    public void updatePurchaseAgencyCancel(Order order, CurrentStatus status, CancelledOrder cancelledOrder, String cancelId) {
        log.info("Update order's PurchaseAgency CancelRequest response on orderRepository. Source Order Id : {}, Purchase Agency Cancel Id : {}", order.getOrderId(), cancelId);
        order.setCurrentStatus(status);
        orderRepository.save(order);

        cancelledOrder.setPurchaseAgencyCancelId(cancelId);
        cancelledOrderRepository.save(cancelledOrder);
    }

    public void updateCancelDate(CancelledOrder cancelledOrder) {
        log.info("Update cancel date. Cancelled Id : {}", cancelledOrder.getCancelId());
        cancelledOrder.setCancelDate(LocalDateTime.now());
        cancelledOrderRepository.save(cancelledOrder);
    }

    public void updateSourceRefund(Order order, CancelledOrder cancelledOrder) {
        log.info("Update order's Source RefundRequest response on orderRepository. Source Order Id : {}", order.getOrderId());
        order.setCurrentStatus(CurrentStatus.REFUND_COMPLETE);
        orderRepository.save(order);

        updateCancelDate(cancelledOrder);
    }

    public Map<CurrentStatus, Integer> getOrderStatusCountMap() {
        log.info("Get orders counts map.");
        Map<CurrentStatus, Integer> currentStatusCountMap = new EnumMap<>(CurrentStatus.class);
        List<CurrentStatus> currentStatusList = CurrentStatus.getCurrentStatusList();
        currentStatusList.forEach(cs -> currentStatusCountMap.putIfAbsent(cs, 0));

        List<Order> orderList = orderRepository.findAll();
        for (Order order : orderList) {
            CurrentStatus currentStatus = order.getCurrentStatus();
            Integer count = currentStatusCountMap.get(currentStatus);
            currentStatusCountMap.replace(currentStatus, count + 1);
        }
        return currentStatusCountMap;
    }

    public Page<Order> findOrderBy(OrderQuery orderQuery, Pageable pageable) {
        log.info("Find orders by various parameters. OrderQuery toString : {}", orderQuery.toString());

        String orderId = orderQuery.getOrderId();
        String purchaseAgencyId = orderQuery.getPurchaseAgencyId();
        CurrentStatus status = orderQuery.getStatus();
        LocalDateTime soldFrom = orderQuery.getSoldFrom();
        LocalDateTime soldTo = orderQuery.getSoldFrom();

        return orderRepository.findBy(orderId, purchaseAgencyId, status, soldFrom, soldTo, pageable);
    }

    public TrackingDto getTrackingNumber(String orderId) {
        log.info("Tracking information was requested. Source Order Id : {}", orderId);

        Optional<Order> orderOptional = orderRepository.findByOrderId(orderId);
        if (orderOptional.isEmpty()) {
            return null;
        }

        Order order = orderOptional.get();

        return TrackingDto.builder()
                .internationalCourier(order.getInternationalCourier())
                .internationalTrackingNumber(order.getInternationalTrackingNumber())
                .domesticCourier(String.valueOf(order.getDomesticCourier()))
                .domesticTrackingNumber(order.getDomesticTrackingNumber())
                .build();
    }

    public Order findOrderBy(String purchaseAgencyRequestId) {
        log.info("Find order by Purchase Agency Request Id : {}", purchaseAgencyRequestId);
        Optional<Order> orderOptional = orderRepository.findByPurchaseAgencyRequestId(purchaseAgencyRequestId);
        return orderOptional.orElse(null);
    }

    public Order findOrderByOrderId(String sourceRequestId) {
        log.info("Find order by Source Request Id : {}", sourceRequestId);
        Optional<Order> orderOptional = orderRepository.findByOrderId(sourceRequestId);
        return orderOptional.orElse(null);
    }

    public List<Order> findOrderBy(HelloWorldAutoStoreProduct hwasProduct, List<CurrentStatus> targetStatus) {
        log.info("Find orders by product_id : {}, current_status : {}", hwasProduct.getId(), targetStatus.toString());
        return orderRepository.findBy(hwasProduct, targetStatus);
    }

    public boolean existsByInternationalTracking(String trackingNumber) {
        log.info("Check if international tracking number is duplicated. international_tracking_number: {}", trackingNumber);
        boolean isExists = orderRepository.existsByInternationalTrackingNumber(trackingNumber);
        log.info("The tracking number already exists: {}. international_tracking_number: {}", isExists, trackingNumber);
        return isExists;
    }

}
