package com.github.kingwaggs.csmanager.sdk.coupang.domain.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoupangInquiryAnswer {

    private String content;
    @SerializedName("replyBy")
    private String vendorUserId;
    private String vendorId;

}
