package com.github.kingwaggs.ordermanager.service.coupang;

import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping.PrepareShipmentDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping.UploadInvoiceDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.exception.ApiException;
import com.github.kingwaggs.ordermanager.coupangsdk.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.ordermanager.exception.CoupangApiException;
import com.github.kingwaggs.ordermanager.service.coupang.CoupangOrderShippingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoupangOrderShippingServiceTest {

    @Mock
    private CoupangMarketPlaceApi apiInstance;

    @InjectMocks
    private CoupangOrderShippingService coupangOrderShippingService;

    private String vendorId;
    private String trackingNumber;
    private Long orderId;
    private Long shipmentBoxId;

    @BeforeEach
    void setUp() {
        this.vendorId = "24680";
        this.trackingNumber = "08642";
        this.orderId = 123456789L;
        this.shipmentBoxId = 918273645L;
    }

    @Test
    @DisplayName("[쿠팡 주문 상품준비중으로 상태 변경] 요청 성공 시")
    void updateStatusToPrepareShipmentSuccessfully() throws ApiException, CoupangApiException {
        // given
        Integer statusCode = 200;
        Integer responseCode = 0;
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);
        when(response.getResponseCode()).thenReturn(responseCode);

        when(apiInstance.applyPrepareShipmentStatus(any(), anyString(), anyString())).thenReturn(response);

        // when
        PrepareShipmentDto.Response actualResponse = coupangOrderShippingService.updateStatusToPrepareShipment(vendorId, orderId, shipmentBoxId);

        // then
        verify(apiInstance, times(1)).applyPrepareShipmentStatus(any(), anyString(), anyString());
        verify(response, times(1)).getCode();
        verify(response, times(1)).getResponseCode();
        assertNotNull(actualResponse);
    }

    @Test
    @DisplayName("[쿠팡 주문 상품준비중으로 상태 변경] 요청 실패 시 - 요청 응답 코드 오류")
    void updateStatusToPrepareShipmentWithStatusCodeException() throws ApiException, CoupangApiException {
        // given
        Integer statusCode = 400;
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.applyPrepareShipmentStatus(any(), anyString(), anyString())).thenReturn(response);

        // when
        PrepareShipmentDto.Response actualResponse = coupangOrderShippingService.updateStatusToPrepareShipment(vendorId, orderId, shipmentBoxId);

        // then
        verify(apiInstance, times(1)).applyPrepareShipmentStatus(any(), anyString(), anyString());
        verify(response, times(1)).getCode();
        verify(response, never()).getResponseCode();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[쿠팡 주문 상품준비중으로 상태 변경] 요청 실패 시 - 상태 변경 코드 오류")
    void updateStatusToPrepareShipmentWithResponseCodeException() throws ApiException {
        // given
        Integer statusCode = 200;
        Integer responseCode = 99;
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);
        when(response.getCode()).thenReturn(statusCode);
        when(response.getResponseCode()).thenReturn(responseCode);

        when(apiInstance.applyPrepareShipmentStatus(any(), anyString(), anyString())).thenReturn(response);

        // when
        assertThrows(CoupangApiException.class, () -> coupangOrderShippingService.updateStatusToPrepareShipment(vendorId, orderId, shipmentBoxId));

        // then
        verify(apiInstance, times(1)).applyPrepareShipmentStatus(any(), anyString(), anyString());
        verify(response, times(1)).getCode();
        verify(response, times(1)).getResponseCode();
    }

    @Test
    @DisplayName("[쿠팡 주문 상품준비중으로 상태 변경] 요청 실패 시 - CoupangMarketPlaceApi 예외")
    void updateStatusToPrepareShipmentWithApiException() throws ApiException, CoupangApiException {
        // given
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);

        when(apiInstance.applyPrepareShipmentStatus(any(), anyString(), anyString())).thenThrow(ApiException.class);

        // when
        PrepareShipmentDto.Response actualResponse = coupangOrderShippingService.updateStatusToPrepareShipment(vendorId, orderId, shipmentBoxId);

        // then
        verify(apiInstance, times(1)).applyPrepareShipmentStatus(any(), anyString(), anyString());
        verify(response, never()).getCode();
        verify(response, never()).getResponseCode();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[쿠팡 송장 업데이트] 요청 성공 시")
    void applyPrepareShipmentStatusSuccessfully() throws ApiException, CoupangApiException {
        // given
        Integer statusCode = 200;
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);
        UploadInvoiceDto.Request request = mock(UploadInvoiceDto.Request.class);
        CoupangOrder coupangOrder = mock(CoupangOrder.class);

        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.applyInvoicesUpload(any(), anyString(), anyString())).thenReturn(response);

        // when
        PrepareShipmentDto.Response actualResponse = coupangOrderShippingService.updateInvoice(vendorId, coupangOrder, trackingNumber);

        // then
        verify(apiInstance, times(1)).applyInvoicesUpload(any(), anyString(), anyString());
        verify(response, times(2)).getCode();
        assertNotNull(actualResponse);
    }

    @Test
    @DisplayName("[쿠팡 송장 업데이트] 요청 실패 시 - 400 코드 (송장 번호 오류)")
    void applyPrepareShipmentStatusWithClientException() throws ApiException {
        // given
        Integer statusCode = 400;
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);
        UploadInvoiceDto.Request request = mock(UploadInvoiceDto.Request.class);
        CoupangOrder coupangOrder = mock(CoupangOrder.class);

        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.applyInvoicesUpload(any(), anyString(), anyString())).thenReturn(response);

        // when
        assertThrows(CoupangApiException.class, () -> coupangOrderShippingService.updateInvoice(vendorId, coupangOrder, trackingNumber));

        // then
        verify(apiInstance, times(1)).applyInvoicesUpload(any(), anyString(), anyString());
        verify(response, times(1)).getCode();
    }

    @Test
    @DisplayName("[쿠팡 송장 업데이트] 요청 실패 시 - 500 코드")
    void applyPrepareShipmentStatusWithServerException() throws ApiException, CoupangApiException {
        // given
        Integer statusCode = 500;
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);
        UploadInvoiceDto.Request request = mock(UploadInvoiceDto.Request.class);
        CoupangOrder coupangOrder = mock(CoupangOrder.class);

        when(response.getCode()).thenReturn(statusCode);

        when(apiInstance.applyInvoicesUpload(any(), anyString(), anyString())).thenReturn(response);

        // when
        PrepareShipmentDto.Response actualResponse = coupangOrderShippingService.updateInvoice(vendorId, coupangOrder, trackingNumber);

        // then
        verify(apiInstance, times(1)).applyInvoicesUpload(any(), anyString(), anyString());
        verify(response, times(2)).getCode();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[쿠팡 송장 업데이트] 요청 실패 시 - 반환 dto null")
    void applyPrepareShipmentStatusWithResponseException() throws ApiException, CoupangApiException {
        // given
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);
        UploadInvoiceDto.Request request = mock(UploadInvoiceDto.Request.class);
        CoupangOrder coupangOrder = mock(CoupangOrder.class);

        when(apiInstance.applyInvoicesUpload(any(), anyString(), anyString())).thenReturn(null);

        // when
        PrepareShipmentDto.Response actualResponse = coupangOrderShippingService.updateInvoice(vendorId, coupangOrder, trackingNumber);

        // then
        verify(apiInstance, times(1)).applyInvoicesUpload(any(), anyString(), anyString());
        verify(response, never()).getCode();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[쿠팡 송장 업데이트] 요청 실패 시 - CoupangMarketPlaceApi 예외")
    void applyPrepareShipmentStatusWithApiException() throws ApiException, CoupangApiException {
        // given
        PrepareShipmentDto.Response response = mock(PrepareShipmentDto.Response.class);
        UploadInvoiceDto.Request request = mock(UploadInvoiceDto.Request.class);
        CoupangOrder coupangOrder = mock(CoupangOrder.class);

        when(apiInstance.applyInvoicesUpload(any(), anyString(), anyString())).thenThrow(ApiException.class);

        // when
        PrepareShipmentDto.Response actualResponse = coupangOrderShippingService.updateInvoice(vendorId, coupangOrder, trackingNumber);

        // then
        verify(apiInstance, times(1)).applyInvoicesUpload(any(), anyString(), anyString());
        verify(response, never()).getCode();
        assertNull(actualResponse);
    }

}