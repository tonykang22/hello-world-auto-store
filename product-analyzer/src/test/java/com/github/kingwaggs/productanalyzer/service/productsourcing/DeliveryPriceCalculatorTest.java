package com.github.kingwaggs.productanalyzer.service.productsourcing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeliveryPriceCalculatorTest {

    private final DeliveryPriceCalculator deliveryPriceCalculator = new DeliveryPriceCalculator();

    @Test
    void readDEFastDeliveryPriceMap() {
        // given
        double weight = 5.0;

        // when
        Double price = deliveryPriceCalculator.getDEFastDeliveryPrice(weight);

        // then
        assertNotNull(price);
    }

    @Test
    void readCADeliveryPriceMap() {
        // given
        double weight = 30.0;

        // when
        Double price = deliveryPriceCalculator.getCADeliveryPrice(weight);

        // then
        assertNotNull(price);
    }

}