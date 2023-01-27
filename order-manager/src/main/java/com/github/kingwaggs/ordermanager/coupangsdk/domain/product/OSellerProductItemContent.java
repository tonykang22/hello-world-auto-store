package com.github.kingwaggs.ordermanager.coupangsdk.domain.product;

import java.util.List;

public class OSellerProductItemContent {

    private OSellerProductItemContent.ContentsTypeEnum contentsType;
    private List<OVendorInventoryItemContentDetail> contentDetails;

    public enum ContentsTypeEnum {
        IMAGE("IMAGE"),
        IMAGE_NO_SPACE("IMAGE_NO_SPACE"),
        TEXT("TEXT"),
        IMAGE_TEXT("IMAGE_TEXT"),
        TEXT_IMAGE("TEXT_IMAGE"),
        IMAGE_IMAGE("IMAGE_IMAGE"),
        TEXT_TEXT("TEXT_TEXT"),
        TITLE("TITLE"),
        HTML("HTML");

        private final String value;

        ContentsTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
