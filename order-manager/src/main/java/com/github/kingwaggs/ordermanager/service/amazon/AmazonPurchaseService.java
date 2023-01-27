package com.github.kingwaggs.ordermanager.service.amazon;

import com.github.kingwaggs.ordermanager.domain.ZincCredential;
import com.github.kingwaggs.ordermanager.domain.dto.request.ZincOrderRequest;
import com.github.kingwaggs.ordermanager.domain.dto.response.ZincOrderListResponse;
import com.github.kingwaggs.ordermanager.domain.dto.response.ZincResponse;
import com.github.kingwaggs.ordermanager.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonPurchaseService {

    private static final String BASE_URL = "https://api.zinc.io/v1/";
    private static final String ORDERS = "orders";
    private static final String CANCEL = "/cancel";
    private static final String CANCELLATION = "cancellations";
    private static final String REQUEST_PROCESSING = "request_processing";
    private static final String ZINC_PURCHASE_DONE = "order_response";
    private static final String ZINC_CANCEL_DONE = "cancellation_response";
    private static final String CREDIT_CARD_DECLINE_ERROR = "credit_card_declined";
    private static final String PAYMENT_PROBLEM = "payment_info_problem";
    private static final String ERROR = "error";
    private static final String LIMIT = "limit";
    private static final int ORDER_LIST_FETCHING_LIMIT = 1000;
    private static final String ORDER_MADE_FROM = "starting_after";
    private static final String ORDER_MADE_UNTIL = "ending_before";
    private static final String RETAILER = "retailer";
    private static final String RETAILER_AMAZON = "amazon";
    private final RestTemplate restTemplate;
    private final ZincCredential zincCredential;
    @Value("${zinc.API.key}")
    private String zincApiKey;

    public String purchaseOrder(String sourceOrderId, String asin, Double price) {
        try {
            log.info("Zinc purchase order request starts. Source Order Id: {}, ASIN: {}, Price: {}",
                    sourceOrderId, asin, price);
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .path(ORDERS)
                    .encode()
                    .toUriString();

            ZincOrderRequest request = ZincOrderRequest.create(asin, price, zincCredential);
            HttpEntity<ZincOrderRequest> requestEntity = createRequestEntityWithBody(request);

            ResponseEntity<ZincResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ZincResponse.class);
            ZincResponse zincResponse = response.getBody();
            if (zincResponse == null) {
                log.error("Exception occurred during purchasing order from Zinc. Source Order Id : {}", sourceOrderId);
                return null;
            }
            log.info("Zinc purchase order request succeeded. Source Order Id : {}", sourceOrderId);
            return zincResponse.getRequestId();

        } catch (RestClientException exception) {
            log.error("Exception occurred during purchasing order from Zinc. Source Order Id : {}", sourceOrderId);
            return null;
        }
    }

    public String cancelOrder(String requestId, String merchantOrderId) {
        try {
            log.info("Zinc cancel order request starts. Purchase Agency Request Id : {}", requestId);
            HttpEntity<String> requestEntity = createRequestEntity();
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .path(ORDERS)
                    .path("/" + requestId)
                    .path(CANCEL)
                    .encode()
                    .toUriString();

            ResponseEntity<ZincResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ZincResponse.class, merchantOrderId);
            ZincResponse zincResponse = response.getBody();
            if (zincResponse == null || zincResponse.getType().equals(ERROR)) {
                log.error("Exception occurred during cancelling order from Zinc. Purchase Agency Request Id : {}", requestId);
                return null;
            }
            log.info("Zinc cancel order request succeeded. Purchase Agency Request Id : {}", requestId);
            return zincResponse.getRequestId();

        } catch (RestClientException exception) {
            log.error("Exception occurred during cancelling order from Zinc. Purchase Agency Request Id : {}", requestId);
            return null;
        }
    }

    public String cancelOrderByRequestId(String requestId) {
        log.info("Zinc cancel order request starts. Purchase Agency Request Id : {}", requestId);
        ZincResponse zincOrderStatus = getCompletedOrderStatus(requestId);
        if (zincOrderStatus == null) {
            log.error("Exception occurred during fetching order status on Zinc. Purchase Agency Request Id : {}", requestId);
            return null;
        }
        log.info("Zinc purchase order completed. Purchase Agency Request Id : {}", requestId);
        return cancelOrder(zincOrderStatus.getRequestId(), zincOrderStatus.getMerchantOrderId());
    }

    public ZincResponse getCancelStatus(String requestId, String cancelId) throws CancellationException {
        try {
            log.info("Fetching Zinc cancel status starts. Purchase Agency Request Id : {}", requestId);
            ZincResponse zincCancelResponse = getZincResponse(cancelId, CANCELLATION);

            if (zincCancelResponse == null) {
                log.error("Exception occurred during fetching zinc cancelling status. Purchase Agency Cancel Id : {}", cancelId);
                return null;
            } else if (zincCancelResponse.getType().equals(ZINC_CANCEL_DONE)) {
                log.info("Zinc cancel order completed. Purchase Agency Cancel Id : {}", cancelId);
                return zincCancelResponse;
            } else if (zincCancelResponse.getCode().equals(REQUEST_PROCESSING)) {
                log.info("Zinc cancel order not finished. Purchase Agency Cancel Id : {}", cancelId);
                return null;
            }
            log.error("Exception occurred during fetching cancel status on Zinc. Purchase Agency Cancel Id : {}", cancelId);
            throw new CancellationException(zincCancelResponse.getCode());

        } catch (RestClientException exception) {
            log.error("Zinc cancel order cannot be made. Purchase Agency Cancel Id : {}", cancelId);
            throw new CancellationException("Unable to cancel zinc order.");
        }

    }

    public ZincResponse getCompletedOrderStatus(String requestId) {
        log.info("Fetching completed Zinc order status starts. Purchase Agency Request Id : {}", requestId);
        ZincResponse zincResponse = getZincResponse(requestId, ORDERS);

        if (zincResponse == null) {
            log.error("Exception occurred during fetching completed order status on Zinc. Purchase Agency Request Id : {}", requestId);
            return null;
        } else if (zincResponse.getType().equals(ZINC_PURCHASE_DONE)) {
            log.info("Fetching completed Zinc order succeeded. Purchase Agency Request Id : {}", requestId);
            return zincResponse;
        }
        return null;
    }

    public ZincResponse getOrderStatus(String requestId) throws CreditCardDeclinedException, PaymentVerificationException, PurchaseAgencyException, ZincApiException {
        try {
            log.info("Fetching Zinc order status starts. Purchase Agency Request Id : {}", requestId);
            ZincResponse zincResponse = getZincResponse(requestId, ORDERS);

            if (zincResponse == null) {
                log.error("Exception occurred during fetching order status on Zinc. Purchase Agency Request Id : {}", requestId);
                throw new ZincApiException();
            } else if (zincResponse.getType().equals(ZINC_PURCHASE_DONE)) {
                log.info("Zinc purchase order completed. Purchase Agency Request Id : {}", requestId);
                return zincResponse;
            } else if (zincResponse.getCode().equals(REQUEST_PROCESSING)) {
                log.info("Zinc purchase order not finished. Purchase Agency Request Id : {}", requestId);
                return null;
            } else if (zincResponse.getCode().equals(CREDIT_CARD_DECLINE_ERROR)) {
                log.error("CARD_DECLINE exception occurred during fetching order status on Zinc. Purchase Agency Request Id : {}", requestId);
                throw new CreditCardDeclinedException();
            } else if (zincResponse.getCode().equals(PAYMENT_PROBLEM)) {
                log.error("PAYMENT_PROBLEM exception occurred during fetching order status on Zinc. Purchase Agency Request Id : {}", requestId);
                throw new PaymentVerificationException();
            }
            log.error("Exception occurred during fetching order status on Zinc. Purchase Agency Request Id : {}", requestId);
            throw new PurchaseAgencyException(zincResponse.getCode());

        } catch (RestClientException exception) {
            log.error("Exception occurred during fetching order status on Zinc. Purchase Agency Request Id : {}", requestId);
            throw new ZincApiException();
        }
    }

    public ZincResponse getShippingStatus(String requestId) {
        try {
            log.info("Fetching Zinc shipping status starts. Purchase Agency Request Id : {}", requestId);
            ZincResponse zincResponse = getZincResponse(requestId, ORDERS);

            if (zincResponse == null || zincResponse.getTracking() == null) {
                log.error("Exception occurred during fetching shipping status on Zinc. Purchase Agency Request Id : {}", requestId);
                return null;
            }
            log.info("Fetching Zinc shipping status completed. Purchase Agency Request Id : {}", requestId);
            return zincResponse;

        } catch (RestClientException exception) {
            log.error("Exception occurred during fetching shipping status on Zinc. Purchase Agency Request Id : {}", requestId);
            return null;
        }
    }

    public List<ZincResponse> getCompletedZincOrderList(Long fromUnixTimestamps, Long untilUnixTimestamps) {
        List<ZincResponse> zincOrderList = getZincOrderList(fromUnixTimestamps, untilUnixTimestamps);
        if (zincOrderList == null) {
            return null;
        }
        zincOrderList.removeIf(order -> !isZincOrderCompleted(order));
        log.info("Return Zinc order list after removing uncompleted zinc-orders.");
        return zincOrderList;
    }

    private List<ZincResponse> getZincOrderList(Long fromUnixTimestamps, Long untilUnixTimestamps) {
        try {
            log.info("Fetch Zinc order list start.");
            HttpEntity<String> requestEntity = createRequestEntity();
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .path(ORDERS)
                    .queryParam(LIMIT, ORDER_LIST_FETCHING_LIMIT)
                    .queryParam(ORDER_MADE_FROM, fromUnixTimestamps)
                    .queryParam(ORDER_MADE_UNTIL, untilUnixTimestamps)
                    .queryParam(RETAILER, RETAILER_AMAZON)
                    .encode()
                    .toUriString();
            ResponseEntity<ZincOrderListResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ZincOrderListResponse.class);
            ZincOrderListResponse responseBody = response.getBody();

            if (responseBody == null || responseBody.getOrders() == null) {
                log.error("Exception occurred during fetching order list from Zinc.");
                return null;
            }
            log.info("Return Zinc order list.");
            return responseBody.getOrders();

        } catch (RestClientException exception) {
            log.error("Exception occurred during fetching order list from Zinc.");
            return null;
        }
    }

    private boolean isZincOrderCompleted(ZincResponse zincResponse) {
        log.info("Check whether zinc-order is completed. Purchase Agency Request Id : {}", zincResponse.getRequestId());
        if (zincResponse.getType().equals(ZINC_PURCHASE_DONE)) {
            log.info("Completed zinc-order. Purchase Agency Request Id : {}", zincResponse.getRequestId());
            return true;
        }
        log.info("Uncompleted zinc-order. Purchase Agency Request Id : {}", zincResponse.getRequestId());
        return false;
    }

    private ZincResponse getZincResponse(String requestId, String requestType) throws RestClientException {
        HttpEntity<String> requestEntity = createRequestEntity();
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(requestType)
                .path("/" + requestId)
                .encode()
                .toUriString();

        ResponseEntity<ZincResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ZincResponse.class);
        return response.getBody();
    }

    private HttpEntity<ZincOrderRequest> createRequestEntityWithBody(ZincOrderRequest requestBody) {
        HttpHeaders requestHeaders = new HttpHeaders();
        String encodeBasicAuth = HttpHeaders.encodeBasicAuth(zincApiKey, "", StandardCharsets.UTF_8);
        requestHeaders.setBasicAuth(encodeBasicAuth);
        requestHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(requestBody, requestHeaders);
    }

    private HttpEntity<String> createRequestEntity() {
        HttpHeaders requestHeaders = new HttpHeaders();
        String encodeBasicAuth = HttpHeaders.encodeBasicAuth(zincApiKey, "", StandardCharsets.UTF_8);
        requestHeaders.setBasicAuth(encodeBasicAuth);
        requestHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(requestHeaders);
    }

}
