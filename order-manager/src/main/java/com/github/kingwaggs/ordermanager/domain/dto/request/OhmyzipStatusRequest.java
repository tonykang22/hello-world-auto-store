package com.github.kingwaggs.ordermanager.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OhmyzipStatusRequest implements DeliveryAgencyRequest {

    private final String sender;
    private final String authkey;
    @JsonProperty("warehouse_code")
    private final String warehouseCode;
    private final String email;
    @JsonProperty("application_id")
    private final String requestId;


    private static final String WAREHOUSE_CODE = "SECRET";
    private static final String OHMYZIP_ACCOUNT_ID = "SECRET";

    public static OhmyzipStatusRequest create(String ohmyzipSender, String ohmyzipApiKey, String requestId) {
        return OhmyzipStatusRequest.builder()
                .sender(ohmyzipSender)
                .authkey(ohmyzipApiKey)
                .warehouseCode(WAREHOUSE_CODE)
                .email(OHMYZIP_ACCOUNT_ID)
                .requestId(requestId)
                .build();
    }
}
