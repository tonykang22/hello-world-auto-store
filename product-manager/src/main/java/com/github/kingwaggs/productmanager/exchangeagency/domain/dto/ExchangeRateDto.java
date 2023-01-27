package com.github.kingwaggs.productmanager.exchangeagency.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExchangeRateDto {
    private Integer result;

    // 통화코드
    @JsonProperty("cur_unit")
    private String curUnit;

    // 국가/통화명
    @JsonProperty("cur_nm")
    private String curNm;


    // 장부가격
    private String bkpr;

    // 전신환(송금) 보내실때
    private String tts;
}
