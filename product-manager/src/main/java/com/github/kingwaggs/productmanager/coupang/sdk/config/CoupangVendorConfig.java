package com.github.kingwaggs.productmanager.coupang.sdk.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@ToString
@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class CoupangVendorConfig {

    @Value("${coupang.vendor.displayed.language}")
    private String displayedLanguage;

    @Value("${coupang.vendor.access.key}")
    private String accessKey;

    @Value("${coupang.vendor.secret.key}")
    private String secretKey;

    @Value("${coupang.vendor.id}")
    private String vendorId;

    @Value("${coupang.vendor.user.id}")
    private String vendorUserId;
}
