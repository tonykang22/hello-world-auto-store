package com.github.kingwaggs.productanalyzer.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PapagoWebPageResponseDto {
    @JsonProperty("status_code")
    int statusCode;
    String data;
}
