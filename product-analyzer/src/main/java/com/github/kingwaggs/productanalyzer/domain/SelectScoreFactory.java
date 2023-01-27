package com.github.kingwaggs.productanalyzer.domain;

import com.github.kingwaggs.productanalyzer.domain.dto.ItemScoutIndicatorDto;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SelectScoreFactory {
    private static final int HUNDRED_PERCENTAGE = 100;
    private static final int REMOVE_PERCENTAGE = 10;

    public List<SelectScore> create(List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList) {
        try {
            List<SelectScore> selectScoreList = new ArrayList<>();
            Threshold threshold = calculateThreshold(itemScoutIndicatorDtoList);
            List<ItemScoutIndicatorDto> filteredItemScoutIndicatorDtoList = itemScoutIndicatorDtoList
                    .stream()
                    .filter(itemScoutIndicatorsDto -> isSatisfyThreshold(itemScoutIndicatorsDto, threshold))
                    .collect(Collectors.toList());
            List<ItemScoutIndicatorDto> scaledItemScoutIndicatorList = MinMaxScaler.scaling(filteredItemScoutIndicatorDtoList);
            for (ItemScoutIndicatorDto scaledItemScoutIndicatorDto : scaledItemScoutIndicatorList) {
                selectScoreList.add(
                        SelectScore.builder()
                                .searchWord(scaledItemScoutIndicatorDto.getSearchTerm())
                                .score(calculateSelectScore(scaledItemScoutIndicatorDto))
                                .relatedKeywords(scaledItemScoutIndicatorDto.getRelatedKeywords())
                                .build()
                );
            }
            selectScoreList.sort(Comparator.comparingDouble(SelectScore::getScore).reversed());
            int rank = 1;
            for (SelectScore selectScore : selectScoreList) {
                selectScore.setRank(rank);
                rank++;
            }
            log.info("Calculate {} select scores successfully.", selectScoreList.size());
            return selectScoreList;
        } catch (Exception exception) {
            log.error("Exception occurred while calculating select score(msg : {}, cause : {})", exception.getMessage(), exception.getCause());
            return Collections.emptyList();
        }
    }

    private boolean isSatisfyThreshold(ItemScoutIndicatorDto itemScoutIndicatorDto, Threshold threshold) {
        if (isOverIOC(itemScoutIndicatorDto, threshold)
                || isOverPOPP(itemScoutIndicatorDto, threshold)
                || isUnderPOOP(itemScoutIndicatorDto, threshold)
                || isUnderPOAP(itemScoutIndicatorDto, threshold)
                || isUnderPRW1Y(itemScoutIndicatorDto, threshold)
                || isUnderAP(itemScoutIndicatorDto, threshold)) {
            return false;
        }
        return true;
    }

    private boolean isUnderAP(ItemScoutIndicatorDto itemScoutIndicatorDto, Threshold threshold) {
        return itemScoutIndicatorDto.getAveragePrice() <= threshold.thresholdOfAveragePrice;
    }

    private boolean isUnderPRW1Y(ItemScoutIndicatorDto itemScoutIndicatorDto, Threshold threshold) {
        return itemScoutIndicatorDto.getPostingRateWithin1Year() <= threshold.thresholdOfPostingRateWithin1Year;
    }

    private boolean isUnderPOAP(ItemScoutIndicatorDto itemScoutIndicatorDto, Threshold threshold) {
        return itemScoutIndicatorDto.getProportionOfActualPurchase() <= threshold.thresholdOfProportionOfActualPurchase;
    }

    private boolean isUnderPOOP(ItemScoutIndicatorDto itemScoutIndicatorDto, Threshold threshold) {
        return itemScoutIndicatorDto.getProportionOfOverseasProducts() <= threshold.thresholdOfProportionOfOverseasProducts;
    }

    private boolean isOverPOPP(ItemScoutIndicatorDto itemScoutIndicatorDto, Threshold threshold) {
        return itemScoutIndicatorDto.getProportionOfPackageProducts() >= threshold.thresholdOfProportionOfPackageProducts;
    }

    private boolean isOverIOC(ItemScoutIndicatorDto itemScoutIndicatorDto, Threshold threshold) {
        return itemScoutIndicatorDto.getIntensityOfCompetition() >= threshold.thresholdOfIntensityOfCompetition;
    }

    private Threshold calculateThreshold(List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList) {
        List<Double> intensityOfCompetitionList = itemScoutIndicatorDtoList.stream().map(ItemScoutIndicatorDto::getIntensityOfCompetition).collect(Collectors.toList());
        List<Double> proportionOfPackageProductsList = itemScoutIndicatorDtoList.stream().map(ItemScoutIndicatorDto::getProportionOfPackageProducts).collect(Collectors.toList());
        List<Double> proportionOfOverseasProductsList = itemScoutIndicatorDtoList.stream().map(ItemScoutIndicatorDto::getProportionOfOverseasProducts).collect(Collectors.toList());
        List<Double> proportionOfActualPurchaseList = itemScoutIndicatorDtoList.stream().map(ItemScoutIndicatorDto::getProportionOfActualPurchase).collect(Collectors.toList());
        List<Double> postingRateWithin1YearList = itemScoutIndicatorDtoList.stream().map(ItemScoutIndicatorDto::getPostingRateWithin1Year).collect(Collectors.toList());
        List<Double> averagePrice = itemScoutIndicatorDtoList.stream().map(ItemScoutIndicatorDto::getAveragePrice).collect(Collectors.toList());

        double thresholdOfIntensityOfCompetition = getTopValueOf(intensityOfCompetitionList);
        double thresholdOfProportionOfPackageProducts = getTopValueOf(proportionOfPackageProductsList);
        double thresholdOfProportionOfOverseasProducts = getBottomValueOf(proportionOfOverseasProductsList);
        double thresholdOfProportionOfActualPurchase = getBottomValueOf(proportionOfActualPurchaseList);
        double thresholdOfPostingRateWithin1Year = getBottomValueOf(postingRateWithin1YearList);
        double thresholdOfAveragePrice = getBottomValueOf(averagePrice);

        return Threshold.builder()
                .thresholdOfIntensityOfCompetition(thresholdOfIntensityOfCompetition)
                .thresholdOfProportionOfPackageProducts(thresholdOfProportionOfPackageProducts)
                .thresholdOfProportionOfOverseasProducts(thresholdOfProportionOfOverseasProducts)
                .thresholdOfProportionOfActualPurchase(thresholdOfProportionOfActualPurchase)
                .thresholdOfPostingRateWithin1Year(thresholdOfPostingRateWithin1Year)
                .thresholdOfAveragePrice(thresholdOfAveragePrice)
                .build();
    }

    private double getBottomValueOf(List<Double> doubleList) {
        int targetRank = doubleList.size() * REMOVE_PERCENTAGE / HUNDRED_PERCENTAGE;
        Collections.sort(doubleList);
        if (targetRank == 0) {
            return doubleList.get(0);
        }
        return doubleList.get(targetRank - 1);
    }

    private double getTopValueOf(List<Double> doubleList) {
        int targetRank = doubleList.size() * REMOVE_PERCENTAGE / HUNDRED_PERCENTAGE;
        doubleList.sort(Collections.reverseOrder());
        if (targetRank == 0) {
            return doubleList.get(0);
        }
        return doubleList.get(targetRank - 1);
    }

    private Double calculateSelectScore(ItemScoutIndicatorDto itemScoutIndicatorDto) {
        return (1 - itemScoutIndicatorDto.getIntensityOfCompetition()) * Weight.INTENSITY_OF_COMPETITION.getValue()
                + (1 - itemScoutIndicatorDto.getProportionOfPackageProducts()) * Weight.PROPORTION_OF_PACKAGE_PRODUCTS.getValue()
                + itemScoutIndicatorDto.getProportionOfOverseasProducts() * Weight.PROPORTION_OF_OVERSEAS_PRODUCTS.getValue()
                + itemScoutIndicatorDto.getProportionOfActualPurchase() * Weight.PROPORTION_OF_ACTUAL_PURCHASE.getValue()
                + itemScoutIndicatorDto.getPostingRateWithin1Year() * Weight.POSTING_RATE_WITHIN_1_YEAR.getValue()
                + itemScoutIndicatorDto.getAveragePrice() * Weight.AVERAGE_PRICE.getValue();
    }

    @Data
    @Builder
    static class Threshold {
        private final double thresholdOfIntensityOfCompetition;
        private final double thresholdOfProportionOfPackageProducts;
        private final double thresholdOfProportionOfOverseasProducts;
        private final double thresholdOfProportionOfActualPurchase;
        private final double thresholdOfPostingRateWithin1Year;
        private final double thresholdOfAveragePrice;
    }

    static class MinMaxScaler {
        public static List<ItemScoutIndicatorDto> scaling(List<ItemScoutIndicatorDto> indicatorsDtoList) {
            Min min = calculateMin(indicatorsDtoList);
            Max max = calculateMax(indicatorsDtoList);
            return scalingWithMinMax(indicatorsDtoList, min, max);
        }

        private static List<ItemScoutIndicatorDto> scalingWithMinMax(List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList, Min min, Max max) {
            for (ItemScoutIndicatorDto itemScoutIndicatorDto : itemScoutIndicatorDtoList) {
                itemScoutIndicatorDto.setIntensityOfCompetition(
                        getScaledValue(itemScoutIndicatorDto.getIntensityOfCompetition(), min.getMinOfIntensityOfCompetition(), max.getMaxOfIntensityOfCompetition())
                );
                itemScoutIndicatorDto.setProportionOfPackageProducts(
                        getScaledValue(itemScoutIndicatorDto.getProportionOfPackageProducts(), min.getMinOfProportionOfPackageProducts(), max.getMaxOfProportionOfPackageProducts())
                );
                itemScoutIndicatorDto.setProportionOfOverseasProducts(
                        getScaledValue(itemScoutIndicatorDto.getProportionOfOverseasProducts(), min.getMinOfProportionOfOverseasProducts(), max.getMaxOfProportionOfOverseasProducts())
                );
                itemScoutIndicatorDto.setProportionOfActualPurchase(
                        getScaledValue(itemScoutIndicatorDto.getProportionOfActualPurchase(), min.getMinOfProportionOfActualPurchase(), max.getMaxOfProportionOfActualPurchase())
                );
                itemScoutIndicatorDto.setPostingRateWithin1Year(
                        getScaledValue(itemScoutIndicatorDto.getPostingRateWithin1Year(), min.getMinOfPostingRateWithin1Year(), max.getMaxOfPostingRateWithin1Year())
                );
                itemScoutIndicatorDto.setAveragePrice(
                        getScaledValue(itemScoutIndicatorDto.getAveragePrice(), min.getMinOfAveragePrice(), max.getMaxOfAveragePrice())
                );
            }
            return itemScoutIndicatorDtoList;
        }

        private static double getScaledValue(double value, double min, double max) {
            if (min == max) {
                return 1.0;
            }
            return (value - min) / (max - min);
        }

        private static Min calculateMin(List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList) {
            double minOfIntensityOfCompetition = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getIntensityOfCompetition).min().orElse(0);
            double minOfProportionOfPackageProducts = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getProportionOfPackageProducts).min().orElse(0);
            double minOfProportionOfOverseasProducts = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getProportionOfOverseasProducts).min().orElse(0);
            double minOfProportionOfActualPurchase = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getProportionOfActualPurchase).min().orElse(0);
            double minOfPostingRateWithin1Year = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getPostingRateWithin1Year).min().orElse(0);
            double minOfAveragePrice = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getAveragePrice).min().orElse(0);

            return Min.builder()
                    .minOfIntensityOfCompetition(minOfIntensityOfCompetition)
                    .minOfProportionOfPackageProducts(minOfProportionOfPackageProducts)
                    .minOfProportionOfOverseasProducts(minOfProportionOfOverseasProducts)
                    .minOfProportionOfActualPurchase(minOfProportionOfActualPurchase)
                    .minOfPostingRateWithin1Year(minOfPostingRateWithin1Year)
                    .minOfAveragePrice(minOfAveragePrice)
                    .build();
        }

        private static Max calculateMax(List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList) {
            double maxOfIntensityOfCompetition = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getIntensityOfCompetition).max().orElse(1);
            double maxOfProportionOfPackageProducts = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getProportionOfPackageProducts).max().orElse(1);
            double maxOfProportionOfOverseasProducts = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getProportionOfOverseasProducts).max().orElse(1);
            double maxOfProportionOfActualPurchase = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getProportionOfActualPurchase).max().orElse(1);
            double maxOfPostingRateWithin1Year = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getPostingRateWithin1Year).max().orElse(1);
            double maxOfAveragePrice = itemScoutIndicatorDtoList.stream().mapToDouble(ItemScoutIndicatorDto::getAveragePrice).max().orElse(1);

            return Max.builder()
                    .maxOfIntensityOfCompetition(maxOfIntensityOfCompetition)
                    .maxOfProportionOfPackageProducts(maxOfProportionOfPackageProducts)
                    .maxOfProportionOfOverseasProducts(maxOfProportionOfOverseasProducts)
                    .maxOfProportionOfActualPurchase(maxOfProportionOfActualPurchase)
                    .maxOfPostingRateWithin1Year(maxOfPostingRateWithin1Year)
                    .maxOfAveragePrice(maxOfAveragePrice)
                    .build();
        }

        @Data
        @Builder
        static class Max {
            private final Double maxOfIntensityOfCompetition;
            private final Double maxOfProportionOfPackageProducts;
            private final Double maxOfProportionOfOverseasProducts;
            private final Double maxOfProportionOfActualPurchase;
            private final Double maxOfPostingRateWithin1Year;
            private final Double maxOfAveragePrice;
        }

        @Data
        @Builder
        static class Min {
            private final Double minOfIntensityOfCompetition;
            private final Double minOfProportionOfPackageProducts;
            private final Double minOfProportionOfOverseasProducts;
            private final Double minOfProportionOfActualPurchase;
            private final Double minOfPostingRateWithin1Year;
            private final Double minOfAveragePrice;
        }

    }

    enum Weight {
        INTENSITY_OF_COMPETITION(0.3),
        PROPORTION_OF_PACKAGE_PRODUCTS(0.05),
        PROPORTION_OF_OVERSEAS_PRODUCTS(0.3),
        PROPORTION_OF_ACTUAL_PURCHASE(0.1),
        POSTING_RATE_WITHIN_1_YEAR(0.05),
        AVERAGE_PRICE(0.2);

        private final double value;

        public double getValue() {
            return value;
        }

        Weight(double value) {
            this.value = value;
        }
    }
}
