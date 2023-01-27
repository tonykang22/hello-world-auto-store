package com.github.kingwaggs.csmanager.sdk.coupang.config;

import com.github.kingwaggs.csmanager.sdk.coupang.service.CoupangMarketPlaceApiClient;

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
