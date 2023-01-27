package com.github.kingwaggs.ordermanager.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@ToString
@Component
@PropertySource("classpath:zinc.properties")
@RequiredArgsConstructor
public class ZincCredential {

    @Value("${zinc.retailer.id}")
    private String retailerId;

    @Value("${zinc.retailer.password}")
    private String retailerPassword;

    @Value("${zinc.first.name}")
    private String firstName;

    @Value("${zinc.last.name}")
    private String lastName;

    @Value("${zinc.address.one}")
    private String addressLine1;

    @Value("${zinc.address.two}")
    private String addressLine2;

    @Value("${zinc.address.zipcode}")
    private String zipcode;

    @Value("${zinc.address.city}")
    private String city;

    @Value("${zinc.address.state}")
    private String state;

    @Value("${zinc.address.country}")
    private String country;

    @Value("${zinc.phone.number}")
    private String phoneNumber;

    @Value("${zinc.webhook.success}")
    private String successWebhookUrl;

    @Value("${zinc.webhook.fail}")
    private String failWebhookUrl;

    @Value("${zinc.webhook.tracking}")
    private String trackingWebhookUrl;
}
