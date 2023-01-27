package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.*;

public class OSellerProductItem {
    @SerializedName("vendorItemId")
    private Long vendorItemId = null;
    @SerializedName("unitCount")
    private Integer unitCount = null;
    @SerializedName("taxType")
    private OSellerProductItem.TaxTypeEnum taxType = null;
    @SerializedName("supplyPrice")
    private Double supplyPrice = null;
    @SerializedName("offerCondition")
    private OSellerProductItem.OfferConditionEnum offerCondition = null;
    @SerializedName("offerDescription")
    private String offerDescription = null;
    @SerializedName("pccNeeded")
    private Boolean pccNeeded = null;
    @SerializedName("bestPriceGuaranteed3P")
    private Boolean bestPriceGuaranteed3P = null;
    @SerializedName("sellerProductItemId")
    private Long sellerProductItemId = null;
    @SerializedName("searchTags")
    private List<String> searchTags = new ArrayList();
    @SerializedName("salePrice")
    private Double salePrice = null;
    @SerializedName("saleAgentCommission")
    private Double saleAgentCommission = null;
    @SerializedName("parallelImported")
    private OSellerProductItem.ParallelImportedEnum parallelImported = null;
    @SerializedName("overseasPurchased")
    private OSellerProductItem.OverseasPurchasedEnum overseasPurchased = null;
    @SerializedName("outboundShippingTimeDay")
    private Integer outboundShippingTimeDay = null;
    @SerializedName("originalPrice")
    private Double originalPrice = null;
    @SerializedName("notices")
    private List<OSellerProductItemNotice> notices = new ArrayList();
    @SerializedName("modelNo")
    private String modelNo = null;
    @SerializedName("maximumBuyForPersonPeriod")
    private Integer maximumBuyForPersonPeriod = null;
    @SerializedName("maximumBuyForPerson")
    private Integer maximumBuyForPerson = null;
    @SerializedName("maximumBuyCount")
    private Integer maximumBuyCount = null;
    @SerializedName("itemName")
    private String itemName = null;
    @SerializedName("images")
    private List<OSellerProductItemImage> images = new ArrayList();
    @SerializedName("freePriceType")
    private String freePriceType = null;
    @SerializedName("extraProperties")
    private Map<String, String> extraProperties = new HashMap();
    @SerializedName("externalVendorSku")
    private String externalVendorSku = null;
    @SerializedName("emptyBarcodeReason")
    private String emptyBarcodeReason = null;
    @SerializedName("emptyBarcode")
    private Boolean emptyBarcode = null;
    @SerializedName("contents")
    private List<OSellerProductItemContent> contents = new ArrayList();
    @SerializedName("certifications")
    private List<OSellerProductItemCertification> certifications = new ArrayList();
    @SerializedName("barcode")
    private String barcode = null;
    @SerializedName("attributes")
    private List<OSellerProductItemAttribute> attributes = new ArrayList();
    @SerializedName("adultOnly")
    private OSellerProductItem.AdultOnlyEnum adultOnly = null;

    public OSellerProductItem() {
    }

    public OSellerProductItem vendorItemId(Long vendorItemId) {
        this.vendorItemId = vendorItemId;
        return this;
    }

    public Long getVendorItemId() {
        return this.vendorItemId;
    }

    public void setVendorItemId(Long vendorItemId) {
        this.vendorItemId = vendorItemId;
    }

    public OSellerProductItem unitCount(Integer unitCount) {
        this.unitCount = unitCount;
        return this;
    }

    public Integer getUnitCount() {
        return this.unitCount;
    }

    public void setUnitCount(Integer unitCount) {
        this.unitCount = unitCount;
    }

    public OSellerProductItem taxType(OSellerProductItem.TaxTypeEnum taxType) {
        this.taxType = taxType;
        return this;
    }

    public OSellerProductItem.TaxTypeEnum getTaxType() {
        return this.taxType;
    }

    public void setTaxType(OSellerProductItem.TaxTypeEnum taxType) {
        this.taxType = taxType;
    }

    public OSellerProductItem supplyPrice(Double supplyPrice) {
        this.supplyPrice = supplyPrice;
        return this;
    }

    public Double getSupplyPrice() {
        return this.supplyPrice;
    }

    public void setSupplyPrice(Double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public OSellerProductItem offerCondition(OSellerProductItem.OfferConditionEnum offerCondition) {
        this.offerCondition = offerCondition;
        return this;
    }

    public OSellerProductItem.OfferConditionEnum getOfferCondition() {
        return this.offerCondition;
    }

    public void setOfferCondition(OSellerProductItem.OfferConditionEnum offerCondition) {
        this.offerCondition = offerCondition;
    }

    public OSellerProductItem offerDescription(String offerDescription) {
        this.offerDescription = offerDescription;
        return this;
    }

    public String getOfferDescription() {
        return this.offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public OSellerProductItem pccNeeded(Boolean pccNeeded) {
        this.pccNeeded = pccNeeded;
        return this;
    }

    public Boolean getPccNeeded() {
        return this.pccNeeded;
    }

    public void setPccNeeded(Boolean pccNeeded) {
        this.pccNeeded = pccNeeded;
    }

    public OSellerProductItem bestPriceGuaranteed3P(Boolean bestPriceGuaranteed3P) {
        this.bestPriceGuaranteed3P = bestPriceGuaranteed3P;
        return this;
    }

    public Boolean getBestPriceGuaranteed3P() {
        return this.bestPriceGuaranteed3P;
    }

    public void setBestPriceGuaranteed3P(Boolean bestPriceGuaranteed3P) {
        this.bestPriceGuaranteed3P = bestPriceGuaranteed3P;
    }

    public OSellerProductItem sellerProductItemId(Long sellerProductItemId) {
        this.sellerProductItemId = sellerProductItemId;
        return this;
    }

    public Long getSellerProductItemId() {
        return this.sellerProductItemId;
    }

    public void setSellerProductItemId(Long sellerProductItemId) {
        this.sellerProductItemId = sellerProductItemId;
    }

    public OSellerProductItem searchTags(List<String> searchTags) {
        this.searchTags = searchTags;
        return this;
    }

    public OSellerProductItem addSearchTagsItem(String searchTagsItem) {
        this.searchTags.add(searchTagsItem);
        return this;
    }

    public List<String> getSearchTags() {
        return this.searchTags;
    }

    public void setSearchTags(List<String> searchTags) {
        this.searchTags = searchTags;
    }

    public OSellerProductItem salePrice(Double salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public Double getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public OSellerProductItem saleAgentCommission(Double saleAgentCommission) {
        this.saleAgentCommission = saleAgentCommission;
        return this;
    }

    public Double getSaleAgentCommission() {
        return this.saleAgentCommission;
    }

    public void setSaleAgentCommission(Double saleAgentCommission) {
        this.saleAgentCommission = saleAgentCommission;
    }

    public OSellerProductItem parallelImported(OSellerProductItem.ParallelImportedEnum parallelImported) {
        this.parallelImported = parallelImported;
        return this;
    }

    public OSellerProductItem.ParallelImportedEnum getParallelImported() {
        return this.parallelImported;
    }

    public void setParallelImported(OSellerProductItem.ParallelImportedEnum parallelImported) {
        this.parallelImported = parallelImported;
    }

    public OSellerProductItem overseasPurchased(OSellerProductItem.OverseasPurchasedEnum overseasPurchased) {
        this.overseasPurchased = overseasPurchased;
        return this;
    }

    public OSellerProductItem.OverseasPurchasedEnum getOverseasPurchased() {
        return this.overseasPurchased;
    }

    public void setOverseasPurchased(OSellerProductItem.OverseasPurchasedEnum overseasPurchased) {
        this.overseasPurchased = overseasPurchased;
    }

    public OSellerProductItem outboundShippingTimeDay(Integer outboundShippingTimeDay) {
        this.outboundShippingTimeDay = outboundShippingTimeDay;
        return this;
    }

    public Integer getOutboundShippingTimeDay() {
        return this.outboundShippingTimeDay;
    }

    public void setOutboundShippingTimeDay(Integer outboundShippingTimeDay) {
        this.outboundShippingTimeDay = outboundShippingTimeDay;
    }

    public OSellerProductItem originalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
        return this;
    }

    public Double getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public OSellerProductItem notices(List<OSellerProductItemNotice> notices) {
        this.notices = notices;
        return this;
    }

    public OSellerProductItem addNoticesItem(OSellerProductItemNotice noticesItem) {
        this.notices.add(noticesItem);
        return this;
    }

    public List<OSellerProductItemNotice> getNotices() {
        return this.notices;
    }

    public void setNotices(List<OSellerProductItemNotice> notices) {
        this.notices = notices;
    }

    public OSellerProductItem modelNo(String modelNo) {
        this.modelNo = modelNo;
        return this;
    }

    public String getModelNo() {
        return this.modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public OSellerProductItem maximumBuyForPersonPeriod(Integer maximumBuyForPersonPeriod) {
        this.maximumBuyForPersonPeriod = maximumBuyForPersonPeriod;
        return this;
    }

    public Integer getMaximumBuyForPersonPeriod() {
        return this.maximumBuyForPersonPeriod;
    }

    public void setMaximumBuyForPersonPeriod(Integer maximumBuyForPersonPeriod) {
        this.maximumBuyForPersonPeriod = maximumBuyForPersonPeriod;
    }

    public OSellerProductItem maximumBuyForPerson(Integer maximumBuyForPerson) {
        this.maximumBuyForPerson = maximumBuyForPerson;
        return this;
    }

    public Integer getMaximumBuyForPerson() {
        return this.maximumBuyForPerson;
    }

    public void setMaximumBuyForPerson(Integer maximumBuyForPerson) {
        this.maximumBuyForPerson = maximumBuyForPerson;
    }

    public OSellerProductItem maximumBuyCount(Integer maximumBuyCount) {
        this.maximumBuyCount = maximumBuyCount;
        return this;
    }

    public Integer getMaximumBuyCount() {
        return this.maximumBuyCount;
    }

    public void setMaximumBuyCount(Integer maximumBuyCount) {
        this.maximumBuyCount = maximumBuyCount;
    }

    public OSellerProductItem itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public OSellerProductItem images(List<OSellerProductItemImage> images) {
        this.images = images;
        return this;
    }

    public OSellerProductItem addImagesItem(OSellerProductItemImage imagesItem) {
        this.images.add(imagesItem);
        return this;
    }

    public List<OSellerProductItemImage> getImages() {
        return this.images;
    }

    public void setImages(List<OSellerProductItemImage> images) {
        this.images = images;
    }

    public OSellerProductItem freePriceType(String freePriceType) {
        this.freePriceType = freePriceType;
        return this;
    }

    public String getFreePriceType() {
        return this.freePriceType;
    }

    public void setFreePriceType(String freePriceType) {
        this.freePriceType = freePriceType;
    }

    public OSellerProductItem extraProperties(Map<String, String> extraProperties) {
        this.extraProperties = extraProperties;
        return this;
    }

    public OSellerProductItem putExtraPropertiesItem(String key, String extraPropertiesItem) {
        this.extraProperties.put(key, extraPropertiesItem);
        return this;
    }

    public Map<String, String> getExtraProperties() {
        return this.extraProperties;
    }

    public void setExtraProperties(Map<String, String> extraProperties) {
        this.extraProperties = extraProperties;
    }

    public OSellerProductItem externalVendorSku(String externalVendorSku) {
        this.externalVendorSku = externalVendorSku;
        return this;
    }

    public String getExternalVendorSku() {
        return this.externalVendorSku;
    }

    public void setExternalVendorSku(String externalVendorSku) {
        this.externalVendorSku = externalVendorSku;
    }

    public OSellerProductItem emptyBarcodeReason(String emptyBarcodeReason) {
        this.emptyBarcodeReason = emptyBarcodeReason;
        return this;
    }

    public String getEmptyBarcodeReason() {
        return this.emptyBarcodeReason;
    }

    public void setEmptyBarcodeReason(String emptyBarcodeReason) {
        this.emptyBarcodeReason = emptyBarcodeReason;
    }

    public OSellerProductItem emptyBarcode(Boolean emptyBarcode) {
        this.emptyBarcode = emptyBarcode;
        return this;
    }

    public Boolean getEmptyBarcode() {
        return this.emptyBarcode;
    }

    public void setEmptyBarcode(Boolean emptyBarcode) {
        this.emptyBarcode = emptyBarcode;
    }

    public OSellerProductItem contents(List<OSellerProductItemContent> contents) {
        this.contents = contents;
        return this;
    }

    public OSellerProductItem addContentsItem(OSellerProductItemContent contentsItem) {
        this.contents.add(contentsItem);
        return this;
    }

    public List<OSellerProductItemContent> getContents() {
        return this.contents;
    }

    public void setContents(List<OSellerProductItemContent> contents) {
        this.contents = contents;
    }

    public OSellerProductItem certifications(List<OSellerProductItemCertification> certifications) {
        this.certifications = certifications;
        return this;
    }

    public OSellerProductItem addCertificationsItem(OSellerProductItemCertification certificationsItem) {
        this.certifications.add(certificationsItem);
        return this;
    }

    public List<OSellerProductItemCertification> getCertifications() {
        return this.certifications;
    }

    public void setCertifications(List<OSellerProductItemCertification> certifications) {
        this.certifications = certifications;
    }

    public OSellerProductItem barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public OSellerProductItem attributes(List<OSellerProductItemAttribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public OSellerProductItem addAttributesItem(OSellerProductItemAttribute attributesItem) {
        this.attributes.add(attributesItem);
        return this;
    }

    public List<OSellerProductItemAttribute> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(List<OSellerProductItemAttribute> attributes) {
        this.attributes = attributes;
    }

    public OSellerProductItem adultOnly(OSellerProductItem.AdultOnlyEnum adultOnly) {
        this.adultOnly = adultOnly;
        return this;
    }

    public OSellerProductItem.AdultOnlyEnum getAdultOnly() {
        return this.adultOnly;
    }

    public void setAdultOnly(OSellerProductItem.AdultOnlyEnum adultOnly) {
        this.adultOnly = adultOnly;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OSellerProductItem osellerProductItem = (OSellerProductItem)o;
            return Objects.equals(this.vendorItemId, osellerProductItem.vendorItemId) && Objects.equals(this.unitCount, osellerProductItem.unitCount) && Objects.equals(this.taxType, osellerProductItem.taxType) && Objects.equals(this.supplyPrice, osellerProductItem.supplyPrice) && Objects.equals(this.offerCondition, osellerProductItem.offerCondition) && Objects.equals(this.offerDescription, osellerProductItem.offerDescription) && Objects.equals(this.pccNeeded, osellerProductItem.pccNeeded) && Objects.equals(this.bestPriceGuaranteed3P, osellerProductItem.bestPriceGuaranteed3P) && Objects.equals(this.sellerProductItemId, osellerProductItem.sellerProductItemId) && Objects.equals(this.searchTags, osellerProductItem.searchTags) && Objects.equals(this.salePrice, osellerProductItem.salePrice) && Objects.equals(this.saleAgentCommission, osellerProductItem.saleAgentCommission) && Objects.equals(this.parallelImported, osellerProductItem.parallelImported) && Objects.equals(this.overseasPurchased, osellerProductItem.overseasPurchased) && Objects.equals(this.outboundShippingTimeDay, osellerProductItem.outboundShippingTimeDay) && Objects.equals(this.originalPrice, osellerProductItem.originalPrice) && Objects.equals(this.notices, osellerProductItem.notices) && Objects.equals(this.modelNo, osellerProductItem.modelNo) && Objects.equals(this.maximumBuyForPersonPeriod, osellerProductItem.maximumBuyForPersonPeriod) && Objects.equals(this.maximumBuyForPerson, osellerProductItem.maximumBuyForPerson) && Objects.equals(this.maximumBuyCount, osellerProductItem.maximumBuyCount) && Objects.equals(this.itemName, osellerProductItem.itemName) && Objects.equals(this.images, osellerProductItem.images) && Objects.equals(this.freePriceType, osellerProductItem.freePriceType) && Objects.equals(this.extraProperties, osellerProductItem.extraProperties) && Objects.equals(this.externalVendorSku, osellerProductItem.externalVendorSku) && Objects.equals(this.emptyBarcodeReason, osellerProductItem.emptyBarcodeReason) && Objects.equals(this.emptyBarcode, osellerProductItem.emptyBarcode) && Objects.equals(this.contents, osellerProductItem.contents) && Objects.equals(this.certifications, osellerProductItem.certifications) && Objects.equals(this.barcode, osellerProductItem.barcode) && Objects.equals(this.attributes, osellerProductItem.attributes) && Objects.equals(this.adultOnly, osellerProductItem.adultOnly);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.vendorItemId, this.unitCount, this.taxType, this.supplyPrice, this.offerCondition, this.offerDescription, this.pccNeeded, this.bestPriceGuaranteed3P, this.sellerProductItemId, this.searchTags, this.salePrice, this.saleAgentCommission, this.parallelImported, this.overseasPurchased, this.outboundShippingTimeDay, this.originalPrice, this.notices, this.modelNo, this.maximumBuyForPersonPeriod, this.maximumBuyForPerson, this.maximumBuyCount, this.itemName, this.images, this.freePriceType, this.extraProperties, this.externalVendorSku, this.emptyBarcodeReason, this.emptyBarcode, this.contents, this.certifications, this.barcode, this.attributes, this.adultOnly});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OSellerProductItem {\n");
        sb.append("    vendorItemId: ").append(this.toIndentedString(this.vendorItemId)).append("\n");
        sb.append("    unitCount: ").append(this.toIndentedString(this.unitCount)).append("\n");
        sb.append("    taxType: ").append(this.toIndentedString(this.taxType)).append("\n");
        sb.append("    supplyPrice: ").append(this.toIndentedString(this.supplyPrice)).append("\n");
        sb.append("    offerCondition: ").append(this.toIndentedString(this.offerCondition)).append("\n");
        sb.append("    offerDescription: ").append(this.toIndentedString(this.offerDescription)).append("\n");
        sb.append("    pccNeeded: ").append(this.toIndentedString(this.pccNeeded)).append("\n");
        sb.append("    bestPriceGuaranteed3P: ").append(this.toIndentedString(this.bestPriceGuaranteed3P)).append("\n");
        sb.append("    sellerProductItemId: ").append(this.toIndentedString(this.sellerProductItemId)).append("\n");
        sb.append("    searchTags: ").append(this.toIndentedString(this.searchTags)).append("\n");
        sb.append("    salePrice: ").append(this.toIndentedString(this.salePrice)).append("\n");
        sb.append("    saleAgentCommission: ").append(this.toIndentedString(this.saleAgentCommission)).append("\n");
        sb.append("    parallelImported: ").append(this.toIndentedString(this.parallelImported)).append("\n");
        sb.append("    overseasPurchased: ").append(this.toIndentedString(this.overseasPurchased)).append("\n");
        sb.append("    outboundShippingTimeDay: ").append(this.toIndentedString(this.outboundShippingTimeDay)).append("\n");
        sb.append("    originalPrice: ").append(this.toIndentedString(this.originalPrice)).append("\n");
        sb.append("    notices: ").append(this.toIndentedString(this.notices)).append("\n");
        sb.append("    modelNo: ").append(this.toIndentedString(this.modelNo)).append("\n");
        sb.append("    maximumBuyForPersonPeriod: ").append(this.toIndentedString(this.maximumBuyForPersonPeriod)).append("\n");
        sb.append("    maximumBuyForPerson: ").append(this.toIndentedString(this.maximumBuyForPerson)).append("\n");
        sb.append("    maximumBuyCount: ").append(this.toIndentedString(this.maximumBuyCount)).append("\n");
        sb.append("    itemName: ").append(this.toIndentedString(this.itemName)).append("\n");
        sb.append("    images: ").append(this.toIndentedString(this.images)).append("\n");
        sb.append("    freePriceType: ").append(this.toIndentedString(this.freePriceType)).append("\n");
        sb.append("    extraProperties: ").append(this.toIndentedString(this.extraProperties)).append("\n");
        sb.append("    externalVendorSku: ").append(this.toIndentedString(this.externalVendorSku)).append("\n");
        sb.append("    emptyBarcodeReason: ").append(this.toIndentedString(this.emptyBarcodeReason)).append("\n");
        sb.append("    emptyBarcode: ").append(this.toIndentedString(this.emptyBarcode)).append("\n");
        sb.append("    contents: ").append(this.toIndentedString(this.contents)).append("\n");
        sb.append("    certifications: ").append(this.toIndentedString(this.certifications)).append("\n");
        sb.append("    barcode: ").append(this.toIndentedString(this.barcode)).append("\n");
        sb.append("    attributes: ").append(this.toIndentedString(this.attributes)).append("\n");
        sb.append("    adultOnly: ").append(this.toIndentedString(this.adultOnly)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static enum AdultOnlyEnum {
        @SerializedName("ADULT_ONLY")
        ADULT_ONLY("ADULT_ONLY"),
        @SerializedName("EVERYONE")
        EVERYONE("EVERYONE");

        private String value;

        private AdultOnlyEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static enum OverseasPurchasedEnum {
        @SerializedName("OVERSEAS_PURCHASED")
        OVERSEAS_PURCHASED("OVERSEAS_PURCHASED"),
        @SerializedName("NOT_OVERSEAS_PURCHASED")
        NOT_OVERSEAS_PURCHASED("NOT_OVERSEAS_PURCHASED");

        private String value;

        private OverseasPurchasedEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static enum ParallelImportedEnum {
        @SerializedName("PARALLEL_IMPORTED")
        PARALLEL_IMPORTED("PARALLEL_IMPORTED"),
        @SerializedName("NOT_PARALLEL_IMPORTED")
        NOT_PARALLEL_IMPORTED("NOT_PARALLEL_IMPORTED");

        private String value;

        private ParallelImportedEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static enum OfferConditionEnum {
        @SerializedName("NEW")
        NEW("NEW"),
        @SerializedName("REFURBISHED")
        REFURBISHED("REFURBISHED"),
        @SerializedName("USED_BEST")
        USED_BEST("USED_BEST"),
        @SerializedName("USED_GOOD")
        USED_GOOD("USED_GOOD"),
        @SerializedName("USED_NORMAL")
        USED_NORMAL("USED_NORMAL");

        private String value;

        private OfferConditionEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static enum TaxTypeEnum {
        @SerializedName("TAX")
        TAX("TAX"),
        @SerializedName("FREE")
        FREE("FREE");

        private String value;

        private TaxTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
