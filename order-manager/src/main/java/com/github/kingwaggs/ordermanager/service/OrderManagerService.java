package com.github.kingwaggs.ordermanager.service;

import com.github.kingwaggs.ordermanager.domain.RunnerStatus;
import com.github.kingwaggs.ordermanager.domain.VendorOrder;
import com.github.kingwaggs.ordermanager.domain.dto.OrderQuery;
import com.github.kingwaggs.ordermanager.domain.dto.SourceVendor;
import com.github.kingwaggs.ordermanager.domain.dto.TrackingDto;
import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import com.github.kingwaggs.ordermanager.service.coupang.CoupangOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderManagerService {

    private static final String RUNTIME_EXCEPTION_MESSAGE = "Unreachable statement";

    private final OrderSheetService orderSheetService;
    private final CoupangOrderService coupangOrderService;

    public void processNewOrders() {
        coupangOrderService.processNewOrders();
    }

    public void checkDeliveryAgencyShipping() {
        coupangOrderService.checkDeliveryAgencyShipping();
    }

    public void restartErrorOrders() {
        coupangOrderService.restartErrorOrders();
    }

    public Map<CurrentStatus, Integer> getOrderStatusCountMap() {
        return orderSheetService.getOrderStatusCountMap();
    }

    public TrackingDto getTrackingNumber(SourceVendor sourceVendor, String orderId) {
        switch (sourceVendor) {
            case COUPANG:
                return coupangOrderService.getTrackingNumber(orderId);
            default:
                throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE);
        }
    }

    public VendorOrder getVendorOrder(SourceVendor vendor, String orderId) {
        switch (vendor) {
            case COUPANG:
                return coupangOrderService.getCoupangOrder(orderId);
            default:
                throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE);
        }
    }

    public Page<Order> getOrderListBy(OrderQuery orderQuery, Pageable pageable) {
        return orderSheetService.findOrderBy(orderQuery, pageable);
    }

    public void updateRunningStatus(SourceVendor sourceVendor, RunnerStatus status) {
        switch (sourceVendor) {
            case COUPANG:
                coupangOrderService.updateRunningStatus(status);
                break;
            default:
                throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE);
        }
    }

    public void isRunning(SourceVendor sourceVendor) {
        switch (sourceVendor) {
            case COUPANG:
                coupangOrderService.checkRunningStatus();
                break;
            default:
                throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE);
        }
    }

    public boolean syncPurchaseAgencyOrders(LocalDateTime startTime, LocalDateTime endTime) {
        return coupangOrderService.syncPurchaseAgencyOrders(startTime, endTime);
    }

    public boolean syncSourceOrders() {
        return coupangOrderService.syncSourceOrders();
    }
}
