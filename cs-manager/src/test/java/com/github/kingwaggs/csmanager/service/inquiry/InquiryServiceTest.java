package com.github.kingwaggs.csmanager.service.inquiry;

import com.github.kingwaggs.csmanager.domain.Vendor;
import com.github.kingwaggs.csmanager.domain.entity.CustomerInquiry;
import com.github.kingwaggs.csmanager.domain.result.CsManagerResultDto;
import com.github.kingwaggs.csmanager.domain.result.ErrorResultDto;
import com.github.kingwaggs.csmanager.domain.result.InquiryResultDto;
import com.github.kingwaggs.csmanager.exception.CoupangApiException;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangInquiries;
import com.github.kingwaggs.csmanager.sdk.coupang.exception.ApiException;
import com.github.kingwaggs.csmanager.util.SlackMessageUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

    private static final Long INQUIRY_ID = 12345L;
    private static final String ANSWER_CONTENT = "감사합니다. 고객님.";

    @InjectMocks
    private InquiryService inquiryService;

    @Mock
    private CustomerInquiryService customerInquiryService;
    @Mock
    private CoupangInquiryService coupangInquiryService;
    @Mock
    private SlackMessageUtil slackMessageUtil;

    @Test
    void getInquiriesSuccessfully() throws ApiException {
        // given
        CoupangInquiries.Inquiry mockInquiry = mock(CoupangInquiries.Inquiry.class);
        when(mockInquiry.getInquiryId()).thenReturn(INQUIRY_ID);
        when(mockInquiry.getContent()).thenReturn("재고 있나요?");

        when(coupangInquiryService.getInquiries()).thenReturn(List.of(mockInquiry));

        // when
        CsManagerResultDto inquiries = inquiryService.getInquiries();

        // then
        assertTrue(inquiries instanceof InquiryResultDto);
        verify(slackMessageUtil, times(1)).sendProductMessage(anyString());
    }

    @Test
    void getInquiriesSuccessfullyWithDuplicateInquiry() throws ApiException {
        // given
        CoupangInquiries.Inquiry mockInquiry = mock(CoupangInquiries.Inquiry.class);
        when(mockInquiry.getInquiryId()).thenReturn(INQUIRY_ID);

        when(coupangInquiryService.getInquiries()).thenReturn(List.of(mockInquiry));

        // when
        CsManagerResultDto inquiries = inquiryService.getInquiries();

        // then
        assertTrue(inquiries instanceof InquiryResultDto);
        verify(slackMessageUtil, times(1)).sendProductMessage(anyString());
    }

    @Test
    void getInquiriesWithCoupangException() throws ApiException {
        // given
        when(coupangInquiryService.getInquiries()).thenThrow(ApiException.class);

        // when
        CsManagerResultDto inquiries = inquiryService.getInquiries();

        // then
        assertTrue(inquiries instanceof ErrorResultDto);
        verify(slackMessageUtil, never()).sendProductMessage(anyString());
    }

    @Test
    void answerInquirySuccessfully() throws CoupangApiException, ApiException {
        // given
        CustomerInquiry mockCustomInquiry = mock(CustomerInquiry.class);
        when(mockCustomInquiry.getVendor()).thenReturn(Vendor.COUPANG);

        when(customerInquiryService.findCustomerInquiry(anyString())).thenReturn(mockCustomInquiry);

        // when
        inquiryService.answerInquiry(INQUIRY_ID.toString(), ANSWER_CONTENT);

        // then
        verify(coupangInquiryService, times(1)).answerInquiry(anyString(), anyString());
        verify(customerInquiryService, times(1)).saveCustomerInquiry(any());
        verify(slackMessageUtil, times(1)).sendProductMessage(anyString());
    }

    @Test
    void answerInquiryWithIllegalStateException() throws CoupangApiException, ApiException {
        // given
        CustomerInquiry mockCustomInquiry = mock(CustomerInquiry.class);
        when(mockCustomInquiry.getVendor()).thenReturn(Vendor.NAVER);

        when(customerInquiryService.findCustomerInquiry(anyString())).thenReturn(mockCustomInquiry);
        String inquiryId = INQUIRY_ID.toString();

        // when
        assertThrows(IllegalStateException.class, () -> inquiryService.answerInquiry(inquiryId, ANSWER_CONTENT));

        // then
        verify(coupangInquiryService, never()).answerInquiry(anyString(), anyString());
        verify(customerInquiryService, never()).saveCustomerInquiry(any());
        verify(slackMessageUtil, never()).sendProductMessage(anyString());
    }

    @ParameterizedTest
    @ValueSource(classes = {ApiException.class, CoupangApiException.class})
    void answerInquiryWithCoupangApiException(Class<Exception> exception) throws CoupangApiException, ApiException {
        // given
        CustomerInquiry mockCustomInquiry = mock(CustomerInquiry.class);
        when(mockCustomInquiry.getVendor()).thenReturn(Vendor.COUPANG);

        when(customerInquiryService.findCustomerInquiry(anyString())).thenReturn(mockCustomInquiry);
        doThrow(exception).when(coupangInquiryService).answerInquiry(anyString(), anyString());


        String inquiryId = INQUIRY_ID.toString();

        // when
        inquiryService.answerInquiry(inquiryId, ANSWER_CONTENT);

        // then
        verify(coupangInquiryService, times(1)).answerInquiry(anyString(), anyString());
        verify(customerInquiryService, never()).saveCustomerInquiry(any());
        verify(slackMessageUtil, never()).sendProductMessage(anyString());
    }

}