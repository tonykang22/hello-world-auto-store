package com.github.kingwaggs.productanalyzer.domain.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

@JsonDeserialize(as = AmazonSourcingProduct.Variant.class)
public interface SourcingItem {

    String EXTERNAL_VENDOR_SKU_DELIMITER = ":";

    String getItemId();
    String getTitle();
    Double getPrice();
    Double getWeight();
    List<Double> getDimensions();
    String getMainImage();
    List<String> getImages();
    Map<String, String> getAttributes();
    Map<String, String> getSpecifications();
    String getExternalVendorSku();
    Double getDeliveryPrice();
    int getAvailability();
}
