package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AutoCategorizationRequestDto {
    @SerializedName("attributes")
    private Map<String, String> attributes = new HashMap();
    @SerializedName("brand")
    private String brand = null;
    @SerializedName("productDescription")
    private String productDescription = null;
    @SerializedName("productName")
    private String productName = null;
    @SerializedName("sellerSkuCode")
    private String sellerSkuCode = null;

    public AutoCategorizationRequestDto() {
    }

    public AutoCategorizationRequestDto attributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public AutoCategorizationRequestDto putAttributesItem(String key, String attributesItem) {
        this.attributes.put(key, attributesItem);
        return this;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public AutoCategorizationRequestDto brand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public AutoCategorizationRequestDto productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public AutoCategorizationRequestDto productName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public AutoCategorizationRequestDto sellerSkuCode(String sellerSkuCode) {
        this.sellerSkuCode = sellerSkuCode;
        return this;
    }

    public String getSellerSkuCode() {
        return this.sellerSkuCode;
    }

    public void setSellerSkuCode(String sellerSkuCode) {
        this.sellerSkuCode = sellerSkuCode;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AutoCategorizationRequestDto autoCategorizationRequestDto = (AutoCategorizationRequestDto)o;
            return Objects.equals(this.attributes, autoCategorizationRequestDto.attributes) && Objects.equals(this.brand, autoCategorizationRequestDto.brand) && Objects.equals(this.productDescription, autoCategorizationRequestDto.productDescription) && Objects.equals(this.productName, autoCategorizationRequestDto.productName) && Objects.equals(this.sellerSkuCode, autoCategorizationRequestDto.sellerSkuCode);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.attributes, this.brand, this.productDescription, this.productName, this.sellerSkuCode});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AutoCategorizationRequestDto {\n");
        sb.append("    attributes: ").append(this.toIndentedString(this.attributes)).append("\n");
        sb.append("    brand: ").append(this.toIndentedString(this.brand)).append("\n");
        sb.append("    productDescription: ").append(this.toIndentedString(this.productDescription)).append("\n");
        sb.append("    productName: ").append(this.toIndentedString(this.productName)).append("\n");
        sb.append("    sellerSkuCode: ").append(this.toIndentedString(this.sellerSkuCode)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
