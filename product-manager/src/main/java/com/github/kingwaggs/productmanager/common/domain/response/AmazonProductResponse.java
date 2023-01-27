package com.github.kingwaggs.productmanager.common.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class AmazonProductResponse {

    private Product product;

    @Data
    public static class Product {
        @JsonProperty("buybox_winner")
        private BuyboxWinner buyboxWinner;
        private String title;
        @JsonProperty("asin")
        private String productId;
        private String link;
        private String description;
        @JsonProperty("feature_bullets")
        private List<String> featureBullets;
        @JsonProperty("keywords_list")
        private List<String> keywordsList;
        @JsonProperty("main_image")
        private Link mainImage;
        private List<Link> images;
        private List<Video> videos;
        @JsonProperty("a_plus_content")
        private PlusContent plusContent;
        private List<NameValue> attributes;
        private List<NameValue> specifications;
        private List<Category> categories;
        private List<Document> documents;
        private List<Variant> variants;
        private Double rating;
        private String brand;
        private String weight;
        private String dimensions;
        @JsonProperty("shipping_weight")
        private String shippingWeight;
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
    public static class Document {
        private String name;
        private String link;
    }

    @Data
    public static class Category {
        private String name;
        private String link;
        @JsonProperty("category_id")
        private String categoryId;
    }

    @Data
    public static class PlusContent {
        @JsonProperty("has_a_plus_content")
        private boolean hasAPlusContent;
        @JsonProperty("has_brand_story")
        private boolean hasBrandStory;
        @JsonProperty("third_party")
        private boolean thirdParty;
        @JsonProperty("company_logo")
        private String companyLogo;
        @JsonProperty("company_description_text")
        private String companyDescriptionText;
        private String body;
        @JsonProperty("body_text")
        private String bodyText;
        @JsonProperty("brand_story")
        private BrandStory brandStory;
    }

    @Data
    public static class BrandStory {
        @JsonProperty("brand_logo")
        private String brandLogo;
        @JsonProperty("hero_image")
        private String heroImage;
        private String description;

    }

    @Data
    public static class Video {
        private String title;
        @JsonProperty("group_id")
        private String groupId;
        @JsonProperty("group_type")
        private String groupType;
        private String link;
        @JsonProperty("duration_seconds")
        private String durationSeconds;
        private String width;
        private String height;
        private String thumbnail;
        @JsonProperty("is_hero_video")
        private String isHeroVideo;
        private String variant;
    }

    @Data
    public static class Link {
        @JsonProperty("link")
        private String raw;
    }

    @Data
    public static class NameValue {
        private String name;
        private String value;
    }

    @Data
    public static class Variant {
        private String title;
        @JsonProperty("asin")
        private String productId;
        private String link;
        private Price price;
        private String format;
        @JsonProperty("is_current_product")
        private boolean isCurrentProduct;
        private List<NameValue> dimensions;
        private List<Link> images;
    }

    @Data
    public static class Price {
        private String symbol;
        private Double value;
        private String currency;
        private String raw;
    }

    public String getDescription() {
        return this.product.getDescription();
    }

    public List<String> getFeatureBullets() {
        return this.product.getFeatureBullets();
    }

    public List<Category> getCategoryList() {
        return Optional.ofNullable(this.product.getCategories()).orElseGet(ArrayList::new);
    }

    public String getBrand() {
        return this.product.getBrand();
    }

    public List<Video> getVideos() {
        return Optional.ofNullable(this.product.getVideos()).orElseGet(ArrayList::new);
    }

    public List<Document> getDocuments() {
        return Optional.ofNullable(this.product.getDocuments()).orElseGet(ArrayList::new);
    }

    public String getProductId() {
        return this.product.getProductId();
    }

    public String getTitle() {
        return this.product.getTitle();
    }

    public BuyboxWinner getBuyboxWinner() {
        return this.product.getBuyboxWinner();
    }

    public String getLink() {
        return this.product.getLink();
    }

    public List<NameValue> getAttributes() {
        return this.product.getAttributes();
    }

    public List<NameValue> getSpecifications() {
        return this.product.getSpecifications();
    }

    public String getWeight() {
        return this.product.getWeight();
    }

    public String getDimensions() {
        return this.product.getDimensions();
    }

    public String getShippingWeight() {
        return this.product.getShippingWeight();
    }

    public String getMainImage() {
        return this.product.getMainImage().getRaw();
    }

    public List<Link> getImages() {
        return this.product.getImages();
    }

    public Double getRating() {
        return this.product.getRating();
    }

    public List<String> getKeywordsList() {
        return this.product.getKeywordsList();
    }
}
