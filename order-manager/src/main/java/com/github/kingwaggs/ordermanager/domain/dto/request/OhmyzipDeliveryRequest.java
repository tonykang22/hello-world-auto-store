package com.github.kingwaggs.ordermanager.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kingwaggs.ordermanager.domain.dto.response.ZincResponse;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OhmyzipDeliveryRequest implements DeliveryAgencyRequest {

    private String sender;
    private String authkey;
    @JsonProperty("warehouse_code")
    private final String warehouseCode;
    private final String email;
    private final CustomerInformation options;
    private final List<ProductInformation> items;


    @Getter
    @Builder
    public static class CustomerInformation {
        @JsonProperty("name_ko")
        private final String nameInKorean;
        @JsonProperty("customs_number")
        private final String customsNumber;
        @JsonProperty("phone")
        private final String phoneNumber;
        private final String address1;
        private final String address2;
        @JsonProperty("address_zip")
        private final String addressZip;
        @JsonProperty("ship_method_cd")
        private final String shipMethod;
        @JsonProperty("autopay_method_cd")
        private final String autoPaymentMethod;
        @JsonProperty("sms_target_cd")
        private final String smsMessageTarget;
        @JsonProperty("ohfast_yn")
        private final String ohmyzipFastTrackingNumber;
    }

    @Getter
    @Builder
    public static class ProductInformation {
        private final String merchant;
        private final String name;
        private final Integer quantity;
        private final Double price;
        private final Integer category;
        @JsonProperty("order_no")
        private final String orderNumber;
    }

    private static final String WAREHOUSE_CODE = "SECRET";
    private static final String OHMYZIP_ACCOUNT_ID = "SECRET";

    public static OhmyzipDeliveryRequest create(CoupangOrder order,
                                                ZincResponse zincOrder,
                                                HelloWorldAutoStoreProduct hwasProduct,
                                                String productName) {


        OhmyzipDeliveryRequest.CustomerInformation customerInformation = createCustomerInformation(order);

        OhmyzipDeliveryRequest.ProductInformation amazonProductInformation =
                createProductInformation(hwasProduct, zincOrder, productName);

        return OhmyzipDeliveryRequest.builder()
                .warehouseCode(WAREHOUSE_CODE)
                .email(OHMYZIP_ACCOUNT_ID)
                .options(customerInformation)
                .items(List.of(amazonProductInformation))
                .build();
    }

    private static final String DEFAULT_SHIPPING_METHOD = "AIR";
    private static final String AUTO_PAYMENT_VIA_POINTS = "T";
    private static final String SENDER_GET_MESSAGE = "S";
    private static final String OHMYZIP_FAST_TRACKING_NUMBER_SERVICE = "Y";

    private static OhmyzipDeliveryRequest.CustomerInformation createCustomerInformation(CoupangOrder order) {
        return order.getReceiver().getAddress2() != null ?
                OhmyzipDeliveryRequest.CustomerInformation.builder()
                        .nameInKorean(order.getReceiver().getName())
                        .customsNumber(order.getOverseaShippingInfo().getPersonalCustomsClearanceCode())
                        .phoneNumber(order.getPhoneNumber())
                        .address1(order.getReceiver().getAddress1())
                        .address2(order.getReceiver().getAddress2())
                        .addressZip(order.getReceiver().getPostCode())
                        .shipMethod(DEFAULT_SHIPPING_METHOD)
                        .autoPaymentMethod(AUTO_PAYMENT_VIA_POINTS)
                        .smsMessageTarget(SENDER_GET_MESSAGE)
                        .ohmyzipFastTrackingNumber(OHMYZIP_FAST_TRACKING_NUMBER_SERVICE)
                        .build()
                :
                OhmyzipDeliveryRequest.CustomerInformation.builder()
                        .nameInKorean(order.getReceiver().getName())
                        .customsNumber(order.getOverseaShippingInfo().getPersonalCustomsClearanceCode())
                        .phoneNumber(order.getPhoneNumber())
                        .address1(order.getReceiver().getAddress1())
                        .addressZip(order.getReceiver().getPostCode())
                        .shipMethod(DEFAULT_SHIPPING_METHOD)
                        .autoPaymentMethod(AUTO_PAYMENT_VIA_POINTS)
                        .smsMessageTarget(SENDER_GET_MESSAGE)
                        .ohmyzipFastTrackingNumber(OHMYZIP_FAST_TRACKING_NUMBER_SERVICE)
                        .build();
    }

    private static final String AMAZON_URL = "www.amazon.com";
    private static final Integer DEFAULT_PRODUCT_QUANTITY = 1;

    private static OhmyzipDeliveryRequest.ProductInformation createProductInformation(HelloWorldAutoStoreProduct hwasProduct,
                                                                                      ZincResponse zincOrder,
                                                                                      String productName) {
        return OhmyzipDeliveryRequest.ProductInformation.builder()
                .merchant(AMAZON_URL)
                .name(productName)
                .quantity(DEFAULT_PRODUCT_QUANTITY)
                .price(hwasProduct.getSourceOriginalPrice())
                .category(95)
                .orderNumber(zincOrder.getMerchantOrderId())
                .build();
    }

    public void setAccountAuth(String sender, String authkey) {
        this.sender = sender;
        this.authkey = authkey;
    }

}
