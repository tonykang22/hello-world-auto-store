package com.github.kingwaggs.productanalyzerv2.domain.response.itemscout;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class CompetitionStatResponse {

    private String status;
    @JsonProperty("data")
    private Stat stat;

    @Getter
    @ToString
    @NoArgsConstructor
    public static class Stat {
        private String grade;
        private String desc;
        private String blogCompetitionGrade;
        private String blogCompetitionDesc;
        private String cafeCompetitionGrade;
        private String cafeCompetitionDesc;
        private String viewCompetitionGrade;
        private String viewCompetitionDesc;
        private String monthRatioGrade;
        private String monthRatioDesc;
        private Integer blogCount;
        private Integer cafeCount;
        private Double blogCompetitionRatio;
        private Double cafeCompetitionRatio;
        private Double viewCompetitionRatio;
        private Double month1Ratio;
        private Double month6Ratio;
        private Double yearRatio;
    }
}
