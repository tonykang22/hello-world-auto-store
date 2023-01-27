package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OVendorInventoryItemContentDetail {
    @SerializedName("detailType")
    private OVendorInventoryItemContentDetail.DetailTypeEnum detailType = null;
    @SerializedName("content")
    private String content = null;

    public OVendorInventoryItemContentDetail() {
    }

    public OVendorInventoryItemContentDetail detailType(OVendorInventoryItemContentDetail.DetailTypeEnum detailType) {
        this.detailType = detailType;
        return this;
    }

    public OVendorInventoryItemContentDetail.DetailTypeEnum getDetailType() {
        return this.detailType;
    }

    public void setDetailType(OVendorInventoryItemContentDetail.DetailTypeEnum detailType) {
        this.detailType = detailType;
    }

    public OVendorInventoryItemContentDetail content(String content) {
        this.content = content;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OVendorInventoryItemContentDetail ovendorInventoryItemContentDetail = (OVendorInventoryItemContentDetail)o;
            return Objects.equals(this.detailType, ovendorInventoryItemContentDetail.detailType) && Objects.equals(this.content, ovendorInventoryItemContentDetail.content);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.detailType, this.content});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OVendorInventoryItemContentDetail {\n");
        sb.append("    detailType: ").append(this.toIndentedString(this.detailType)).append("\n");
        sb.append("    content: ").append(this.toIndentedString(this.content)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static enum DetailTypeEnum {
        @SerializedName("TITLE")
        TITLE("TITLE"),
        @SerializedName("IMAGE")
        IMAGE("IMAGE"),
        @SerializedName("TEXT")
        TEXT("TEXT");

        private String value;

        private DetailTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
