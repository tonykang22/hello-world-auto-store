package com.github.kingwaggs.ordermanager.service;

import com.github.kingwaggs.ordermanager.domain.dto.request.OhmyzipDeliveryRequest;
import com.github.kingwaggs.ordermanager.domain.dto.request.OhmyzipTrackingUpdateRequest;
import com.github.kingwaggs.ordermanager.domain.dto.response.OhmyzipResponse;
import com.github.kingwaggs.ordermanager.exception.OhmyzipApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "ohmyzip.API.sender=testSender",
        "ohmyzip.API.key=testKey"
})
class OhmyzipServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OhmyzipService ohmyzipService;

    private static final String orderId = "123456789";
    private static final String requestId = "987654321";

    @Test
    @DisplayName("[배송 대행 요청] 성공 시")
    void requestDeliverAgencySuccessfully() throws OhmyzipApiException {
        // given
        OhmyzipDeliveryRequest request = mock(OhmyzipDeliveryRequest.class);
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        ResponseEntity<OhmyzipResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OhmyzipResponse.class)))
                .thenReturn(responseEntity);
        when(response.getStatus()).thenReturn("success");

        // when
        OhmyzipResponse actualResponse = ohmyzipService.requestDeliverAgency(request, orderId);

        // then
        verify(response, times(1)).getStatus();
        assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("[배송 대행 요청] 실패 시 - Response Entity 예외")
    void requestDeliverAgencyWithResponseException() throws OhmyzipApiException {
        // given
        OhmyzipDeliveryRequest request = mock(OhmyzipDeliveryRequest.class);
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OhmyzipResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        OhmyzipResponse actualResponse = ohmyzipService.requestDeliverAgency(request, orderId);

        // then
        verify(response, never()).getStatus();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[배송 대행 요청] 실패 시 - Response body null 예외")
    void requestDeliverAgencyWithResponseNullException() throws OhmyzipApiException {
        // given
        OhmyzipDeliveryRequest request = mock(OhmyzipDeliveryRequest.class);
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        ResponseEntity<OhmyzipResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OhmyzipResponse.class)))
                .thenReturn(responseEntity);

        // when
        OhmyzipResponse actualResponse = ohmyzipService.requestDeliverAgency(request, orderId);

        // then
        verify(response, never()).getStatus();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[배송 대행 요청] 실패 시 - 반환 [status = error]")
    void requestDeliverAgencyWithResponseStatusException() {
        // given
        OhmyzipDeliveryRequest request = mock(OhmyzipDeliveryRequest.class);
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        ResponseEntity<OhmyzipResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OhmyzipResponse.class)))
                .thenReturn(responseEntity);
        when(response.getStatus()).thenReturn("error");

        // when
        assertThrows(OhmyzipApiException.class, () -> ohmyzipService.requestDeliverAgency(request, orderId));

        // then
        verify(response, times(1)).getStatus();
    }

    @Test
    @DisplayName("[송장 업데이트 요청] 성공 시")
    void updateInvoiceSuccessfully() throws OhmyzipApiException {
        // given
        OhmyzipTrackingUpdateRequest request = mock(OhmyzipTrackingUpdateRequest.class);
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        ResponseEntity<OhmyzipResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OhmyzipResponse.class)))
                .thenReturn(responseEntity);
        when(response.getStatus()).thenReturn("success");

        // when
        OhmyzipResponse actualResponse = ohmyzipService.updateInvoice(request, orderId);

        // then
        verify(response, times(1)).getStatus();
        assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("[송장 업데이트 요청] 실패 시 - Response Entity 예외")
    void updateInvoiceWithResponseException() throws OhmyzipApiException {
        // given
        OhmyzipTrackingUpdateRequest request = mock(OhmyzipTrackingUpdateRequest.class);
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OhmyzipResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        OhmyzipResponse actualResponse = ohmyzipService.updateInvoice(request, orderId);

        // then
        verify(response, never()).getStatus();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[송장 업데이트 요청] 실패 시 - Response body null 예외")
    void updateInvoiceWithResponseNullException() throws OhmyzipApiException {
        // given
        OhmyzipTrackingUpdateRequest request = mock(OhmyzipTrackingUpdateRequest.class);
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        ResponseEntity<OhmyzipResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OhmyzipResponse.class)))
                .thenReturn(responseEntity);

        // when
        OhmyzipResponse actualResponse = ohmyzipService.updateInvoice(request, orderId);

        // then
        verify(response, never()).getStatus();
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[송장 업데이트 요청] 실패 시 - 반환 [status = error]")
    void updateInvoiceWithResponseStatusException() {
        // given
        OhmyzipTrackingUpdateRequest request = mock(OhmyzipTrackingUpdateRequest.class);
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        ResponseEntity<OhmyzipResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(OhmyzipResponse.class)))
                .thenReturn(responseEntity);
        when(response.getStatus()).thenReturn("error");

        // when
        assertThrows(OhmyzipApiException.class, () -> ohmyzipService.updateInvoice(request, orderId));

        // then
        verify(response, times(1)).getStatus();
    }

    @Test
    @DisplayName("[배송 상태 조회] 성공 시")
    void getShippingStatusSuccessfully() {
        // given
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        ResponseEntity<OhmyzipResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(OhmyzipResponse.class)))
                .thenReturn(responseEntity);
        when(response.getShippingStatus()).thenReturn("입고완료");

        // when
        String status = ohmyzipService.getShippingStatus(requestId, orderId);

        // then
        verify(response, times(2)).getShippingStatus();
        assertEquals("입고완료", status);
    }

    @Test
    @DisplayName("[배송 상태 조회] 실패 시 - Response Entity 예외")
    void getShippingStatusResponseException() {
        // given
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(OhmyzipResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        String status = ohmyzipService.getShippingStatus(requestId, orderId);

        // then
        verify(response, never()).getShippingStatus();
        assertNull(status);
    }

    @Test
    @DisplayName("[배송 상태 조회] 실패 시 - Response body null 예외")
    void getShippingStatusResponseNullException() {
        // given
        OhmyzipResponse response = mock(OhmyzipResponse.class);

        ResponseEntity<OhmyzipResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(OhmyzipResponse.class)))
                .thenReturn(responseEntity);

        // when
        String status = ohmyzipService.getShippingStatus(requestId, orderId);

        // then
        verify(response, never()).getShippingStatus();
        assertNull(status);
    }

}