package com.github.kingwaggs.productanalyzer.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.kingwaggs.productanalyzer.domain.SourcingContext;
import com.github.kingwaggs.productanalyzer.util.EnumConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public SourcingContext sourcingContext() {
        return SourcingContext.createInitialContext();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMinutes(3))
                .setReadTimeout(Duration.ofMinutes(3))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EnumConverter());
    }
}
