package com.github.kingwaggs.csmanager.service.inquiry;

import com.github.kingwaggs.csmanager.domain.Vendor;
import com.github.kingwaggs.csmanager.domain.dto.InquiryMessage;
import com.github.kingwaggs.csmanager.domain.entity.CustomerInquiry;
import com.github.kingwaggs.csmanager.domain.entity.CustomerInquiryAnswer;
import com.github.kingwaggs.csmanager.domain.result.CsManagerResultDto;
import com.github.kingwaggs.csmanager.domain.result.ErrorResultDto;
import com.github.kingwaggs.csmanager.domain.result.InquiryResultDto;
import com.github.kingwaggs.csmanager.exception.CoupangApiException;
import com.github.kingwaggs.csmanager.exception.NotAnsweredException;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangInquiries;
import com.github.kingwaggs.csmanager.sdk.coupang.exception.ApiException;
import com.github.kingwaggs.csmanager.util.DateTimeConverter;
import com.github.kingwaggs.csmanager.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryService {

    private static final String GET_INQUIRIES_MESSAGE = "`cs-manager`:: `getInquiries` 답변되지 않은 문의가 있습니다.\n%s";
    private static final String POST_ANSWER_MESSAGE = "`cs-manager`:: `answerInquiry` 문의 답변에 성공했습니다.\n%s";
    private static final String ERROR_MESSAGE = "`cs-manager`:: `ERROR` 다음과 같은 예외가 발생했습니다.\n%s";
    private static final Vendor COUPANG = Vendor.COUPANG;

    private final CustomerInquiryService customerInquiryService;
    private final CoupangInquiryService coupangInquiryService;
    private final SlackMessageUtil slackMessageUtil;

    public CsManagerResultDto getInquiries() {
        try {
            log.info("Getting inquiries which has not been answered start.");
            List<InquiryMessage> coupangInquiries = getCoupangInquiries();
            log.info("Getting inquiries which has not been answered complete.");
            return new InquiryResultDto(coupangInquiries, LocalDateTime.now());
        } catch (ApiException exception) {
            log.error("Exception occurred during get-inquiries.", exception);
            return new ErrorResultDto(exception, LocalDateTime.now());
        }
    }

    public void answerInquiry(String inquiryId, String answerContent) {
        try {
            log.info("Answering inquiry start. inquiry_id: {}, answer_content: {}", inquiryId, answerContent);
            CustomerInquiry customerInquiry = customerInquiryService.findCustomerInquiry(inquiryId);
            Vendor vendor = customerInquiry.getVendor();

            switch (vendor) {
                case COUPANG:
                    coupangInquiryService.answerInquiry(inquiryId, answerContent);
                    break;
                default:
                    throw new IllegalStateException();
            }
            saveAnswer(customerInquiry, answerContent);
            String message = String.format(POST_ANSWER_MESSAGE, customerInquiry);
            slackMessageUtil.sendProductMessage(message);
        } catch (ApiException exception) {
            log.error("Exception occurred during answering coupang inquiry.");
        } catch (CoupangApiException exception) {
            log.error("Exception occurred during answering coupang inquiry. " +
                    "Answer content might be the problem : {}", answerContent);
        }
    }

    public void saveAnsweredInquiries() {
        try {
            log.info("Saving inquiries which has been answered start.");
            saveCoupangInquiries();
            log.info("Saving inquiries which has been answered complete.");
        } catch (ApiException exception) {
            log.error("Exception occurred during getting coupang answered-inquiry to save.");
            String message = String.format(ERROR_MESSAGE, exception);
            slackMessageUtil.sendErrorMessage(message);
        } catch (NotAnsweredException exception) {
            log.error("Exception occurred during saving coupang answered-inquiry on repository.");
            String message = String.format(ERROR_MESSAGE, exception);
            slackMessageUtil.sendErrorMessage(message);
        }
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    private void checkInquiriesRegularly() {
        log.info("Scheduled job of getting inquiries start.");
        CsManagerResultDto resultDto = getInquiries();

        if (resultDto instanceof ErrorResultDto) {
            slackMessageUtil.sendErrorMessage("`cs-manager`:: `getInquiries` 수행 시 오류가 발생했습니다.");
            return;
        }
        log.info("Scheduled job of getting inquiries complete.");
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    private void saveInquiriesRegularly() {
        log.info("Scheduled job of saving answered inquiries start.");
        saveAnsweredInquiries();
    }

    private List<InquiryMessage> getCoupangInquiries() throws ApiException {
        List<InquiryMessage> resultList = new ArrayList<>();
        List<CoupangInquiries.Inquiry> inquiries = coupangInquiryService.getInquiries();
        for (CoupangInquiries.Inquiry inquiry : inquiries) {
            String inquiryId = inquiry.getInquiryId().toString();
            log.info("Unanswered inquiry received. inquiry_id: {}", inquiryId);
            InquiryMessage inquiryMessage = InquiryMessage.builder()
                    .vendor(COUPANG)
                    .inquiryId(inquiryId)
                    .content(inquiry.getContent())
                    .build();
            resultList.add(inquiryMessage);
            String message = String.format(GET_INQUIRIES_MESSAGE, inquiryMessage);
            slackMessageUtil.sendProductMessage(message);
        }
        return resultList;
    }

    private void saveCoupangInquiries() throws ApiException, NotAnsweredException {
        List<CoupangInquiries.Inquiry> answeredInquiries = coupangInquiryService.getAnsweredInquiries();
        for (CoupangInquiries.Inquiry answeredInquiry : answeredInquiries) {
            String inquiryId = answeredInquiry.getInquiryId().toString();
            LocalDateTime inquiryAt = DateTimeConverter.coupangDateTimeString2LocalDateTime(answeredInquiry.getInquiryAt());
            CoupangInquiries.InquiryAnswer inquiryAnswer = answeredInquiry.getAnswerList()
                    .stream().findAny()
                    .orElseThrow(NotAnsweredException::new);
            if (customerInquiryService.isRegisteredInquiry(inquiryId)) {
                log.info("Already saved coupang answered-inquiry. Moving on. inquiry_id: {}", inquiryId);
                continue;
            }
            CustomerInquiry inquiryEntity = CustomerInquiry.builder()
                    .vendor(COUPANG)
                    .inquiryId(inquiryId)
                    .content(answeredInquiry.getContent())
                    .productId(answeredInquiry.getProductId())
                    .customerId(answeredInquiry.getBuyerEmail())
                    .createdAt(inquiryAt)
                    .customerInquiryAnswer(new CustomerInquiryAnswer(inquiryAnswer.getContent(), LocalDateTime.now()))
                    .build();
            log.info("Save answered coupang inquiry. customer_inquiry: {}", inquiryEntity);
            customerInquiryService.saveCustomerInquiry(inquiryEntity);
        }
    }

    private void saveAnswer(CustomerInquiry customerInquiry, String answerContent) {
        CustomerInquiryAnswer inquiryAnswer = new CustomerInquiryAnswer(answerContent, LocalDateTime.now());
        customerInquiry.setInquiryAnswer(inquiryAnswer);
        customerInquiryService.saveCustomerInquiry(customerInquiry);
    }

}
