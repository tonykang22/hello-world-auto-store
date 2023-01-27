package com.github.kingwaggs.productanalyzer.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class AmazonProductResponseDto {

    private Product product;

    @Data
    public static class Product {
        @JsonProperty("buybox_winner")
        BuyboxWinner buyboxWinner;
        String title;
        @JsonProperty("asin")
        String productId;
        String link;
        String description;
        @JsonProperty("feature_bullets")
        List<String> featureBullets;
        @JsonProperty("keywords_list")
        List<String> keywordsList;
        @JsonProperty("main_image")
        Link mainImage;
        List<Link> images;
        List<Video> videos;
        @JsonProperty("a_plus_content")
        PlusContent plusContent;
        List<NameValue> attributes;
        List<NameValue> specifications;
        List<Category> categories;
        List<Document> documents;
        List<Variant> variants;
        Double rating;
        String brand;
        String weight;
        String dimensions;
        @JsonProperty("shipping_weight")
        String shippingWeight;
    }

    @Data
    public static class BuyboxWinner {
        @JsonProperty("offer_id")
        String offerId;
        Availability availability;
        @JsonProperty("unqualified_buy_box")
        boolean unqualifiedBuyBox;
        Price price;
        Shipping shipping;
    }

    @Data
    public static class Shipping {
        String raw;
        Double value;
    }

    @Data
    public static class Availability {
        String type;
        String raw;
    }

    @Data
    public static class Document {
        String name;
        String link;
    }

    @Data
    public static class Category {
        String name;
        String link;
        @JsonProperty("category_id")
        String categoryId;
    }

    @Data
    public static class PlusContent {
        @JsonProperty("has_a_plus_content")
        boolean hasAPlusContent;
        @JsonProperty("has_brand_story")
        boolean hasBrandStory;
        @JsonProperty("third_party")
        boolean thirdParty;
        @JsonProperty("company_logo")
        String companyLogo;
        @JsonProperty("company_description_text")
        String companyDescriptionText;
        String body;
        @JsonProperty("body_text")
        String bodyText;
        @JsonProperty("brand_story")
        BrandStory brandStory;
    }

    @Data
    public static class BrandStory {
        @JsonProperty("brand_logo")
        String brandLogo;
        @JsonProperty("hero_image")
        String heroImage;
        String description;

    }

    @Data
    public static class Video {
        String title;
        @JsonProperty("group_id")
        String groupId;
        @JsonProperty("group_type")
        String groupType;
        String link;
        @JsonProperty("duration_seconds")
        String durationSeconds;
        String width;
        String height;
        String thumbnail;
        @JsonProperty("is_hero_video")
        String isHeroVideo;
        String variant;
    }

    @Data
    public static class Link {
        String link;
    }

    @Data
    public static class NameValue {
        String name;
        String value;
    }

    @Data
    public static class Variant {
        String title;
        @JsonProperty("asin")
        String productId;
        String link;
        Price price;
        String format;
        @JsonProperty("is_current_product")
        boolean isCurrentProduct;
        List<NameValue> dimensions;
        List<Link> images;
    }

    @Data
    public static class Price {
        String symbol;
        Double value;
        String currency;
        String raw;
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
        return this.product.getMainImage().getLink();
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
