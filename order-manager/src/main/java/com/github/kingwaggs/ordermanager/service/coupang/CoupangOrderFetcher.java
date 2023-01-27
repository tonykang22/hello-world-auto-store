package com.github.kingwaggs.ordermanager.service.coupang;

import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrders;
import com.github.kingwaggs.ordermanager.coupangsdk.exception.ApiException;
import com.github.kingwaggs.ordermanager.coupangsdk.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.ordermanager.exception.CoupangApiException;
import com.github.kingwaggs.ordermanager.util.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoupangOrderFetcher {

    private static final int DEFAULT_MAX_PRODUCT_PER_PAGE = 50;
    private static final String ORDER_STATUS_ACCEPT = "ACCEPT";
    private static final String ORDER_STATUS_PREPARE_SHIPPING = "INSTRUCT";
    private static final Integer SUCCESS_CODE = 200;
    private static final int TRIAL_COUNT = 3;
    private final CoupangMarketPlaceApi apiInstance;

    public List<CoupangOrder> fetchPaymentCompleteOrders(String vendorId) throws CoupangApiException {
        int trial = TRIAL_COUNT;
        while (trial-- > 0) {
            log.info("Fetch Coupang order list begins. [status=ACCEPT] {} times tried.", TRIAL_COUNT - trial);
            CoupangOrders orders = getCoupangOrders(vendorId, ORDER_STATUS_ACCEPT);
            if (orders != null) {
                log.info("Return Coupang order list. [status=ACCEPT]");
                return orders.getData();
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                log.error("Thread exception occurred during fetching Coupang order list.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.error("Fetching Coupang order list failed 3 times in a row. [status=ACCEPT]");
        throw new CoupangApiException();
    }

    public List<CoupangOrder> fetchPrepareShippingOrders(String vendorId) throws CoupangApiException {
        log.info("Fetch Coupang order list start. [status=INSTRUCT]");
        CoupangOrders orders = getCoupangOrders(vendorId, ORDER_STATUS_PREPARE_SHIPPING);
        if (orders == null) {
            throw new CoupangApiException();
        }
        log.info("Return Coupang order list. [status=INSTUCT]");
        return orders.getData();
    }

    public CoupangOrder fetchOrderByOrderId(String vendorId, Long orderId) {
        int trial = TRIAL_COUNT;
        while (trial-- > 0) {
            log.info("Fetch Coupang order by order id begins. Source Order Id : {},  {} times tried.", orderId, TRIAL_COUNT - trial);
            CoupangOrder coupangOrder = getResponseByOrderId(vendorId, orderId);
            if (coupangOrder != null) {
                return coupangOrder;
            }
            log.info("Coupang order is null. Source Order Id : {}", orderId);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException exception) {
                log.error("Thread exception occurred during fetching Coupang order (by order id : {}).", orderId);
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.error("Fetching Coupang order failed 3 times in a row. Source Order Id : {}", orderId);
        return null;
    }

    public boolean isStatusPaymentComplete(String vendorId, Long orderId) {
        CoupangOrder response = getResponseByOrderId(vendorId, orderId);
        if (response == null) {
            return false;
        }
        return response.getStatus().equals(ORDER_STATUS_ACCEPT);
    }

    private CoupangOrders getCoupangOrders(String vendorId, String orderStatus) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1L);

            String todayInFormat = DateTimeConverter.localDate2DateString(today);
            String yesterdayInFormat = DateTimeConverter.localDate2DateString(yesterday);

            CoupangOrders response = apiInstance.getOrderSheetsByVendorId(vendorId, vendorId, yesterdayInFormat, todayInFormat,
                    orderStatus, DEFAULT_MAX_PRODUCT_PER_PAGE);
            if (!response.getCode().equals(SUCCESS_CODE)) {
                log.error("Exception occurred during fetching Coupang order list.");
                return null;
            }
            return response;
        } catch (ApiException exception) {
            log.error("Exception occurred during fetching Coupang order list response.", exception);
            return null;
        }
    }

    private CoupangOrder getResponseByOrderId(String vendorId, Long orderId) {
        try {
            CoupangOrders response = apiInstance.getOrderByOrderId(orderId, vendorId, vendorId);
            log.debug("Coupang's sold-list has been fetched.");
            if (!response.getCode().equals(SUCCESS_CODE)) {
                log.error("Exception occurred during fetch Coupang order response. Source Order Id : {}", orderId);
                return null;
            }
            return response.getFirstOrder();
        } catch (ApiException exception) {
            log.error("Exception occurred during fetch Coupang order response. Source Order Id : {}", orderId, exception);
            return null;
        }
    }
}