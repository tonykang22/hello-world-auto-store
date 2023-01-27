package com.github.kingwaggs.productmanager.common.domain.product;

import com.github.kingwaggs.productmanager.common.domain.Vendor;

import java.util.List;

public interface SourcingProduct {
    Vendor getSourcingVendor();
    String getBrand();
    String getProductName();
    String getDescription();

    String getDetailedDescriptionPage();

    List<String> getFeatureBullets();
    List<String> getKeywordsList();

    List<String> getVideos();

    List<String> getDocuments();

    List<SourcingItem> getItems();
}
