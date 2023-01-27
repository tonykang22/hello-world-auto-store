package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.product.SourcingItem;
import com.github.kingwaggs.productmanager.coupang.domain.CoupangPrice;
import com.github.kingwaggs.productmanager.deliveryagency.exception.DeliveryPriceException;
import com.github.kingwaggs.productmanager.exchangeagency.exception.ExchangeRateException;
import com.github.kingwaggs.productmanager.exchangeagency.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangPriceService {

    private static final double ITEM_MARGIN_POLICY = 0.2;
    private static final double ITEM_SALE_POLICY = 0.17;
    private static final double ESTIMATED_TAX_PERCENTAGE = 0.15;
    private static final int DELIVERY_AGENT_COMMISSION = 3;
    private static final int GENERAL_CUSTOMS_FEE = 1;

    private final ExchangeRateService exchangeRateService;
    private final CoupangDeliveryService coupangDeliveryService;

    public CoupangPrice createPrices(SourcingItem sourcingItem) throws ExchangeRateException, DeliveryPriceException {
        Double sourcingPrice = sourcingItem.getPrice();
        Double taxAddedPrice = sourcingPrice * (1 + ESTIMATED_TAX_PERCENTAGE) + DELIVERY_AGENT_COMMISSION + GENERAL_CUSTOMS_FEE;
        Double dollar2WonExchangeRate = exchangeRateService.getDollar2WonExchangeRate();
        Double taxAddedPriceInWon = taxAddedPrice * dollar2WonExchangeRate;
        Double ceiledPrice = Math.ceil(taxAddedPriceInWon);

        Double deliveryCharge = coupangDeliveryService.createDeliveryCharge(sourcingItem);

        Double marginedPrice = ceiledPrice * (1 + ITEM_MARGIN_POLICY) + dollar2WonExchangeRate;
        Double originalPrice = getScaledPrice(marginedPrice) + deliveryCharge;

        Double saledPrice = ceiledPrice * (1 + ITEM_SALE_POLICY) + dollar2WonExchangeRate;
        Double salePrice = getScaledPrice(saledPrice) + deliveryCharge;
        log.info("Created coupang prices. (itemId: {}, sourcingPrice : ${}, taxAddedPrice : ${}, taxAddedPriceInWon : ₩{}, deliveryCharge : ₩{}, originalPrice : ₩{}, salePrice : ₩{})",
                sourcingItem.getItemId(),
                sourcingPrice,
                taxAddedPrice,
                taxAddedPriceInWon,
                deliveryCharge,
                originalPrice,
                salePrice);
        return new CoupangPrice(originalPrice, salePrice);
    }

    public CoupangPrice createPrices(Double sourcingPrice, Double originalDeliveryPrice, String asin) throws ExchangeRateException {
        log.info("Creating coupang prices start. ASIN : {}", asin);
        Double taxAddedPrice = sourcingPrice * (1 + ESTIMATED_TAX_PERCENTAGE) + DELIVERY_AGENT_COMMISSION + GENERAL_CUSTOMS_FEE;
        Double dollar2WonExchangeRate = exchangeRateService.getDollar2WonExchangeRate();
        Double taxAddedPriceInWon = taxAddedPrice * dollar2WonExchangeRate;
        Double ceiledPrice = Math.ceil(taxAddedPriceInWon);

        Double deliveryCharge = coupangDeliveryService.createDeliveryCharge(originalDeliveryPrice, asin);

        Double marginedPrice = ceiledPrice * (1 + ITEM_MARGIN_POLICY) + dollar2WonExchangeRate;
        Double originalPrice = getScaledPrice(marginedPrice) + deliveryCharge;

        Double saledPrice = ceiledPrice * (1 + ITEM_SALE_POLICY) + dollar2WonExchangeRate;
        Double salePrice = getScaledPrice(saledPrice) + deliveryCharge;
        log.info("Created coupang prices. (ASIN: {}, sourcingPrice : ${}, taxAddedPrice : ${}, taxAddedPriceInWon : ₩{}, deliveryCharge : ₩{}, originalPrice : ₩{}, salePrice : ₩{})",
                asin,
                sourcingPrice,
                taxAddedPrice,
                taxAddedPriceInWon,
                deliveryCharge,
                originalPrice,
                salePrice);
        return new CoupangPrice(originalPrice, salePrice);
    }

    private Double getScaledPrice(Double price) {
        // 1000 원 단위로 배송비 계산
        int quota = (int) (price / 1_000) + 1;
        return quota * 1_000.0;
    }
}
