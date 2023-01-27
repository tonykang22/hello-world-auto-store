package com.github.kingwaggs.ordermanager.coupangsdk.domain.product;

public class OSellerProductItemAttribute {

    private String attributeValueName;
    private String attributeTypeName;
    private OSellerProductItemAttribute.ExposedEnum exposed;
    private String editable;

    public enum ExposedEnum {
        EXPOSED("EXPOSED"),
        NONE("NONE");

        private final String value;

        ExposedEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
