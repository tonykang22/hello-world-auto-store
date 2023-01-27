package com.github.kingwaggs.csmanager.repository;

import com.github.kingwaggs.csmanager.domain.entity.CustomerInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerInquiryRepository extends JpaRepository<CustomerInquiry, Long> {

    Optional<CustomerInquiry> findByInquiryId(String inquiryId);

    boolean existsByInquiryId(String inquiryId);

}
