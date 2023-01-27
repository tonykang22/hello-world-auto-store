package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OSellerProductItemImage {
    @SerializedName("vendorPath")
    private String vendorPath = null;
    @SerializedName("imageType")
    private OSellerProductItemImage.ImageTypeEnum imageType = null;
    @SerializedName("imageOrder")
    private Long imageOrder = null;
    @SerializedName("cdnPath")
    private String cdnPath = null;

    public OSellerProductItemImage() {
    }

    public OSellerProductItemImage vendorPath(String vendorPath) {
        this.vendorPath = vendorPath;
        return this;
    }

    public String getVendorPath() {
        return this.vendorPath;
    }

    public void setVendorPath(String vendorPath) {
        this.vendorPath = vendorPath;
    }

    public OSellerProductItemImage imageType(OSellerProductItemImage.ImageTypeEnum imageType) {
        this.imageType = imageType;
        return this;
    }

    public OSellerProductItemImage.ImageTypeEnum getImageType() {
        return this.imageType;
    }

    public void setImageType(OSellerProductItemImage.ImageTypeEnum imageType) {
        this.imageType = imageType;
    }

    public OSellerProductItemImage imageOrder(Long imageOrder) {
        this.imageOrder = imageOrder;
        return this;
    }

    public Long getImageOrder() {
        return this.imageOrder;
    }

    public void setImageOrder(Long imageOrder) {
        this.imageOrder = imageOrder;
    }

    public OSellerProductItemImage cdnPath(String cdnPath) {
        this.cdnPath = cdnPath;
        return this;
    }

    public String getCdnPath() {
        return this.cdnPath;
    }

    public void setCdnPath(String cdnPath) {
        this.cdnPath = cdnPath;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OSellerProductItemImage osellerProductItemImage = (OSellerProductItemImage)o;
            return Objects.equals(this.vendorPath, osellerProductItemImage.vendorPath) && Objects.equals(this.imageType, osellerProductItemImage.imageType) && Objects.equals(this.imageOrder, osellerProductItemImage.imageOrder) && Objects.equals(this.cdnPath, osellerProductItemImage.cdnPath);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.vendorPath, this.imageType, this.imageOrder, this.cdnPath});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OSellerProductItemImage {\n");
        sb.append("    vendorPath: ").append(this.toIndentedString(this.vendorPath)).append("\n");
        sb.append("    imageType: ").append(this.toIndentedString(this.imageType)).append("\n");
        sb.append("    imageOrder: ").append(this.toIndentedString(this.imageOrder)).append("\n");
        sb.append("    cdnPath: ").append(this.toIndentedString(this.cdnPath)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static enum ImageTypeEnum {
        @SerializedName("REPRESENTATION")
        REPRESENTATION("REPRESENTATION"),
        @SerializedName("REP_OBLONG_ORIGIN")
        REP_OBLONG_ORIGIN("REP_OBLONG_ORIGIN"),
        @SerializedName("DETAIL")
        DETAIL("DETAIL"),
        @SerializedName("USED_PRODUCT")
        USED_PRODUCT("USED_PRODUCT");

        private String value;

        private ImageTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
