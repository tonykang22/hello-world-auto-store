package com.github.kingwaggs.ordermanager.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kingwaggs.ordermanager.domain.dto.response.ZincResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OhmyzipTrackingUpdateRequest implements DeliveryAgencyRequest {

    private String sender;
    private String authkey;
    @JsonProperty("warehouse_code")
    private final String warehouseCode;
    private final String email;
    private final Tracking tracking;
    private final List<Items> items;

    @Getter
    @Builder
    public static class Tracking {
        @JsonProperty("application_id")
        private final String requestId;
        @JsonProperty("order_no")
        private final String orderNumber;
        private final String merchant;
        @JsonProperty("tracking_no")
        private final String trackingNumber;
    }

    @Getter
    @Builder
    public static class Items {
        private final String name;
    }

    private static final String WAREHOUSE_CODE = "SECRET";
    private static final String OHMYZIP_ACCOUNT_ID = "SECRET";

    public static OhmyzipTrackingUpdateRequest create(String deliveryAgencyRequestId, String productName, ZincResponse zincOrder) {
        return OhmyzipTrackingUpdateRequest.builder()
                .warehouseCode(WAREHOUSE_CODE)
                .email(OHMYZIP_ACCOUNT_ID)
                .tracking(createTracking(deliveryAgencyRequestId, zincOrder))
                .items(List.of(createItems(productName)))
                .build();
    }

    private static final String AMAZON_URL = "www.amazon.com";

    private static Tracking createTracking(String requestId, ZincResponse zincOrder) {
        return Tracking.builder()
                .requestId(requestId)
                .orderNumber(zincOrder.getMerchantOrderId())
                .merchant(AMAZON_URL)
                .trackingNumber(zincOrder.getTrackingNumber())
                .build();
    }

    private static Items createItems(String productName) {
        return Items.builder()
                .name(productName)
                .build();
    }

    public void setAccountAuth(String sender, String authkey) {
        this.sender = sender;
        this.authkey = authkey;
    }
}
