package com.github.kingwaggs.ordermanager.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter @Setter
@ToString
@JsonIgnoreProperties
public class ZincResponse {

    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("_type")
    private String type;
    private String code;
    private String message;
    private Data data;
    @JsonProperty("merchant_order_ids")
    private List<MerchantOrderIds> merchantOrderIds;
    @JsonProperty("price_components")
    private PriceComponents priceComponents;
    private List<Tracking> tracking;



    @Getter
    @ToString
    public static class Data {
        @JsonProperty("validator_errors")
        private List<ValidatorErrors> validatorErrors;

        @ToString
        public static class ValidatorErrors {
            private String path;
            private String message;
        }
    }

    @Getter
    public static class MerchantOrderIds {
        @JsonProperty("merchant_order_id")
        private String merchantOrderId;
        private String merchant;
        private String account;
        @JsonProperty("placed_at")
        private String placedAt;
        private List<Integer> tracking;
        @JsonProperty("product_ids")
        private List<String> productIds;
        @JsonProperty("tracking_url")
        private String trackingUrl;
        @JsonProperty("delivery_date")
        private String deliveryDate;
    }

    @Getter
    public static class PriceComponents {

        private String currency;
        private Double shipping;
        private Double subtotal;
        private Double tax;
        private Double total;
        private String payment_currency;
        @JsonProperty("converted_payment_total")
        private Double convertedPaymentTotal;
    }

    @Getter
    public static class Tracking {
        @JsonProperty("merchant_order_id")
        private String merchantOrderId;
        private String carrier;
        @JsonProperty("tracking_number")
        private String trackingNumber;
        @JsonProperty("delivery_status")
        private String deliveryStatus;
        @JsonProperty("tracking_url")
        private String trackingUrl;
        @JsonProperty("product_ids")
        private List<String> productIds;
        @JsonProperty("retailer_tracking_number")
        private String retailerTrackingNumber;
        @JsonProperty("retailer_tracking_url")
        private String retailerTrackingUrl;
        @JsonProperty("obtained_at")
        private String obtainedAt;
    }

    public boolean isEmpty() {
        return getMerchantOrderId() == null || getRequestCompletedAt() == null || getCourier() == null || getTrackingNumber() == null;
    }

    private static final int HAS_ONLY_ONE_ITEM = 0;

    public String getMerchantOrderId() {
        return Optional.ofNullable(merchantOrderIds.get(HAS_ONLY_ONE_ITEM))
                .map(MerchantOrderIds::getMerchantOrderId)
                .orElse(null);
    }

    public String getRequestCompletedAt() {
        return Optional.ofNullable(merchantOrderIds.get(HAS_ONLY_ONE_ITEM))
                .map(MerchantOrderIds::getPlacedAt)
                .orElse(null);
    }

    public String getCourier() {
        return Optional.ofNullable(tracking.get(HAS_ONLY_ONE_ITEM))
                .map(Tracking::getCarrier)
                .orElse(null);
    }

    public String getTrackingNumber() {
        return Optional.ofNullable(tracking.get(HAS_ONLY_ONE_ITEM))
                .map(Tracking::getTrackingNumber)
                .orElse(null);
    }

    public String getAsin() {
        return Optional.ofNullable(merchantOrderIds.get(HAS_ONLY_ONE_ITEM))
                .map(merchantOrderIds -> merchantOrderIds.getProductIds().get(HAS_ONLY_ONE_ITEM))
                .orElse(null);
    }
}
