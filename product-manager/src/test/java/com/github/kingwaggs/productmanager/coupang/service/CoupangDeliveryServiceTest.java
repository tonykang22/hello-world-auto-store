package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productmanager.config.TestConfig;
import com.github.kingwaggs.productmanager.exchangeagency.exception.ExchangeRateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class CoupangDeliveryServiceTest {

    @Autowired
    private CoupangDeliveryService coupangDeliveryService;

    @Test
    void createDeliveryCharge() throws ExchangeRateException {
        // Arrange
        AmazonSourcingProduct.Variant item = mock(AmazonSourcingProduct.Variant.class);
        when(item.getDeliveryPrice()).thenReturn(10D);

        // Act
        Double actual = coupangDeliveryService.createDeliveryCharge(item);

        // Assert
        assertNotNull(actual);
    }

    @Test
    void createDeliveryChargeForSync() throws ExchangeRateException {
        // Arrange
        Double originalPrice = 50D;
        String asin = "AB24X32";

        // Act
        Double actual = coupangDeliveryService.createDeliveryCharge(originalPrice, asin);

        // Assert
        assertNotNull(actual);
    }

}