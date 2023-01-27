package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.coupang.domain.CoupangPrice;
import com.github.kingwaggs.productmanager.exchangeagency.exception.ExchangeRateException;
import com.github.kingwaggs.productmanager.exchangeagency.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoupangPriceServiceTest {

    @InjectMocks
    private CoupangPriceService coupangPriceService;

    @Mock
    private CoupangDeliveryService coupangDeliveryService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Test
    void createPricesForSync() throws ExchangeRateException {
        // given
        Double sourcingPrice = 50D;
        Double originalDeliveryPrice = 10D;
        String asin = "B0B5WF2HX9";

        when(exchangeRateService.getDollar2WonExchangeRate()).thenReturn(1400D);
        when(coupangDeliveryService.createDeliveryCharge(anyDouble(), anyString())).thenReturn(originalDeliveryPrice * 1400D);

        // when
        CoupangPrice prices = coupangPriceService.createPrices(sourcingPrice, originalDeliveryPrice, asin);

        // then
        assertNotNull(prices);
        assertEquals(119_000, prices.getOriginalPrice());
        assertEquals(117_000, prices.getSalePrice());
    }

}