package com.github.kingwaggs.csmanager.service.inquiry;

import com.github.kingwaggs.csmanager.domain.entity.CustomerInquiry;
import com.github.kingwaggs.csmanager.repository.CustomerInquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerInquiryService {

    private final CustomerInquiryRepository inquiryRepository;

    public void saveCustomerInquiry(CustomerInquiry customerInquiry) {
        log.info("Save customer inquiry in repository. CustomerInquiry: {}", customerInquiry);
        inquiryRepository.save(customerInquiry);
    }

    public CustomerInquiry findCustomerInquiry(String inquiryId) {
        log.info("Find customer inquiry in repository. inquiry_id: {}", inquiryId);
        return inquiryRepository.findByInquiryId(inquiryId)
                .orElseThrow(NoSuchElementException::new);
    }

    public boolean isRegisteredInquiry(String inquiryId) {
        log.info("Check customer inquiry is registered in repository. inquiry_id: {}", inquiryId);
        return inquiryRepository.existsByInquiryId(inquiryId);
    }

}
