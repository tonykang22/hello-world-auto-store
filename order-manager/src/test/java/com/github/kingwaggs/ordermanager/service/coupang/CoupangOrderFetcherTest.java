package com.github.kingwaggs.ordermanager.service.coupang;

import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrders;
import com.github.kingwaggs.ordermanager.coupangsdk.exception.ApiException;
import com.github.kingwaggs.ordermanager.coupangsdk.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.ordermanager.exception.CoupangApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoupangOrderFetcherTest {

    @Mock
    private CoupangMarketPlaceApi apiInstance;

    @InjectMocks
    private CoupangOrderFetcher coupangOrderFetcher;

    private String vendorId;
    private Long orderId;

    @BeforeEach
    void setUp() {
        this.vendorId = "24680";
        this.orderId = 123456789L;
    }

    @Test
    @DisplayName("[결제완료 주문 리스트 조회] 요청 성공 시")
    void fetchPaymentCompleteOrdersSuccessfully() throws CoupangApiException, ApiException {
        // given
        Integer statusCode = 200;
        CoupangOrders response = mock(CoupangOrders.class);
        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.getOrderSheetsByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt())).thenReturn(response);
        when(response.getData()).thenReturn(new ArrayList<>());
        Thread.currentThread().interrupt();

        // when
        List<CoupangOrder> orders = coupangOrderFetcher.fetchPaymentCompleteOrders(vendorId);


        // then
        verify(apiInstance, only()).getOrderSheetsByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt());
        verify(response, times(1)).getCode();
        verify(response, times(1)).getData();
        assertNotNull(orders);
    }

    @Test
    @DisplayName("[결제완료 주문 리스트 조회] 요청 실패 시 - 쓰레드 예외")
    void fetchPaymentCompleteOrdersWithThreadException() throws ApiException {
        // given
        Integer statusCode = 400;
        CoupangOrders response = mock(CoupangOrders.class);
        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.getOrderSheetsByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt())).thenReturn(response);
        Thread.currentThread().interrupt();

        // when
        assertThrows(CoupangApiException.class, () -> coupangOrderFetcher.fetchPaymentCompleteOrders(vendorId));

        // then
        verify(apiInstance, only()).getOrderSheetsByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt());
        verify(response, times(1)).getCode();
        verify(response, never()).getData();
    }

    @Test
    @DisplayName("[결제완료 주문 리스트 조회] 요청 실패 시 - 400 코드")
    void fetchPaymentCompleteOrdersWithStatusCodeException() throws ApiException {
        // given
        Integer statusCode = 400;
        CoupangOrders response = mock(CoupangOrders.class);
        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.getOrderSheetsByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt())).thenReturn(response);

        // when
        assertThrows(CoupangApiException.class, () -> coupangOrderFetcher.fetchPaymentCompleteOrders(vendorId));


        // then
        verify(apiInstance, times(3)).getOrderSheetsByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt());
        verify(response, times(3)).getCode();
        verify(response, never()).getData();
    }

    @Test
    @DisplayName("[결제완료 주문 리스트 조회] 요청 실패 시 - CoupangMarketPlaceApi 예외")
    void fetchPaymentCompleteOrdersWithApiException() throws ApiException {
        // given
        CoupangOrders response = mock(CoupangOrders.class);

        when(apiInstance.getOrderSheetsByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt())).thenThrow(ApiException.class);

        // when
        assertThrows(CoupangApiException.class, () -> coupangOrderFetcher.fetchPaymentCompleteOrders(vendorId));


        // then
        verify(apiInstance, times(3)).getOrderSheetsByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt());
        verify(response, never()).getCode();
        verify(response, never()).getData();
    }

    @Test
    @DisplayName("[결제완료 주문 번호로 조회] 요청 성공 시")
    void fetchOrderByOrderIdSuccessfully() throws ApiException {
        // given
        Integer statusCode = 200;
        CoupangOrders response = mock(CoupangOrders.class);
        when(response.getCode()).thenReturn(statusCode);
        when(response.getFirstOrder()).thenReturn(new CoupangOrder());

        when(apiInstance.getOrderByOrderId(anyLong(), anyString(), anyString())).thenReturn(response);

        // when
        CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorId, orderId);

        // then
        verify(apiInstance, times(1)).getOrderByOrderId(anyLong(), anyString(), anyString());
        verify(response, times(1)).getCode();
        verify(response, times(1)).getFirstOrder();
        assertNotNull(coupangOrder);
    }

    @Test
    @DisplayName("[결제완료 주문 번호로 조회] 요청 실패 시 - 쓰레드 예외")
    void fetchOrderByOrderIdWithThreadException() throws ApiException {
        // given
        Integer statusCode = 400;
        CoupangOrders response = mock(CoupangOrders.class);
        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.getOrderByOrderId(anyLong(), anyString(), anyString())).thenReturn(response);
        Thread.currentThread().interrupt();

        // when
        CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorId, orderId);

        // then
        verify(apiInstance, times(1)).getOrderByOrderId(anyLong(), anyString(), anyString());
        verify(response, times(1)).getCode();
        verify(response, never()).getFirstOrder();
        assertNull(coupangOrder);
    }

    @Test
    @DisplayName("[결제완료 주문 번호로 조회] 요청 실패 시 - 400 코드")
    void fetchOrderByOrderIdWithException() throws ApiException {
        // given
        Integer statusCode = 400;
        CoupangOrders response = mock(CoupangOrders.class);
        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.getOrderByOrderId(anyLong(), anyString(), anyString())).thenReturn(response);

        // when
        CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorId, orderId);

        // then
        verify(apiInstance, times(3)).getOrderByOrderId(anyLong(), anyString(), anyString());
        verify(response, times(3)).getCode();
        verify(response, never()).getFirstOrder();
        assertNull(coupangOrder);
    }

    @Test
    @DisplayName("[결제완료 주문 번호로 조회] 요청 실패 시 - CoupangMarketPlaceApi 예외")
    void fetchOrderByOrderIdWithApiException() throws ApiException {
        // given
        CoupangOrders response = mock(CoupangOrders.class);

        when(apiInstance.getOrderByOrderId(anyLong(), anyString(), anyString())).thenThrow(ApiException.class);

        // when
        CoupangOrder coupangOrder = coupangOrderFetcher.fetchOrderByOrderId(vendorId, orderId);

        // then
        verify(apiInstance, times(3)).getOrderByOrderId(anyLong(), anyString(), anyString());
        verify(response, never()).getCode();
        verify(response, never()).getFirstOrder();
        assertNull(coupangOrder);
    }

    @Test
    @DisplayName("[결제완료 상태인지 조회] 요청 시 성공")
    void statusPaymentCompleteSuccessfully() throws ApiException {
        // given
        Integer statusCode = 200;
        CoupangOrders response = mock(CoupangOrders.class);
        CoupangOrder order = mock(CoupangOrder.class);

        when(response.getCode()).thenReturn(statusCode);
        when(response.getFirstOrder()).thenReturn(order);

        when(order.getStatus()).thenReturn("ACCEPT");

        when(apiInstance.getOrderByOrderId(anyLong(), anyString(), anyString())).thenReturn(response);

        // when
        boolean statusPaymentComplete = coupangOrderFetcher.isStatusPaymentComplete(vendorId, orderId);

        // then
        verify(apiInstance, times(1)).getOrderByOrderId(anyLong(), anyString(), anyString());
        verify(response, times(1)).getCode();
        verify(response, times(1)).getFirstOrder();
        verify(order, times(1)).getStatus();
        assertTrue(statusPaymentComplete);
    }

    @Test
    @DisplayName("[결제완료 상태인지 조회] 요청 시 실패")
    void statusPaymentCompleteWithException() throws ApiException {
        // given
        Integer statusCode = 400;
        CoupangOrders response = mock(CoupangOrders.class);

        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.getOrderByOrderId(anyLong(), anyString(), anyString())).thenReturn(response);

        // when
        boolean statusPaymentComplete = coupangOrderFetcher.isStatusPaymentComplete(vendorId, orderId);

        // then
        verify(apiInstance, times(1)).getOrderByOrderId(anyLong(), anyString(), anyString());
        verify(response, times(1)).getCode();
        verify(response, never()).getFirstOrder();
        assertFalse(statusPaymentComplete);
    }

    @Test
    @DisplayName("[결제완료 상태인지 조회] 요청 시 실패 - CoupangMarketPlaceApi 예외")
    void statusPaymentCompleteWithApiException() throws ApiException {
        // given
        CoupangOrders response = mock(CoupangOrders.class);

        when(apiInstance.getOrderByOrderId(anyLong(), anyString(), anyString())).thenThrow(ApiException.class);

        // when
        boolean statusPaymentComplete = coupangOrderFetcher.isStatusPaymentComplete(vendorId, orderId);

        // then
        verify(apiInstance, times(1)).getOrderByOrderId(anyLong(), anyString(), anyString());
        verify(response, never()).getCode();
        verify(response, never()).getFirstOrder();
        assertFalse(statusPaymentComplete);
    }

}