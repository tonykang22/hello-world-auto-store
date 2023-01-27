package com.github.kingwaggs.ordermanager.coupangsdk.domain.product;

public class OSellerProductItemImage {

    private String vendorPath;
    private OSellerProductItemImage.ImageTypeEnum imageType;
    private Long imageOrder;
    private String cdnPath;

    public enum ImageTypeEnum {
        REPRESENTATION("REPRESENTATION"),
        REP_OBLONG_ORIGIN("REP_OBLONG_ORIGIN"),
        DETAIL("DETAIL"),
        USED_PRODUCT("USED_PRODUCT");

        private final String value;

        ImageTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
