package com.github.kingwaggs.csmanager.service.inquiry;

import com.github.kingwaggs.csmanager.exception.CoupangApiException;
import com.github.kingwaggs.csmanager.sdk.coupang.config.CoupangVendorConfig;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangInquiries;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangResponse;
import com.github.kingwaggs.csmanager.sdk.coupang.exception.ApiException;
import com.github.kingwaggs.csmanager.sdk.coupang.service.CoupangMarketPlaceApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoupangInquiryServiceTest {

    private static final Integer SUCCESS_CODE = 200;
    private static final Integer FAIL_CODE = 400;
    private static final String VENDOR_ID = "AA11032";
    private static final String ANSWER = "감사합니다. 고객님.";
    private static final String INQUIRY_ID = "12345";

    @InjectMocks
    private CoupangInquiryService coupangInquiryService;

    @Mock
    private CoupangMarketPlaceApi apiInstance;
    @Mock
    private CoupangVendorConfig vendorConfig;

    @Test
    void getInquiriesSuccessfully() throws ApiException {
        // given
        CoupangInquiries mockResponse = mock(CoupangInquiries.class);
        CoupangInquiries.Inquiry mockInquiry = mock(CoupangInquiries.Inquiry.class);
        when(mockResponse.getInquiryList()).thenReturn(List.of(mockInquiry));

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(apiInstance.getCustomerInquiries(eq(VENDOR_ID), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(mockResponse);

        // when
        List<CoupangInquiries.Inquiry> inquiries = coupangInquiryService.getInquiries();

        // then
        assertTrue(inquiries.contains(mockInquiry));
        verify(apiInstance, times(1))
                .getCustomerInquiries(anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void getInquiriesWithException() throws ApiException {
        // given
        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(apiInstance.getCustomerInquiries(eq(VENDOR_ID), anyString(), anyString(), anyString(), anyInt()))
                .thenThrow(ApiException.class);

        // when

        // then
        assertThrows(ApiException.class, () -> coupangInquiryService.getInquiries());
        verify(apiInstance, times(1))
                .getCustomerInquiries(anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void answerInquirySuccessfully() throws ApiException, CoupangApiException {
        // given
        CoupangResponse mockResponse = mock(CoupangResponse.class);
        when(mockResponse.getCode()).thenReturn(SUCCESS_CODE);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(apiInstance.addAnswerToInquiry(anyString(), anyString(), any())).thenReturn(mockResponse);

        // when
        coupangInquiryService.answerInquiry(INQUIRY_ID, ANSWER);

        // then
        verify(apiInstance, times(1)).addAnswerToInquiry(anyString(), anyString(), any());
    }

    @Test
    void answerInquiryWithException() throws ApiException {
        // given
        CoupangResponse mockResponse = mock(CoupangResponse.class);
        when(mockResponse.getCode()).thenReturn(FAIL_CODE);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(apiInstance.addAnswerToInquiry(anyString(), anyString(), any())).thenReturn(mockResponse);

        // when
        assertThrows(CoupangApiException.class, () -> coupangInquiryService.answerInquiry(INQUIRY_ID, ANSWER));

        // then
        verify(apiInstance, times(1)).addAnswerToInquiry(anyString(), anyString(), any());
    }

}