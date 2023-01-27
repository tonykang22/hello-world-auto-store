package com.github.kingwaggs.productanalyzerv2.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PandaRankResponse {

    private Status status;
    private Paging paging;
    @JsonProperty("data")
    private List<Keyword> keywordList;

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class Status {
        private Integer code;
        private String message;
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class Paging {
        private Integer count;
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class Keyword {
        @JsonProperty("keyword")
        private List<String> keywords;
        private Integer categoryId;
        @JsonProperty("comp")
        private Double competitionRate;
        @JsonProperty("compStr")
        private List<String> competitionRateList;
        @JsonProperty("cvr")
        private Double shoppingSearchRate;
        @JsonProperty("cvrStr")
        private List<String> shoppingSearchRateList;
        @JsonProperty("bid")
        private Integer advertisingPrice;
        @JsonProperty("bidStr")
        private List<String> advertisingPriceList;
        @JsonProperty("prodCnt")
        private Integer productCount;
        @JsonProperty("searchCnt")
        private Integer searchCount;
        @JsonProperty("prodPrcAvg")
        private Integer productAveragePrice;

        private static final int FIRST_VALUE = 0;

        public String getCompetitionRateString() {
            return Optional.of(competitionRateList.get(FIRST_VALUE))
                    .orElseThrow(NoSuchElementException::new);
        }

        public String getAdvertisingPriceString() {
            return Optional.of(advertisingPriceList.get(FIRST_VALUE))
                    .orElseThrow(NoSuchElementException::new);
        }

        public String getShoppingSearchRateString() {
            return Optional.of(shoppingSearchRateList.get(FIRST_VALUE))
                    .orElseThrow(NoSuchElementException::new);
        }

        public String getKeywordString() {
            return Optional.of(keywords.get(FIRST_VALUE))
                    .orElseThrow(NoSuchElementException::new);
        }

    }

}
