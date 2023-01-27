package com.github.kingwaggs.ordermanager.service.amazon;

import com.github.kingwaggs.ordermanager.domain.ZincCredential;
import com.github.kingwaggs.ordermanager.domain.dto.request.ZincOrderRequest;
import com.github.kingwaggs.ordermanager.domain.dto.response.ZincResponse;
import com.github.kingwaggs.ordermanager.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmazonPurchaseServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ZincCredential zincCredential;

    @InjectMocks
    private AmazonPurchaseService amazonPurchaseService;


    private String orderId;
    private String requestId;
    private String cancelId;
    private String merchantOrderId;
    private String asin;
    private Double price;

    @BeforeEach
    void setUp() {
        this.orderId = "AB3S245ZB";
        this.requestId = "cf062464e945eee96def2b643f13ad";
        this.cancelId = "45eee96def2b9ds45eee96de593djv";
        this.merchantOrderId = "L5TB07N86G";
        this.asin = "B07N86GL5T";
        this.price = 100D;
        ReflectionTestUtils.setField(amazonPurchaseService, "zincApiKey", "1234");
    }

    @Test
    @DisplayName("[상품 구매] 요청 성공 시")
    void purchaseOrderSuccessfully() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), any(ZincOrderRequest.class)))
                .thenReturn(responseEntity);
        when(response.getRequestId()).thenReturn(requestId);

        // when
        String actualRequestId = amazonPurchaseService.purchaseOrder(orderId, asin, price);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), any(ZincOrderRequest.class));
        verify(response, times(1)).getRequestId();
        assertEquals(requestId, actualRequestId);
    }

    @Test
    @DisplayName("[상품 구매] 요청 실패 시 - Response Entity 예외")
    void purchaseOrderWithResponseException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), any(ZincOrderRequest.class)))
                .thenReturn(responseEntity);

        // when
        String requestId = amazonPurchaseService.purchaseOrder(orderId, asin, price);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), any(ZincOrderRequest.class));
        verify(response, never()).getRequestId();
        assertNull(requestId);
    }

    @Test
    @DisplayName("[상품 구매] 요청 실패 시 - Response body null 예외")
    void purchaseOrderWithResponseNullException() {
        // given
        ZincResponse response = mock(ZincResponse.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), any(ZincOrderRequest.class)))
                .thenThrow(RestClientException.class);

        // when
        String requestId = amazonPurchaseService.purchaseOrder(orderId, asin, price);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), any(ZincOrderRequest.class));
        verify(response, never()).getRequestId();
        assertNull(requestId);
    }

    @Test
    @DisplayName("[상품 구매 취소] 요청 성공 시")
    void cancelOrderSuccessfully() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), anyString()))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("request_processing");
        when(response.getRequestId()).thenReturn(cancelId);

        // when
        String actualCancelId = amazonPurchaseService.cancelOrder(requestId, merchantOrderId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), anyString());
        verify(response, times(1)).getType();
        verify(response, times(1)).getRequestId();
        assertEquals(cancelId, actualCancelId);
    }

    @Test
    @DisplayName("[상품 구매 취소] 요청 실패 시 - 반환 [type = error]")
    void cancelOrderWithResponseTypeException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), anyString()))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("error");

        // when
        String actualCancelId = amazonPurchaseService.cancelOrder(requestId, merchantOrderId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), anyString());
        verify(response, times(1)).getType();
        verify(response, never()).getRequestId();
        assertNull(actualCancelId);
    }

    @Test
    @DisplayName("[상품 구매 취소] 요청 실패 시 - Response Entity 예외")
    void cancelOrderWithResponseException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        String cancelId = "234298";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), anyString()))
                .thenThrow(RestClientException.class);

        // when
        String actualCancelId = amazonPurchaseService.cancelOrder(requestId, merchantOrderId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), anyString());
        verify(response, never()).getType();
        verify(response, never()).getRequestId();
        assertNull(actualCancelId);
    }

    @Test
    @DisplayName("[주문 상태 조회] 요청 성공 시")
    void getOrderStatusSuccessfully() throws CreditCardDeclinedException, PaymentVerificationException, PurchaseAgencyException, ZincApiException {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("order_response");

        // when
        ZincResponse actualResponse = amazonPurchaseService.getOrderStatus(requestId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getType();
        verify(response, never()).getCode();
        assertNotNull(actualResponse);
    }

    @Test
    @DisplayName("[주문 상태 조회] 요청 실패 시 - 주문 진행 중")
    void getOrderStatusWithProcessingException() throws CreditCardDeclinedException, PaymentVerificationException, PurchaseAgencyException, ZincApiException {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("error");
        when(response.getCode()).thenReturn("request_processing");

        // when
        ZincResponse actualResponse = amazonPurchaseService.getOrderStatus(requestId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getType();
        verify(response, times(1)).getCode();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[주문 상태 조회] 요청 실패 시 - 잔액 부족 예외")
    void getOrderStatusWithCardException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("error");
        when(response.getCode()).thenReturn("credit_card_declined");

        // when
        assertThrows(CreditCardDeclinedException.class, () -> amazonPurchaseService.getOrderStatus(requestId));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getType();
        verify(response, times(2)).getCode();
    }

    @Test
    @DisplayName("[주문 상태 조회] 요청 실패 시 - Response entity 예외")
    void getOrderStatusWithResponseException() {
        // given
        ZincResponse response = mock(ZincResponse.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        assertThrows(ZincApiException.class, () -> amazonPurchaseService.getOrderStatus(requestId));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, never()).getType();
        verify(response, never()).getCode();
    }

    @Test
    @DisplayName("[주문 상태 조회] 요청 실패 시 - Response body null 예외")
    void getOrderStatusWithResponseNullException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);

        // when
        assertThrows(ZincApiException.class, () -> amazonPurchaseService.getOrderStatus(requestId));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, never()).getType();
        verify(response, never()).getCode();
    }

    @Test
    @DisplayName("[주문 상태 조회] 요청 실패 시 - 그 외 예외")
    void getOrderStatusWithOtherException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("error");
        when(response.getCode()).thenReturn("???");

        // when
        assertThrows(PurchaseAgencyException.class, () -> amazonPurchaseService.getOrderStatus(requestId));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getType();
        verify(response, times(4)).getCode();
    }

    @Test
    @DisplayName("[Request Id로 상품 구매 취소] 요청 성공 시")
    void cancelOrderByRequestIdSuccessfully() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("order_response");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), anyString()))
                .thenReturn(responseEntity);
        when(response.getRequestId()).thenReturn(cancelId);
        when(response.getMerchantOrderId()).thenReturn(merchantOrderId);

        // when
        String actualCancelId = amazonPurchaseService.cancelOrderByRequestId(requestId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(ZincResponse.class), anyString());
        verify(response, times(2)).getType();
        verify(response, times(2)).getRequestId();
        assertEquals(cancelId, actualCancelId);
    }

    @Test
    @DisplayName("[취소 상태 조회] 요청 성공 시")
    void getCancelStatusSuccessfully() throws CancellationException {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("cancellation_response");

        // when
        ZincResponse actualResponse = amazonPurchaseService.getCancelStatus(requestId, cancelId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getType();
        verify(response, never()).getCode();
        assertNotNull(actualResponse);
    }

    @Test
    @DisplayName("[취소 상태 조회] 요청 실패 시 - 주문 진행 중")
    void getCancelStatusWithProcessingException() throws CancellationException {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("error");
        when(response.getCode()).thenReturn("request_processing");

        // when
        ZincResponse actualResponse = amazonPurchaseService.getCancelStatus(requestId, cancelId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getType();
        verify(response, times(1)).getCode();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[취소 상태 조회] 요청 실패 시 - 요청 실패 예외")
    void getCancelStatusWithRequestFailException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("error");
        when(response.getCode()).thenReturn("request_fail");

        // when
        assertThrows(CancellationException.class, () -> amazonPurchaseService.getCancelStatus(requestId, cancelId));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getType();
        verify(response, times(2)).getCode();
    }

    @Test
    @DisplayName("[취소 상태 조회] 요청 실패 시 - Response entity 예외")
    void getCancelStatusWithResponseException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        assertThrows(CancellationException.class, () -> amazonPurchaseService.getCancelStatus(requestId, cancelId));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, never()).getType();
        verify(response, never()).getCode();
    }

    @Test
    @DisplayName("[취소 상태 조회] 요청 실패 시 - Response body null 예외")
    void getCancelStatusWithResponseNullException() throws CancellationException {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);

        // when
        ZincResponse actualResponse = amazonPurchaseService.getCancelStatus(requestId, cancelId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, never()).getType();
        verify(response, never()).getCode();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[취소 상태 조회] 요청 실패 시 - 그 외 예외")
    void getCancelStatusWithOtherException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getType()).thenReturn("error");
        when(response.getCode()).thenReturn("???");

        // when
        assertThrows(CancellationException.class, ()-> amazonPurchaseService.getCancelStatus(requestId, cancelId));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getType();
        verify(response, times(2)).getCode();
    }

    @Test
    @DisplayName("[배송 상태 조회] 요청 성공 시")
    void getShippingStatusSuccessfully() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ZincResponse.Tracking trackingResponse = mock(ZincResponse.Tracking.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);
        when(response.getTracking()).thenReturn(List.of(trackingResponse));

        // when
        ZincResponse actualResponse = amazonPurchaseService.getShippingStatus(requestId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, times(1)).getTracking();
        assertNotNull(actualResponse);
    }

    @Test
    @DisplayName("[배송 상태 조회] 요청 실패 시 - Response body null 예외")
    void getShippingStatusResponseNullException() {
        // given
        ZincResponse response = mock(ZincResponse.class);
        ResponseEntity<ZincResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenReturn(responseEntity);

        // when
        ZincResponse actualResponse = amazonPurchaseService.getShippingStatus(requestId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, never()).getTracking();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[배송 상태 조회] 요청 실패 시 - Response entity 예외")
    void getShippingStatusResponseException() {
        // given
        ZincResponse response = mock(ZincResponse.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        ZincResponse actualResponse = amazonPurchaseService.getShippingStatus(requestId);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(ZincResponse.class));
        verify(response, never()).getTracking();
        assertNull(actualResponse);
    }

}