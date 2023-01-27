package com.github.kingwaggs.ordermanager.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@JsonIgnoreProperties
public class AmazonProductResponse {

    private Product product;

    @Data
    public static class Product {
        @JsonProperty("buybox_winner")
        private BuyboxWinner buyboxWinner;
        private String title;
        private String asin;
        private List<Category> categories;
    }

    @Data
    public static class BuyboxWinner {
        @JsonProperty("offer_id")
        private String offerId;
        private Availability availability;
        @JsonProperty("unqualified_buy_box")
        private boolean unqualifiedBuyBox;
        private Price price;
        private Shipping shipping;
    }

    @Data
    public static class Shipping {
        private String raw;
        private Double value;
    }

    @Data
    public static class Availability {
        private String type;
        private String raw;
    }

    @Data
    public static class Category {
        private String name;
        private String link;
        @JsonProperty("category_id")
        private String categoryId;
    }

    @Data
    public static class Price {
        private String symbol;
        private Double value;
        private String currency;
        private String raw;
    }

    public String getAsin() {
        return Optional.ofNullable(product)
                .map(Product::getAsin)
                .orElse(null);
    }
    public Double getPrice() {
        return Optional.ofNullable(product)
                .map(Product::getBuyboxWinner)
                .map(BuyboxWinner::getPrice)
                .map(Price::getValue)
                .orElse(null);
    }

    public String getQuantity() {
        return Optional.ofNullable(product)
                .map(Product::getBuyboxWinner)
                .map(BuyboxWinner::getAvailability)
                .map(Availability::getRaw)
                .orElse(null);
    }

    public String getTitle() {
        return Optional.ofNullable(product)
                .map(Product::getTitle)
                .orElse(null);
    }

}
