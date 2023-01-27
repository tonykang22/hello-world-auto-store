package com.github.kingwaggs.productanalyzerv2.domain.response.itemscout;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class StatResponse {

    private String status;
    @JsonProperty("data")
    private Stat stat;

    @Getter
    @ToString
    @NoArgsConstructor
    public static class Stat {
        private String mainProductImageUrl;
        private From1Page statsFrom1Page;
        private FromNPay statsFromNPay;
        private Double tieProductPercent;
        private String tieProductPercentDesc;
        private Double overseaProductPercent;
        private String overseaProductPercentDesc;
        private Double soldProductPercent;
        private String soldProductPercentDesc;
        private Double competitionTotalScore;
        private String competitionTotalScoreDesc;
        private Integer adEfficiencyScore;
        private String adEfficiencyScoreDesc;
        private Integer totalSearchCount;
        private Integer searchCountPC;
        private Integer searchCountMobile;
        private Integer totalProductCount;
        private Double clickCount;
        private Integer clickCountForShowing;
        private Integer averagePrice;
        private Integer averageAdPrice;
        private Double competitionIntensity;
        private String competitionIntensityGrade;
        private String competitionIntensityDesc;
        private Double clickCompetitionRate;
        private String clickCompetitionRateGrade;
        private String clickCompetitionRateDesc;
        private Double productAdPriceRate;
        private String productAdPriceRateGrade;
        private String productAdPriceRateDesc;
        private Double adPriceClickRate;
        private String adPriceClickRateGrade;
        private String adPriceClickRateDesc;
        private AdClickRateStats adClickRateStats;
        private Integer keywordId;
        private String keyword;
        private Double openProdPercentScore;
        private String openProdPercentScoreDesc;
        private List<OpenProductPercentResult> openProductPercentResult;
        private List<CategoryResult> categoryResult;
        private Integer firstCategory;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class From1Page {
        private Long sales;
        private Long saleCount;
        private Long averagePrice;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class FromNPay {
        private Long sales;
        private Long saleCount;
        private Long averagePrice;
        private Long salesFrom80;
        private Long saleCountFrom80;
        private Long averagePriceFrom80;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class BrandKeywordResult {
        private boolean isBrandKeyword;
        private List<String> brandKeywords;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class AdClickRateStats {
        private Double adClickRateTotal;
        private String adClickRateTotalGrade;
        private String adClickRateTotalDesc;
        private Double adClickRatePC;
        private String adClickRatePCGrade;
        private String adClickRatePCDesc;
        private Double adClickRateMobile;
        private String adClickRateMobileGrade;
        private String adClickRateMobileDesc;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class OpenProductPercentResult {
        private String name;
        private Double percent;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class CategoryResult {
        private Integer count;
        private List<CategoryInfo> categoryInfoList;
        private Double percent;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class CategoryInfo {
        private Integer id;
        private String name;
    }
}
