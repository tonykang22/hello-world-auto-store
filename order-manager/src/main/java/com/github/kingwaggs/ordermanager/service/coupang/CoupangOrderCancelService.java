package com.github.kingwaggs.ordermanager.service.coupang;

import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.CancelProductDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.RefundDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.RefundOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.exception.ApiException;
import com.github.kingwaggs.ordermanager.coupangsdk.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.ordermanager.util.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangOrderCancelService {

    private static final String SUCCESS_CODE_STR = "200";
    private static final Integer SUCCESS_CODE = 200;
    private static final String REQUEST_FOR_STOP_SHIPPING = "RU";
    private static final String RETURN_REQUESTED = "UC";
    private static final String RETURN_COMPLETED = "CC";
    private static final String REFUND = "RETURN";
    private static final String CANCEL = "CANCEL";
    private static final Long REFUND_REQUEST_MADE_IN = 6L;
    private static final Long CANCEL_REQUEST_MADE_IN = 2L;

    private final CoupangMarketPlaceApi apiInstance;

    public String cancelOrder(Long orderId, Long vendorItemId, String vendorId, String userId) {
        try {
            log.info("Calling Coupang-Cancel-API start. Source Order Id : {}", orderId);
            CancelProductDto.Request request = CancelProductDto.Request.create(orderId, vendorItemId, vendorId, userId);
            CancelProductDto.Response response = apiInstance.cancelOrder(vendorId, orderId, request);
            if (response == null || !response.getCode().equals(SUCCESS_CODE_STR)) {
                log.error("Exception occurred during calling Coupang-Cancel-API. Source Order Id : {}", orderId);
                return null;
            }
            return response.getCancelId();
        } catch (ApiException exception) {
            log.error("Exception occurred during calling Coupang-Cancel-API. Source Order Id : {}", orderId);
            return null;
        }
    }

    public RefundOrder.Item getRefundItemByOrderId(String vendorId, Long orderId) {
        log.info("Fetch request for refund from Coupang customer. Source Order Id : {}", orderId);
        List<String> targetStatusList = List.of(REQUEST_FOR_STOP_SHIPPING, RETURN_REQUESTED, RETURN_COMPLETED);
        List<RefundOrder.Item> refundItemList = new ArrayList<>();
        targetStatusList.forEach(status -> refundItemList.addAll(getRefundItemList(vendorId, status)));
        return refundItemList.stream()
                .filter(refundItem -> refundItem.getOrderId().equals(orderId))
                .findAny()
                .orElse(null);
    }

    public RefundOrder.Item getCancelItemByOrderId(String vendorId, Long orderId) {
        log.info("Fetch cancelled order from Coupang customer. [From order status=ACCEPT] Source Order Id : {}", orderId);
        List<RefundOrder.Item> cancelledItemList = getCancelItemList(vendorId);
        return cancelledItemList.stream()
                .filter(cancelItem -> cancelItem.getOrderId().equals(orderId))
                .findAny()
                .orElse(null);
    }

    public boolean canStopRelease(String vendorId, Long orderId) {
        try {
            log.info("Calling Coupang-Stop-Release API starts. Source Order Id : {}", orderId);
            RefundDto.Request refundRequest = RefundDto.Request.create(orderId, vendorId);
            RefundDto.Response refundResponse = apiInstance.doStopRelease(orderId, vendorId, refundRequest, vendorId);
            if (refundResponse == null || !refundResponse.getCode().equals(SUCCESS_CODE)) {
                log.error("Exception occurred during calling Coupang-Stop-Release API. Source Order Id : {}", orderId);
                return false;
            }
            return true;
        } catch (ApiException exception) {
            log.info("Exception occurred during calling Coupang-Stop-Release API. Source Order Id : {}", orderId);
            return false;
        }
    }

    public boolean isRefundComplete(String vendorId, Long cancelId) {
        try {
            log.info("Calling Coupang-Grant-Refund API starts. Cancel Id : {}", cancelId);
            RefundDto.Request refundConfirmRequest = RefundDto.Request.create(cancelId, vendorId);
            RefundDto.Response response = apiInstance.grantRefundClaim(cancelId, refundConfirmRequest, vendorId, vendorId);
            if (response == null || !response.getCode().equals(SUCCESS_CODE)) {
                log.error("Exception occurred during calling Coupang-Grant-Refund API. Source Cancel Id : {}", cancelId);
                return false;
            }
            return true;
        } catch (ApiException exception) {
            log.error("Exception occurred during calling Coupang-Grant-Refund API. Source Cancel Id : {}", cancelId);
            return false;
        }
    }

    protected List<RefundOrder.Item> getRefundItemList(String vendorId, String returnStatus) {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(REFUND_REQUEST_MADE_IN);

            String endDateInFormat = DateTimeConverter.localDate2DateString(endDate);
            String startDateInFormat = DateTimeConverter.localDate2DateString(startDate);
            RefundOrder response = apiInstance.getRefundClaimOrdersByVendorId(vendorId,
                    vendorId,
                    startDateInFormat,
                    endDateInFormat,
                    returnStatus,
                    50,
                    REFUND);
            if (response == null || !response.getCode().equals(SUCCESS_CODE) || response.getRefundItemList() == null) {
                return new ArrayList<>();
            }
            return response.getRefundItemList();
        } catch (ApiException exception) {
            log.error("Exception occurred when fetching requested refund-order on coupang.", exception);
            return new ArrayList<>();
        }
    }

    protected List<RefundOrder.Item> getCancelItemList(String vendorId) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(CANCEL_REQUEST_MADE_IN);

            String todayInFormat = DateTimeConverter.localDate2DateString(today);
            String yesterdayInFormat = DateTimeConverter.localDate2DateString(yesterday);
            RefundOrder response = apiInstance.getRefundClaimOrdersByVendorId(vendorId,
                    vendorId,
                    yesterdayInFormat,
                    todayInFormat,
                    null,
                    50,
                    CANCEL);
            if (response == null || !response.getCode().equals(SUCCESS_CODE) || response.getRefundItemList() == null) {
                return new ArrayList<>();
            }
            return response.getRefundItemList();
        } catch (ApiException exception) {
            log.error("Exception occurred when fetching requested refund-order on coupang.", exception);
            return new ArrayList<>();
        }
    }

}
