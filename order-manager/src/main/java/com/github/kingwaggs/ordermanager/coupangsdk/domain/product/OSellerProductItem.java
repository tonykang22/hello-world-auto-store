package com.github.kingwaggs.ordermanager.coupangsdk.domain.product;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class OSellerProductItem {

    private Long vendorItemId;
    private Integer unitCount;
    private OSellerProductItem.TaxTypeEnum taxType;
    private Double supplyPrice;
    private OSellerProductItem.OfferConditionEnum offerCondition;
    private String offerDescription;
    private Boolean pccNeeded;
    private Boolean bestPriceGuaranteed3P;
    private Long sellerProductItemId;
    private List<String> searchTags;
    private Double salePrice;
    private Double saleAgentCommission;
    private OSellerProductItem.ParallelImportedEnum parallelImported;
    private OSellerProductItem.OverseasPurchasedEnum overseasPurchased;
    private Integer outboundShippingTimeDay;
    private Double originalPrice;
    private List<OSellerProductItemNotice> notices;
    private String modelNo;
    private Integer maximumBuyForPersonPeriod;
    private Integer maximumBuyForPerson;
    private Integer maximumBuyCount;
    private String itemName;
    private List<OSellerProductItemImage> images;
    private String freePriceType;
    private Map<String, String> extraProperties;
    private String externalVendorSku;
    private String emptyBarcodeReason;
    private Boolean emptyBarcode;
    private List<OSellerProductItemContent> contents;
    private List<OSellerProductItemCertification> certifications;
    private String barcode;
    private List<OSellerProductItemAttribute> attributes;
    private OSellerProductItem.AdultOnlyEnum adultOnly;

    public enum AdultOnlyEnum {
        ADULT_ONLY("ADULT_ONLY"),
        EVERYONE("EVERYONE");

        private final String value;

        AdultOnlyEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum OverseasPurchasedEnum {
        OVERSEAS_PURCHASED("OVERSEAS_PURCHASED"),
        NOT_OVERSEAS_PURCHASED("NOT_OVERSEAS_PURCHASED");

        private final String value;

        OverseasPurchasedEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum ParallelImportedEnum {
        PARALLEL_IMPORTED("PARALLEL_IMPORTED"),
        NOT_PARALLEL_IMPORTED("NOT_PARALLEL_IMPORTED");

        private final String value;

        ParallelImportedEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum OfferConditionEnum {
        NEW("NEW"),
        REFURBISHED("REFURBISHED"),
        USED_BEST("USED_BEST"),
        USED_GOOD("USED_GOOD"),
        USED_NORMAL("USED_NORMAL");

        private final String value;

        OfferConditionEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public enum TaxTypeEnum {
        TAX("TAX"),
        FREE("FREE");

        private final String value;

        TaxTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
