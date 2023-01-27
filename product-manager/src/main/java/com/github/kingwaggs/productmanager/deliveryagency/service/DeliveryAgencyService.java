package com.github.kingwaggs.productmanager.deliveryagency.service;

import com.github.kingwaggs.productmanager.deliveryagency.exception.DeliveryPriceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryAgencyService {

    private static final Double WEIGHT_STANDARD_IN_LB = 200.0;

    // 부피 무게 기준값(https://www.ohmyzip.com/how-it-works/shipping-fee/how-to-apply-volume-weight/)
    private static final int VOLUME_WEIGHT_DIVIDED_VALUE = 166;
    private static final int EXPECTED_ERROR_WEIGHT = 1;

    private final DeliveryPriceCalculator deliveryPriceCalculator;

    public Double getDeliveryPrice(List<Double> dimensions, Double productWeight) throws DeliveryPriceException {
        // volumeWeight : 부피 무게
        Double volumeWeight = createVolumeWeight(dimensions);

        Double biggerWeight = getBiggerWeight(productWeight, volumeWeight);
        if (biggerWeight == null) {
            throw new DeliveryPriceException("Cannot calculate delivery price. All values are NULL.");
        }
        Double ceiledWeight = Math.ceil(biggerWeight) + EXPECTED_ERROR_WEIGHT;
        if (ceiledWeight > WEIGHT_STANDARD_IN_LB) {
            throw new DeliveryPriceException(String.format("Cannot calculate delivery price. Product reached maximum weight limit(%s lb)", WEIGHT_STANDARD_IN_LB));
        }
        Double deliveryPrice = deliveryPriceCalculator.getDEFastDeliveryPrice(ceiledWeight);
        log.info("Calculated delivery price. (volumeWeight : {}, productWeight : {}, ceiledWeight : {}, deliveryPrice : ${})",
                volumeWeight,
                productWeight,
                ceiledWeight,
                deliveryPrice);
        return deliveryPrice;
    }

    private Double getBiggerWeight(Double weightWeight, Double volumeWeight) {
        if (weightWeight == null || volumeWeight == null) {
            return Stream.of(weightWeight, volumeWeight)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return Math.max(weightWeight, volumeWeight);
    }

    private Double createVolumeWeight(List<Double> dimensions) {
        if (dimensions == null) {
            log.info("All dimensions are null. Return null.");
            return null;
        }
        Double volume = 1.0;
        for (Double dimension : dimensions) {
            volume *= dimension;
        }
        double volumeWeight = volume / VOLUME_WEIGHT_DIVIDED_VALUE;
        String logFormat = "Created volume weight - (dimensions : %s, volumeWeight : %s)";
        String logMessage = String.format(logFormat, dimensions.stream().map(Object::toString).collect(Collectors.joining(" x ")), volumeWeight);
        log.info(logMessage);
        return volumeWeight;
    }

}
