package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OSellerProductItemContent {
    @SerializedName("contentsType")
    private OSellerProductItemContent.ContentsTypeEnum contentsType = null;
    @SerializedName("contentDetails")
    private List<OVendorInventoryItemContentDetail> contentDetails = new ArrayList();

    public OSellerProductItemContent() {
    }

    public OSellerProductItemContent contentsType(OSellerProductItemContent.ContentsTypeEnum contentsType) {
        this.contentsType = contentsType;
        return this;
    }

    public OSellerProductItemContent.ContentsTypeEnum getContentsType() {
        return this.contentsType;
    }

    public void setContentsType(OSellerProductItemContent.ContentsTypeEnum contentsType) {
        this.contentsType = contentsType;
    }

    public OSellerProductItemContent contentDetails(List<OVendorInventoryItemContentDetail> contentDetails) {
        this.contentDetails = contentDetails;
        return this;
    }

    public OSellerProductItemContent addContentDetailsItem(OVendorInventoryItemContentDetail contentDetailsItem) {
        this.contentDetails.add(contentDetailsItem);
        return this;
    }

    public List<OVendorInventoryItemContentDetail> getContentDetails() {
        return this.contentDetails;
    }

    public void setContentDetails(List<OVendorInventoryItemContentDetail> contentDetails) {
        this.contentDetails = contentDetails;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OSellerProductItemContent osellerProductItemContent = (OSellerProductItemContent)o;
            return Objects.equals(this.contentsType, osellerProductItemContent.contentsType) && Objects.equals(this.contentDetails, osellerProductItemContent.contentDetails);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.contentsType, this.contentDetails});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OSellerProductItemContent {\n");
        sb.append("    contentsType: ").append(this.toIndentedString(this.contentsType)).append("\n");
        sb.append("    contentDetails: ").append(this.toIndentedString(this.contentDetails)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static enum ContentsTypeEnum {
        @SerializedName("IMAGE")
        IMAGE("IMAGE"),
        @SerializedName("IMAGE_NO_SPACE")
        IMAGE_NO_SPACE("IMAGE_NO_SPACE"),
        @SerializedName("TEXT")
        TEXT("TEXT"),
        @SerializedName("IMAGE_TEXT")
        IMAGE_TEXT("IMAGE_TEXT"),
        @SerializedName("TEXT_IMAGE")
        TEXT_IMAGE("TEXT_IMAGE"),
        @SerializedName("IMAGE_IMAGE")
        IMAGE_IMAGE("IMAGE_IMAGE"),
        @SerializedName("TEXT_TEXT")
        TEXT_TEXT("TEXT_TEXT"),
        @SerializedName("TITLE")
        TITLE("TITLE"),
        @SerializedName("HTML")
        HTML("HTML");

        private String value;

        private ContentsTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
