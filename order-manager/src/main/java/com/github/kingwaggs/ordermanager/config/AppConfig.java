package com.github.kingwaggs.ordermanager.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.kingwaggs.ordermanager.domain.EnumConverter;
import com.github.kingwaggs.ordermanager.domain.OrderContext;
import com.github.kingwaggs.ordermanager.domain.RequestInterceptor;
import com.github.kingwaggs.ordermanager.domain.RunnerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

    private final RequestInterceptor customRequestInterceptor;

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

    @Bean
    public OrderContext orderContext() {
        return OrderContext.init(RunnerStatus.STOP);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EnumConverter.StringToRunningStatus());
        registry.addConverter(new EnumConverter.StringToSourceVendor());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customRequestInterceptor)
                .addPathPatterns("/**");
    }
}
