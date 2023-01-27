package com.github.kingwaggs.productanalyzerv2.domain.response.itemscout;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class KeywordResponse {

    private String status;
    @JsonProperty("data")
    private Integer keywordId;

}
