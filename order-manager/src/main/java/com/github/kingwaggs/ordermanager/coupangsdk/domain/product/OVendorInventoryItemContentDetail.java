package com.github.kingwaggs.ordermanager.coupangsdk.domain.product;

public class OVendorInventoryItemContentDetail {

    private OVendorInventoryItemContentDetail.DetailTypeEnum detailType;
    private String content;

    public enum DetailTypeEnum {
        TITLE("TITLE"),
        IMAGE("IMAGE"),
        TEXT("TEXT");

        private final String value;

        DetailTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
