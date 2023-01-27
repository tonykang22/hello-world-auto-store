package com.github.kingwaggs.csmanager.domain.dto;

import com.github.kingwaggs.csmanager.domain.Vendor;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class InquiryMessage {

    private Vendor vendor;
    private String inquiryId;
    private String content;

}

