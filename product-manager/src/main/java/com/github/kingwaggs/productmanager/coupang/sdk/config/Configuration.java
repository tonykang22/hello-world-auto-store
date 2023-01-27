package com.github.kingwaggs.productmanager.coupang.sdk.config;

import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApiClient;

public class Configuration {
    private static CoupangMarketPlaceApiClient defaultApiClient = new CoupangMarketPlaceApiClient();

    public Configuration() {
    }

    public static CoupangMarketPlaceApiClient getDefaultApiClient() {
        return defaultApiClient;
    }

    public static void setDefaultApiClient(CoupangMarketPlaceApiClient apiClient) {
        defaultApiClient = apiClient;
    }
}
