package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.product.SourcingItem;
import com.github.kingwaggs.productmanager.deliveryagency.service.DeliveryAgencyService;
import com.github.kingwaggs.productmanager.exchangeagency.exception.ExchangeRateException;
import com.github.kingwaggs.productmanager.exchangeagency.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangDeliveryService {

    private final ExchangeRateService exchangeRateService;

    public Double createDeliveryCharge(SourcingItem item) throws ExchangeRateException {
        Double originalDeliveryPrice = item.getDeliveryPrice();
        Double dollar2WonExchangeRate = exchangeRateService.getDollar2WonExchangeRate();
        Double rawDeliveryPriceInWon = originalDeliveryPrice * dollar2WonExchangeRate;
        Double ceiledDeliveryPrice = Math.ceil(rawDeliveryPriceInWon);
        Double scaledDeliveryCharge = createScaledPrice(ceiledDeliveryPrice);
        log.info("CreateDeliveryCharge Done. (itemId : {}, originalDeliveryPrice : {}, dollar2WonExchangeRate : {}, scaledDeliveryCharge : {})",
                item.getItemId(),
                originalDeliveryPrice,
                dollar2WonExchangeRate,
                scaledDeliveryCharge);
        return scaledDeliveryCharge;
    }

    public Double createDeliveryCharge(Double originalDeliveryPrice, String asin) throws ExchangeRateException {
        Double dollar2WonExchangeRate = exchangeRateService.getDollar2WonExchangeRate();
        Double rawDeliveryPriceInWon = originalDeliveryPrice * dollar2WonExchangeRate;
        Double ceiledDeliveryPrice = Math.ceil(rawDeliveryPriceInWon);
        Double scaledDeliveryCharge = createScaledPrice(ceiledDeliveryPrice);
        log.info("CreateDeliveryCharge Done. (ASIN : {}, originalDeliveryPrice : {}, dollar2WonExchangeRate : {}, scaledDeliveryCharge : {})",
                asin,
                originalDeliveryPrice,
                dollar2WonExchangeRate,
                scaledDeliveryCharge);
        return scaledDeliveryCharge;
    }

    private Double createScaledPrice(Double price) {
        // 5000 원 단위로 배송비 계산
        int quota = (int) (price / 5_000) + 1;
        return quota * 5_000.0;
    }

}
