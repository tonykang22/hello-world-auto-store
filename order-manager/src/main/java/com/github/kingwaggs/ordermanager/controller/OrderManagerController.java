package com.github.kingwaggs.ordermanager.controller;

import com.github.kingwaggs.ordermanager.domain.RunnerStatus;
import com.github.kingwaggs.ordermanager.domain.VendorOrder;
import com.github.kingwaggs.ordermanager.domain.dto.OrderQuery;
import com.github.kingwaggs.ordermanager.domain.dto.SourceVendor;
import com.github.kingwaggs.ordermanager.domain.dto.TrackingDto;
import com.github.kingwaggs.ordermanager.domain.dto.response.CommonResponse;
import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import com.github.kingwaggs.ordermanager.service.OrderManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/v1/order-manager")
@RequiredArgsConstructor
public class OrderManagerController {

    private static final String SUCCESS_BODY_RESPONSE_MESSAGE = "Request sent successfully.";
    private static final String ERROR_BODY_RESPONSE_MESSAGE = "There was an error during handling the request you sent.";

    private final OrderManagerService orderManagerService;

    @GetMapping("/{vendor}/orders/status-count")
    public ResponseEntity<CommonResponse> getOrderStatusCount(@PathVariable SourceVendor vendor) {
        Map<CurrentStatus, Integer> orderStatusCountMap = orderManagerService.getOrderStatusCountMap();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, orderStatusCountMap);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/{vendor}/orders/{orderId}/tracking-number")
    public ResponseEntity<CommonResponse> getTrackingNumber(@PathVariable SourceVendor vendor,
                                                            @PathVariable String orderId) {
        TrackingDto trackingDto = orderManagerService.getTrackingNumber(vendor, orderId);
        CommonResponse.ResponseStatus status = trackingDto == null ? CommonResponse.ResponseStatus.ERROR : CommonResponse.ResponseStatus.SUCCESS;
        CommonResponse commonResponse = new CommonResponse(status, trackingDto);
        return ResponseEntity.ok(commonResponse);

    }

    @GetMapping("/{vendor}/orders/{orderId}")
    public ResponseEntity<CommonResponse> getVendorOrder(@PathVariable SourceVendor vendor,
                                                         @PathVariable String orderId) {
        VendorOrder vendorOrder = orderManagerService.getVendorOrder(vendor, orderId);
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, vendorOrder);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/{vendor}/orders")
    public ResponseEntity<CommonResponse> getOrdersBy(@PathVariable SourceVendor vendor,
                                                      @ModelAttribute OrderQuery orderQuery,
                                                      @PageableDefault Pageable pageable) {
        Page<Order> orderPage = orderManagerService.getOrderListBy(orderQuery, pageable);
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, orderPage);
        return ResponseEntity.ok(commonResponse);
    }

    @PutMapping("/{vendor}")
    public ResponseEntity<CommonResponse> putRunningStatus(@PathVariable SourceVendor vendor,
                                                           @RequestParam RunnerStatus status) {
        orderManagerService.updateRunningStatus(vendor, status);
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, SUCCESS_BODY_RESPONSE_MESSAGE);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/{vendor}/error-order")
    public ResponseEntity<CommonResponse> postProcessNewOrder(@PathVariable SourceVendor vendor) {
        orderManagerService.restartErrorOrders();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, SUCCESS_BODY_RESPONSE_MESSAGE);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/{vendor}/sync-zinc-order")
    public ResponseEntity<CommonResponse> postSyncPurchaseAgencyOrders(@PathVariable SourceVendor vendor,
                                                                       @RequestParam LocalDateTime startTime,
                                                                       @RequestParam LocalDateTime endTime) {
        boolean isCompleted = orderManagerService.syncPurchaseAgencyOrders(startTime, endTime);
        if (!isCompleted) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, ERROR_BODY_RESPONSE_MESSAGE);
            return ResponseEntity.internalServerError().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, SUCCESS_BODY_RESPONSE_MESSAGE);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/{vendor}/sync-coupang-order")
    public ResponseEntity<CommonResponse> postSyncSourceOrders(@PathVariable SourceVendor vendor) {
        boolean isCompleted = orderManagerService.syncSourceOrders();
        if (!isCompleted) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, ERROR_BODY_RESPONSE_MESSAGE);
            return ResponseEntity.internalServerError().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, SUCCESS_BODY_RESPONSE_MESSAGE);
        return ResponseEntity.ok(commonResponse);
    }
}
