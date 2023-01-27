package com.github.kingwaggs.ordermanager.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OhmyzipResponse {

    private String status;
    private String message;
    @JsonProperty("url")
    private String checkDeliveryStatusUrl;
    @JsonProperty("application_id")
    private String requestId;
    @JsonProperty("pre_way_bill")
    private String invoiceNumber;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("current_status")
    private String statusInEnglish;
    @JsonProperty("current_status_remark")
    private String shippingStatus;

}
