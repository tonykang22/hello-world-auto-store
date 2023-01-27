package com.github.kingwaggs.productanalyzerv2.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemScoutIndicator {
    private String searchTerm;
    private Double intensityOfCompetition;
    private Double proportionOfPackageProducts;
    private Double proportionOfOverseasProducts;
    private Double proportionOfActualPurchase;
    private Double postingRateWithin1Year;
    private Double averagePrice;
}
