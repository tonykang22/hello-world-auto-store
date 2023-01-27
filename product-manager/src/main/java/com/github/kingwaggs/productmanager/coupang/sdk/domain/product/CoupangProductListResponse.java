package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class CoupangProductListResponse {

    private String nextToken;
    private String message;
    @SerializedName("data")
    private List<CoupangProduct> productList;
    private String code;

    @Data
    public static class CoupangProduct {
        private String vendorId;
        private String statusName;
        private String sellerProductName;
        private Long sellerProductId;
        private String saleStartedAt;
        private String saleEndedAt;
        private Long productId;
        private String mdName;
        private String mdId;
        private Long displayCategoryCode;
        private String createdAt;
        private Long categoryId;
        private String brand;
    }
}
