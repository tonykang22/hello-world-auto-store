package com.github.kingwaggs.productanalyzer.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemScoutIndicatorDto {
    private final String searchTerm;
    private double intensityOfCompetition;
    private double proportionOfPackageProducts;
    private double proportionOfOverseasProducts;
    private double proportionOfActualPurchase;
    private double postingRateWithin1Year;
    private double averagePrice;
    private final List<String> relatedKeywords;
}
