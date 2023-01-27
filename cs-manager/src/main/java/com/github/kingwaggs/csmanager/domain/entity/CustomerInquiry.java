package com.github.kingwaggs.csmanager.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.csmanager.domain.Vendor;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "customer_inquiry")
@ToString
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInquiry {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Vendor vendor;

    @Column(name = "inquiry_id", nullable = false)
    private String inquiryId;

    @Column(nullable = false)
    private String content;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Embedded
    private CustomerInquiryAnswer customerInquiryAnswer;

    public void setInquiryAnswer(CustomerInquiryAnswer customerInquiryAnswer) {
        this.customerInquiryAnswer = customerInquiryAnswer;
    }
}
