package com.github.kingwaggs.csmanager.service.inquiry;

import com.github.kingwaggs.csmanager.exception.CoupangApiException;
import com.github.kingwaggs.csmanager.sdk.coupang.config.CoupangVendorConfig;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangInquiries;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangInquiryAnswer;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangResponse;
import com.github.kingwaggs.csmanager.sdk.coupang.exception.ApiException;
import com.github.kingwaggs.csmanager.sdk.coupang.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.csmanager.util.DateTimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangInquiryService {

    private static final String NOT_ANSWERED = "NOANSWER";
    private static final String ANSWERED = "ANSWERED";
    private static final Long ONE_DAY = 1L;
    private static final Integer SUCCESS_CODE = 200;
    private static final int MAX_PER_PAGE = 50;

    private final CoupangMarketPlaceApi apiInstance;
    private final CoupangVendorConfig vendorConfig;

    public List<CoupangInquiries.Inquiry> getInquiries() throws ApiException {
        log.info("Getting coupang inquiries start.");
        CoupangInquiries unansweredInquiries = getInquiriesType(NOT_ANSWERED);
        log.info("Getting coupang inquiries complete.");
        return unansweredInquiries.getInquiryList();
    }

    public List<CoupangInquiries.Inquiry> getAnsweredInquiries() throws ApiException {
        log.info("Getting coupang answered-inquiries start.");
        CoupangInquiries answeredInquiries = getInquiriesType(ANSWERED);
        log.info("Getting coupang answered-inquiries complete.");
        return answeredInquiries.getInquiryList();
    }

    public void answerInquiry(String inquiryId, String answerContent) throws ApiException, CoupangApiException {
        log.info("Answering coupang inquiry start. inquiry_id: {}, answer_content: {}", inquiryId, answerContent);
        CoupangInquiryAnswer answerDto = CoupangInquiryAnswer.builder()
                .content(answerContent)
                .vendorUserId(vendorConfig.getVendorUserId())
                .vendorId(vendorConfig.getVendorId())
                .build();

        CoupangResponse coupangResponse = apiInstance.addAnswerToInquiry(inquiryId, vendorConfig.getVendorId(), answerDto);
        Integer responseCode = coupangResponse.getCode();
        if (!responseCode.equals(SUCCESS_CODE) ) {
            log.info("Exception occurred during answering coupang inquiry. " +
                    "ResponseCode: {}, inquiry_id: {}, answer_content: {}", responseCode, inquiryId, answerContent);
            throw new CoupangApiException();
        }
        log.info("Answering coupang inquiry complete. inquiry_id: {}, answer_content: {}", inquiryId, answerContent);
    }

    private CoupangInquiries getInquiriesType(String answerType) throws ApiException {
        String vendorId = vendorConfig.getVendorId();
        LocalDate today = LocalDate.now();
        String todayInString = DateTimeConverter.localDate2DateString(today);
        String yesterdayInString = DateTimeConverter.localDate2DateString(today.minusDays(ONE_DAY));
        return apiInstance.getCustomerInquiries(vendorId, yesterdayInString, todayInString, answerType, MAX_PER_PAGE);
    }


}
