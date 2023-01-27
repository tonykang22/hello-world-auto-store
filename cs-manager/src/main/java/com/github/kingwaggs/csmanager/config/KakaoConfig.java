package com.github.kingwaggs.csmanager.config;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:kakao.properties")
public class KakaoConfig {

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${kakao.secret.key}")
    private String secretKey;

    @Value("${base.url}")
    private String baseUrl;

    @Bean
    public DefaultMessageService defaultMessageService() {
        return NurigoApp.INSTANCE.initialize(apiKey, secretKey, baseUrl);
    }
}
