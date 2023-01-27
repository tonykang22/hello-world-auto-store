package com.github.kingwaggs.productmanager.common.service;

import com.github.kingwaggs.productmanager.common.domain.product.AmazonSyncProduct;
import com.github.kingwaggs.productmanager.common.domain.response.AmazonProductResponse;
import com.github.kingwaggs.productmanager.coupang.domain.CoupangPrice;
import com.github.kingwaggs.productmanager.coupang.exception.OutOfStockException;
import com.github.kingwaggs.productmanager.coupang.service.CoupangPriceService;
import com.github.kingwaggs.productmanager.deliveryagency.exception.DeliveryPriceException;
import com.github.kingwaggs.productmanager.deliveryagency.service.DeliveryAgencyService;
import com.github.kingwaggs.productmanager.exchangeagency.exception.ExchangeRateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonSyncProductFactoryService {

    private static final int MAXIMUM_BUY_COUNT = 5;
    private static final int OUT_OF_STOCK = 0;
    private static final double OUNCE_TO_POUND = 0.0625;

    private final DeliveryAgencyService deliveryAgencyService;
    private final CoupangPriceService coupangPriceService;

    public AmazonSyncProduct create(AmazonProductResponse product) throws ExchangeRateException, InterruptedException {
        log.info("Mapping AmazonSyncProduct [ASIN:{}] start.", product.getProductId());
        String asin = product.getProductId();
        Double price = mapPrice(product);
        Double deliveryPrice = calculateDeliveryPrice(product);
        Thread.sleep(10);
        CoupangPrice coupangPrice = getCoupangPrice(price, deliveryPrice, asin);
        log.info("Mapping AmazonSyncProduct [ASIN:{}] completed.", product.getProductId());
        return AmazonSyncProduct.builder()
                .asin(asin)
                .sourcingPrice(price)
                .originalPrice(coupangPrice.getOriginalPrice())
                .salePrice(coupangPrice.getSalePrice())
                .availability(mapAvailability(product))
                .build();
    }

    private List<Double> mapDimensions(AmazonProductResponse dto) {
        String dimensions = dto.getDimensions();
        if (dimensions == null) {
            List<AmazonProductResponse.NameValue> specifications = dto.getSpecifications();
            List<AmazonProductResponse.NameValue> dimensionsEntry = specifications
                    .stream()
                    .filter(nv -> nv.getName().contains("Dimensions")).collect(Collectors.toList());
            if (dimensionsEntry.isEmpty()) {
                return Collections.emptyList();
            }
            dimensions = dimensionsEntry
                    .stream()
                    .findFirst()
                    .get()
                    .getValue();
        }
        return Arrays.stream(dimensions.split(";")[0].split("x"))
                .map(s -> Double.parseDouble(s.replaceAll("[^\\d.]", "")))
                .collect(Collectors.toList());
    }

    private Double mapWeight(AmazonProductResponse dto) {
        String originalWeight = dto.getWeight();
        if (originalWeight != null) {
            return Double.parseDouble(originalWeight.replaceAll("[^\\d.]", ""));
        }

        List<AmazonProductResponse.NameValue> specifications = dto.getSpecifications();

        Double poundsWeightByName = getWeightByName(specifications, "Pounds");
        if (poundsWeightByName != null) {
            return poundsWeightByName;
        }
        Double ouncesWeightByName = getWeightByName(specifications, "Ounces");
        if (ouncesWeightByName != null) {
            return ounceToPound(ouncesWeightByName);
        }
        Double ouncesWeightByValue = getWeightByValue(specifications, "Ounces");
        if (ouncesWeightByValue != null) {
            return ounceToPound(ouncesWeightByValue);
        }
        return getWeightByValue(specifications, "Pounds");
    }

    private Double getWeightByName(List<AmazonProductResponse.NameValue> specifications, String weightUnit) {
        List<AmazonProductResponse.NameValue> weightEntry = specifications
                .stream()
                .filter(nv -> nv.getName().contains("Weight"))
                .collect(Collectors.toList());
        if (weightEntry.isEmpty()) {
            return null;
        }
        String originalWeight = weightEntry.stream()
                .findFirst()
                .get()
                .getValue();
        return originalWeight.contains(weightUnit) ? Double.parseDouble(originalWeight.replaceAll("[^\\d.]", "")) : null;
    }

    private Double getWeightByValue(List<AmazonProductResponse.NameValue> specifications, String weightUnit) {
        List<AmazonProductResponse.NameValue> weightEntry = specifications.stream()
                .filter(nv -> nv.getValue().contains(weightUnit))
                .collect(Collectors.toList());
        if (weightEntry.isEmpty()) {
            return null;
        }
        String originalWeight = weightEntry.stream()
                .findFirst()
                .get()
                .getValue();
        String[] splitValue = originalWeight.split(";");
        return Double.parseDouble(splitValue[splitValue.length - 1].replaceAll("[^\\d.]", ""));
    }

    private Double ounceToPound(Double ounce) {
        return ounce * OUNCE_TO_POUND;
    }

    private Double calculateDeliveryPrice(AmazonProductResponse product) throws InterruptedException {
        try {
            log.info("Calculating delivery price start. ASIN : {}", product.getProductId());
            List<Double> dimensions = mapDimensions(product);
            Double weight = mapWeight(product);
            Double deliveryPrice = deliveryAgencyService.getDeliveryPrice(dimensions, weight);
            Thread.sleep(10);
            return deliveryPrice;
        } catch (DeliveryPriceException exception) {
            log.error("Exception occurred during calculating delivery price", exception);
            return null;
        }
    }

    private Double mapPrice(AmazonProductResponse dto) {
        AmazonProductResponse.BuyboxWinner buyboxWinner = dto.getBuyboxWinner();
        return buyboxWinner.getPrice()
                .getValue();
    }

    private CoupangPrice getCoupangPrice(Double price, Double deliveryPrice, String asin) throws ExchangeRateException, InterruptedException {
        CoupangPrice coupangPrice = coupangPriceService.createPrices(price, deliveryPrice, asin);
        Thread.sleep(10);
        return coupangPrice;
    }

    private int mapAvailability(AmazonProductResponse dto) {
        try {
            AmazonProductResponse.BuyboxWinner buyboxWinner = dto.getBuyboxWinner();
            AmazonProductResponse.Availability availability = buyboxWinner.getAvailability();
            String availabilityRaw = availability.getRaw();
            return createMaximumBuyCount(availabilityRaw);
        } catch (OutOfStockException exception) {
            log.error("OutOfStockException occurred during getting stock count. Set stock_count to 0. ASIN: {}",
                    dto.getProductId());
            return OUT_OF_STOCK;
        }
    }

    private int createMaximumBuyCount(String availability) throws OutOfStockException {
        if (availability == null) {
            throw new OutOfStockException();
        }

        String availabilityLowerCase = availability.toLowerCase();
        if (!availabilityLowerCase.contains("in stock")) {
            throw new OutOfStockException();
        }

        String stockCount = availabilityLowerCase.replaceAll("[\\D]", "");
        if (stockCount.equals("")) {
            return MAXIMUM_BUY_COUNT;
        }

        int stockCountInt = Integer.parseInt(stockCount);
        if (stockCountInt < 5) {
            throw new OutOfStockException();
        }
        return MAXIMUM_BUY_COUNT;
    }
}