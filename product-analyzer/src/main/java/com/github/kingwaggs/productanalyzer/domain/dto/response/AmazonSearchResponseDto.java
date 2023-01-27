package com.github.kingwaggs.productanalyzer.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AmazonSearchResponseDto {
    @JsonProperty("search_results")
    private List<ProductInfo> searchResults;
    private Refinements refinements;

    @Data
    public static class Refinements {
        private List<NameValue> reviews;
        private List<NameValue> price;
    }

    @Data
    public static class NameValue {
        private String name;
        private String value;
    }

    @Data
    public static class ProductInfo {
        @JsonProperty("asin")
        private String productId;
    }
}
