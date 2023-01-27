package com.github.kingwaggs.ordermanager.coupangsdk.config;

import com.github.kingwaggs.ordermanager.coupangsdk.service.CoupangMarketPlaceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CoupangApiConfig {

    private final CoupangVendorConfig coupangVendorConfig;

    @Bean
    public CoupangMarketPlaceApi buildCoupangMarketPlaceApi() {
        return new CoupangMarketPlaceApi(coupangVendorConfig.getSecretKey(), coupangVendorConfig.getAccessKey(), false);
    }
}
