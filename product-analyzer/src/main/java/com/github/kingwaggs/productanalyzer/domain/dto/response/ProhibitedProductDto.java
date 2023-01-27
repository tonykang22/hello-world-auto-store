package com.github.kingwaggs.productanalyzer.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class ProhibitedProductDto {

    @JsonProperty("I2715")
    private Content content;

    @Data
    public static class Content {

        @JsonProperty("total_count")
        private Integer totalProduct;

        @JsonProperty("row")
        private List<Product> productList;

    }

    @Data
    public static class Product {

        @JsonProperty("PRDT_NM")
        private String name;

        @JsonProperty("MUFC_NM")
        private String brand;

        @JsonProperty("MUFC_CNTRY_NM")
        private String country;

        @JsonProperty("INGR_NM_LST")
        private String forbiddenIngredient;

        @JsonProperty("STT_YMD")
        private String startDate;

        @JsonProperty("END_YMD")
        private String endDate;

        @JsonProperty("CRET_DTM")
        private String createdAt;

        @JsonProperty("LAST_UPDT_DTM")
        private String modifiedAt;

        @JsonProperty("IMAGE_URL")
        private String imageUrl;

        @JsonProperty("SELF_IMPORT_SEQ")
        private String serialNumber;

        @JsonProperty("BARCD_CTN")
        private String barcodeNumber;

    }

    public List<Product> getProhibitedProductList() {
        return Optional.ofNullable(content)
                .map(Content::getProductList)
                .orElseThrow();
    }

}
