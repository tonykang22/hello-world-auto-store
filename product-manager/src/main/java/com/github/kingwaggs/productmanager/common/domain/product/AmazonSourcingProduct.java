package com.github.kingwaggs.productmanager.common.domain.product;

import com.github.kingwaggs.productmanager.common.domain.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmazonSourcingProduct implements SourcingProduct, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Vendor SOURCING_VENDOR = Vendor.AMAZON_US;

    private String productName;
    private String description;
    private List<String> featureBullets;
    private List<String> keywordsList;
    private String brand;
    private List<String> videos;
    private List<Category> categories;
    private List<String> documents;
    private String detailedDescriptionPage;
    private List<Variant> variants;

    @Override
    public List<SourcingItem> getItems() {
        return new ArrayList<>(variants);
    }

    @Override
    public List<String> getVideos() {
        return this.videos;
    }

    @Override
    public Vendor getSourcingVendor() {
        return SOURCING_VENDOR;
    }

    @Override
    public List<String> getFeatureBullets() {
        return new ArrayList<>(CollectionUtils.emptyIfNull(featureBullets));
    }

    @Override
    public List<String> getDocuments() {
        return this.documents;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Variant implements SourcingItem, Serializable {
        private String asin;
        private String title;
        private BuyboxWinner buyboxWinner;
        private String link;
        private Double weight;
        private List<Double> dimensions;
        private Double shippingWeight;
        private String mainImage;
        private List<String> images;
        private Double rating;
        private Map<String, String> attributes;
        private Map<String, String> specifications;
        private Double deliveryPrice;
        private int availability;

        @Override
        public Map<String, String> getAttributes() {
            return Objects.requireNonNullElse(attributes, Collections.emptyMap());
        }

        @Override
        public Map<String, String> getSpecifications() {
            return Objects.requireNonNullElse(specifications, Collections.emptyMap());
        }

        @Override
        public String getItemId() {
            return this.asin;
        }

        @Override
        public Double getPrice() {
            return Optional.ofNullable(buyboxWinner)
                    .map(BuyboxWinner::getPrice)
                    .map(Price::getValue)
                    .orElse(null);
        }

        @Override
        public Double getWeight() {
            if (weight == null || shippingWeight == null) {
                return Stream.of(weight, shippingWeight)
                        .filter(Objects::nonNull)
                        .findFirst().orElse(null);
            }
            return Math.max(weight, shippingWeight);
        }

        @Override
        public String getExternalVendorSku() {
            return String.join(EXTERNAL_VENDOR_SKU_DELIMITER, SOURCING_VENDOR.toString(), this.asin);
        }

        @Override
        public Double getDeliveryPrice() {
            return this.deliveryPrice;
        }

        @Override
        public int getAvailability() {
            return this.availability;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuyboxWinner implements Serializable {

        private String offerId;
        private Availability availability;
        private boolean unqualifiedBuyBox;
        private Price price;
        private Shipping shipping;

    }



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Shipping implements Serializable {

        private String raw;
        private Double value;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Availability implements Serializable {

        private String type;
        private String raw;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category implements Serializable {

        private String name;
        private String link;
        private String categoryId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price implements Serializable {

        private String symbol;
        private Double value;
        private String currency;
        private String raw;

    }

}