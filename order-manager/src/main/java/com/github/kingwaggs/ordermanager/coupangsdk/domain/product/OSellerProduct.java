package com.github.kingwaggs.ordermanager.coupangsdk.domain.product;


import lombok.Getter;

import java.util.List;

@Getter
public class OSellerProduct {

    private String vendorUserId;
    private String vendorId;
    private String displayProductName;
    private String generalProductName;
    private String productGroup;
    private OSellerProduct.UnionDeliveryTypeEnum unionDeliveryType;
    private String statusName;
    private String sellerProductName;
    private Long sellerProductId;
    private String saleStartedAt;
    private String saleEndedAt;
    private String returnZipCode;
    private OSellerProduct.ReturnChargeVendorEnum returnChargeVendor;
    private String returnChargeName;
    private Double returnCharge;
    private String returnCenterCode;
    private String returnAddressDetail;
    private String returnAddress;
    private List<OSellerProductRequiredDocument> requiredDocuments;
    private Boolean requested;
    private Long productId;
    private Long outboundShippingPlaceCode;
    private String mdName;
    private String mdId;
    private String manufacture;
    private String extraInfoMessage;
    private List<OSellerProductItem> items;
    private Double freeShipOverAmount;
    private String exchangeType;
    private Long displayCategoryCode;
    private Double deliverySurcharge;
    private OSellerProduct.DeliveryMethodEnum deliveryMethod;
    private String deliveryCompanyCode;
    private OSellerProduct.DeliveryChargeTypeEnum deliveryChargeType;
    private Double deliveryChargeOnReturn;
    private Double deliveryCharge;
    private String contributorType;
    private String companyContactNumber;
    private Long categoryId;
    private Integer bundlePackingDelivery;
    private String brand;
    private String afterServiceInformation;
    private String afterServiceContactNumber;
    private OSellerProduct.RemoteAreaDeliverableEnum remoteAreaDeliverable;


    public enum RemoteAreaDeliverableEnum {
        Y("Y"),
        N("N");

        private String value;

        private RemoteAreaDeliverableEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum DeliveryChargeTypeEnum {
        FREE("FREE"),
        NOT_FREE("NOT_FREE"),
        CHARGE_RECEIVED("CHARGE_RECEIVED"),
        CONDITIONAL_FREE("CONDITIONAL_FREE"),
        FREE_DELIVERY_OVER_9800("FREE_DELIVERY_OVER_9800");

        private String value;

        DeliveryChargeTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum DeliveryMethodEnum {
        SEQUENCIAL("SEQUENCIAL"),
        VENDOR_DIRECT("VENDOR_DIRECT"),
        MAKE_ORDER("MAKE_ORDER"),
        INSTRUCTURE("INSTRUCTURE"),
        AGENT_BUY("AGENT_BUY"),
        COLD_FRESH("COLD_FRESH"),
        MAKE_ORDER_DIRECT("MAKE_ORDER_DIRECT");

        private String value;

        DeliveryMethodEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum ReturnChargeVendorEnum {
        Y("Y"),
        N("N");

        private String value;

        ReturnChargeVendorEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum UnionDeliveryTypeEnum {
        UNION_DELIVERY("UNION_DELIVERY"),
        NOT_UNION_DELIVERY("NOT_UNION_DELIVERY");

        private String value;

        UnionDeliveryTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
