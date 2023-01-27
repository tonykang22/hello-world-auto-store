package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.github.kingwaggs.productanalyzer.exception.DeliveryPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryAgencyServiceTest {

    @InjectMocks
    private DeliveryAgencyService deliveryAgencyService;

    @Mock
    private DeliveryPriceCalculator deliveryPriceCalculator;

    @Test
    void calculateDeliveryPrice() throws DeliveryPriceException {
        // given
        Double expectedDeliveryPrice = 77.5D;
        List<Double> dimensions = List.of(66.0, 12.0, 10.0);
        Double weight = 20.0;

        when(deliveryPriceCalculator.getDEFastDeliveryPrice(any())).thenReturn(expectedDeliveryPrice);

        // when
        Double actualDeliveryPrice = deliveryAgencyService.getDeliveryPrice(dimensions, weight);

        // then
        assertEquals(expectedDeliveryPrice, actualDeliveryPrice);
    }
}