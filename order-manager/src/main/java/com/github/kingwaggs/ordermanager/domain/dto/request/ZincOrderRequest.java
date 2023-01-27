package com.github.kingwaggs.ordermanager.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kingwaggs.ordermanager.domain.ZincCredential;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ZincOrderRequest {

    private final String retailer;
    private final List<Product> products;
    @JsonProperty("shipping_address")
    private final ShippingAddress shippingAddress;
    @JsonProperty("shipping_method")
    private final String shippingMethod;
    @JsonProperty("billing_address")
    private final BillingAddress billingAddress;
    @JsonProperty("payment_method")
    private final PaymentMethod paymentMethod;
    @JsonProperty("retailer_credentials")
    private final RetailerCredentials retailerCredentials;
    private final Webhooks webhooks;
    @JsonProperty("is_gift")
    private final boolean isGift;
    @JsonProperty("max_price")
    private final int maxPrice;

    @Data
    @Builder
    public static class Product {
        @JsonProperty("product_id")
        private final String productId;
        private final int quantity;
        @JsonProperty("seller_selection_criteria")
        private final SellerSelection sellerSelection;
    }

    @Data
    public static class SellerSelection {
        @JsonProperty("handling_days_max")
        private final int maxDeliveryDay;
    }

    @Data
    @Builder
    public static class ShippingAddress {
        @JsonProperty("first_name")
        private final String firstName;
        @JsonProperty("last_name")
        private final String lastName;
        @JsonProperty("address_line1")
        private final String addressLine1;
        @JsonProperty("address_line2")
        private final String addressLine2;
        @JsonProperty("zip_code")
        private final String zipCode;
        private final String city;
        private final String state;
        private final String country;
        @JsonProperty("phone_number")
        private final String phoneNumber;
        private final String instructions;
    }

    @Data
    @Builder
    public static class BillingAddress {
        @JsonProperty("first_name")
        private final String firstName;
        @JsonProperty("last_name")
        private final String lastName;
        @JsonProperty("address_line1")
        private final String addressLine1;
        @JsonProperty("address_line2")
        private final String addressLine2;
        @JsonProperty("zip_code")
        private final String zipCode;
        private final String city;
        private final String state;
        private final String country;
        @JsonProperty("phone_number")
        private final String phoneNumber;
        private final String instructions;
    }

    @Data
    @Builder
    public static class PaymentMethod {
        @JsonProperty("use_gift")
        private final boolean useGift;
    }

    @Data
    @Builder
    public static class RetailerCredentials {
        private final String email;
        private final String password;
    }

    @Data
    @Builder
    public static class Webhooks {
        @JsonProperty("request_succeeded")
        private final String requestSucceeded;
        @JsonProperty("request_failed")
        private final String requestFailed;
        @JsonProperty("tracking_obtained")
        private final String trackingObtained;
    }

    private static final String RETAILER = "amazon";
    private static final String SHIPPING_POLICY_CHEAPEST_PRICE = "cheapest";
    private static final boolean REMOVE_RECEIPT = true;
    private static final boolean USE_GIFT_POINT = true;

    public static ZincOrderRequest create(String asin, Double price, ZincCredential zincCredential) {
        return ZincOrderRequest.builder()
                .retailer(RETAILER)
                .products(new ArrayList<>(List.of(createProduct(asin))))
                .shippingAddress(createShippingAddress(zincCredential))
                .shippingMethod(SHIPPING_POLICY_CHEAPEST_PRICE)
                .billingAddress(createBillingAddress(zincCredential))
                .paymentMethod(new PaymentMethod(USE_GIFT_POINT))
                .retailerCredentials(createRetailerCredentials(zincCredential))
                .webhooks(createWebhooks(zincCredential))
                .isGift(REMOVE_RECEIPT)
                .maxPrice((int) Math.round((price * 110)))
                .build();

    }

    private static final int DEFAULT_QUANTITY = 1;
    private static final int MAX_DELIVERY_DAYS = 10;

    private static ZincOrderRequest.Product createProduct(String asin) {
        return ZincOrderRequest.Product.builder()
                .productId(asin)
                .quantity(DEFAULT_QUANTITY)
                .sellerSelection(new SellerSelection(MAX_DELIVERY_DAYS))
                .build();
    }

    private static ZincOrderRequest.RetailerCredentials createRetailerCredentials(ZincCredential zincCredential) {
        return ZincOrderRequest.RetailerCredentials.builder()
                .email(zincCredential.getRetailerId())
                .password(zincCredential.getRetailerPassword())
                .build();
    }

    private static ZincOrderRequest.ShippingAddress createShippingAddress(ZincCredential zincCredential) {
        return ShippingAddress.builder()
                .firstName(zincCredential.getFirstName())
                .lastName(zincCredential.getLastName())
                .addressLine1(zincCredential.getAddressLine1())
                .addressLine2(zincCredential.getAddressLine2())
                .zipCode(zincCredential.getZipcode())
                .city(zincCredential.getCity())
                .state(zincCredential.getState())
                .country(zincCredential.getCountry())
                .phoneNumber(zincCredential.getPhoneNumber())
                .instructions(null)
                .build();
    }

    private static ZincOrderRequest.BillingAddress createBillingAddress(ZincCredential zincCredential) {
        return BillingAddress.builder()
                .firstName(zincCredential.getFirstName())
                .lastName(zincCredential.getLastName())
                .addressLine1(zincCredential.getAddressLine1())
                .addressLine2(zincCredential.getAddressLine2())
                .zipCode(zincCredential.getZipcode())
                .city(zincCredential.getCity())
                .state(zincCredential.getState())
                .country(zincCredential.getCountry())
                .phoneNumber(zincCredential.getPhoneNumber())
                .instructions(null)
                .build();
    }

    private static ZincOrderRequest.Webhooks createWebhooks(ZincCredential zincCredential) {
        return Webhooks.builder()
                .requestSucceeded(zincCredential.getSuccessWebhookUrl())
                .requestFailed(zincCredential.getFailWebhookUrl())
                .trackingObtained(zincCredential.getTrackingWebhookUrl())
                .build();
    }
}
