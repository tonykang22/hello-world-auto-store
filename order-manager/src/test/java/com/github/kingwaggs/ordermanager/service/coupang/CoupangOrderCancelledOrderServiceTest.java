package com.github.kingwaggs.ordermanager.service.coupang;

import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.CancelProductDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.RefundDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.RefundOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.exception.ApiException;
import com.github.kingwaggs.ordermanager.coupangsdk.service.CoupangMarketPlaceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoupangOrderCancelledOrderServiceTest {

    @Mock
    private CoupangMarketPlaceApi apiInstance;

    @InjectMocks
    private CoupangOrderCancelService coupangOrderCancelService;

    private String vendorId;
    private String userId;
    private Long vendorItemId;
    private Long orderId;
    private Long cancelId;

    @BeforeEach
    void setUp() {
        this.vendorId = "24680";
        this.userId = "08642";
        this.vendorItemId = 987654321L;
        this.orderId = 123456789L;
        this.cancelId = 918273645L;
    }

    @Test
    @DisplayName("[출고중지] 요청 성공 시")
    void canStopReleaseSuccessfully() throws ApiException {
        // given
        Integer statusCode = 200;
        RefundDto.Response response = mock(RefundDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);
        when(apiInstance.doStopRelease(anyLong(), anyString(), any(), anyString())).thenReturn(response);

        // when
        boolean canStopRelease = coupangOrderCancelService.canStopRelease(vendorId, orderId);

        // then
        verify(apiInstance, only()).doStopRelease(anyLong(), anyString(), any(), anyString());
        verify(response, only()).getCode();
        assertTrue(canStopRelease);
    }

    @Test
    @DisplayName("[출고중지] 요청 실패 시 - 반환 코드 예외")
    void canStopReleaseWithResponseCodeException() throws ApiException {
        // given
        Integer statusCode = 400;
        RefundDto.Response response = mock(RefundDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);
        when(apiInstance.doStopRelease(anyLong(), anyString(), any(), anyString())).thenReturn(response);

        // when
        boolean canStopRelease = coupangOrderCancelService.canStopRelease(vendorId, orderId);

        // then
        verify(apiInstance, only()).doStopRelease(anyLong(), anyString(), any(), anyString());
        verify(response, only()).getCode();
        assertFalse(canStopRelease);
    }

    @Test
    @DisplayName("[출고중지] 요청 실패 시 - ApiException")
    void canStopReleaseWithApiException() throws ApiException {
        // given
        RefundDto.Response response = mock(RefundDto.Response.class);
        when(apiInstance.doStopRelease(anyLong(), anyString(), any(), anyString())).thenThrow(ApiException.class);

        // when
        boolean canStopRelease = coupangOrderCancelService.canStopRelease(vendorId, orderId);

        // then
        verify(apiInstance, only()).doStopRelease(anyLong(), anyString(), any(), anyString());
        verify(response, never()).getCode();
        assertFalse(canStopRelease);
    }

    @Test
    @DisplayName("[주문취소] 요청 성공 시")
    void cancelOrderSuccessfully() throws ApiException {
        // given
        String statusCode = "200";
        CancelProductDto.Response response = mock(CancelProductDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);
        when(response.getCancelId()).thenReturn(cancelId.toString());
        when(apiInstance.cancelOrder(anyString(), anyLong(), any())).thenReturn(response);

        // when
        String cancelId = coupangOrderCancelService.cancelOrder(orderId, vendorItemId, vendorId, userId);

        // then
        verify(apiInstance, only()).cancelOrder(anyString(), anyLong(), any());
        verify(response, times(1)).getCode();
        verify(response, times(1)).getCancelId();
        assertNotNull(cancelId);
    }

    @Test
    @DisplayName("[주문취소] 요청 실패 시 - 반환 코드 예외")
    void cancelOrderWithResponseCodeException() throws ApiException {
        // given
        String statusCode = "400";
        CancelProductDto.Response response = mock(CancelProductDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);
        when(apiInstance.cancelOrder(anyString(), anyLong(), any())).thenReturn(response);

        // when
        String cancelId = coupangOrderCancelService.cancelOrder(orderId, vendorItemId, vendorId, userId);

        // then
        verify(apiInstance, only()).cancelOrder(anyString(), anyLong(), any());
        verify(response, times(1)).getCode();
        verify(response, never()).getCancelId();
        assertNull(cancelId);
    }

    @Test
    @DisplayName("[주문취소] 요청 실패 시 - ApiException")
    void cancelOrderWithApiException() throws ApiException {
        // given
        CancelProductDto.Response response = mock(CancelProductDto.Response.class);
        when(apiInstance.cancelOrder(anyString(), anyLong(), any())).thenThrow(ApiException.class);

        // when
        String cancelId = coupangOrderCancelService.cancelOrder(orderId, vendorItemId, vendorId, userId);

        // then
        verify(apiInstance, only()).cancelOrder(anyString(), anyLong(), any());
        verify(response, never()).getCode();
        verify(response, never()).getCancelId();
        assertNull(cancelId);
    }

    @Test
    @DisplayName("[환불처리] 요청 성공 시")
    void refundCompleteSuccessfully() throws ApiException {
        // given
        Integer statusCode = 200;
        RefundDto.Response response = mock(RefundDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);
        when(apiInstance.grantRefundClaim(anyLong(), any(), anyString(), anyString())).thenReturn(response);

        // when
        boolean refundComplete = coupangOrderCancelService.isRefundComplete(vendorId, cancelId);

        // then
        verify(apiInstance, only()).grantRefundClaim(anyLong(), any(), anyString(), anyString());
        verify(response, times(1)).getCode();
        assertTrue(refundComplete);
    }

    @Test
    @DisplayName("[환불처리] 요청 실패 시 - 반환 코드 예외")
    void refundCompleteWithResponseCodeException() throws ApiException {
        // given
        Integer statusCode = 400;
        RefundDto.Response response = mock(RefundDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);
        when(apiInstance.grantRefundClaim(anyLong(), any(), anyString(), anyString())).thenReturn(response);

        // when
        boolean refundComplete = coupangOrderCancelService.isRefundComplete(vendorId, cancelId);

        // then
        verify(apiInstance, only()).grantRefundClaim(anyLong(), any(), anyString(), anyString());
        verify(response, times(1)).getCode();
        assertFalse(refundComplete);
    }

    @Test
    @DisplayName("[환불처리] 요청 실패 시 - ApiException")
    void refundCompleteWithApiException() throws ApiException {
        // given
        Integer statusCode = 400;
        RefundDto.Response response = mock(RefundDto.Response.class);
        when(apiInstance.grantRefundClaim(anyLong(), any(), anyString(), anyString())).thenThrow(ApiException.class);

        // when
        boolean refundComplete = coupangOrderCancelService.isRefundComplete(vendorId, cancelId);

        // then
        verify(apiInstance, only()).grantRefundClaim(anyLong(), any(), anyString(), anyString());
        verify(response, never()).getCode();
        assertFalse(refundComplete);
    }

    @Test
    @DisplayName("[주문번호로 반품/취소 조회] 요청 성공 시")
    void getRefundItemByOrderIdSuccessfullyWithItem() throws ApiException {
        // given
        Integer statusCode = 200;
        RefundOrder response = mock(RefundOrder.class);
        RefundOrder.Item refundOrderItem = mock(RefundOrder.Item.class);
        when(response.getCode()).thenReturn(statusCode);
        when(response.getRefundItemList()).thenReturn(List.of(refundOrderItem));
        when(refundOrderItem.getOrderId()).thenReturn(orderId);

        when(apiInstance.getRefundClaimOrdersByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), anyString())).thenReturn(response);

        // when
        RefundOrder.Item refundItemByOrderId = coupangOrderCancelService.getRefundItemByOrderId(vendorId, orderId);

        // then
        verify(apiInstance, times(3)).getRefundClaimOrdersByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), anyString());
        verify(response, times(3)).getCode();
        assertNotNull(refundItemByOrderId);
    }

    @Test
    @DisplayName("[주문번호로 반품/취소 조회] 요청 실패 시 - 반환 코드 예외")
    void getRefundItemByOrderIdWithResponseCodeException() throws ApiException {
        // given
        Integer statusCode = 400;
        RefundOrder response = mock(RefundOrder.class);
        when(response.getCode()).thenReturn(statusCode);
        when(apiInstance.getRefundClaimOrdersByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), anyString())).thenReturn(response);

        // when
        RefundOrder.Item refundItemByOrderId = coupangOrderCancelService.getRefundItemByOrderId(vendorId, orderId);

        // then
        verify(apiInstance, times(3)).getRefundClaimOrdersByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), anyString());
        verify(response, times(3)).getCode();
        assertNull(refundItemByOrderId);
    }

    @Test
    @DisplayName("[주문번호로 반품/취소 조회] 요청 실패 시 - ApiException")
    void getRefundItemByOrderIdWithApiException() throws ApiException {
        // given
        RefundOrder response = mock(RefundOrder.class);

        when(apiInstance.getRefundClaimOrdersByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), anyString())).thenThrow(ApiException.class);

        // when
        RefundOrder.Item refundItemByOrderId = coupangOrderCancelService.getRefundItemByOrderId(vendorId, orderId);

        // then
        verify(apiInstance, times(3)).getRefundClaimOrdersByVendorId(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), anyString());
        verify(response, never()).getCode();
        assertNull(refundItemByOrderId);
    }
}
