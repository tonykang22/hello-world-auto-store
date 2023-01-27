package com.github.kingwaggs.productanalyzer.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PapagoTextResponseDto {
    private Message message;

    @Data
    public static class Message {
        @JsonProperty("@type")
        private String type;
        @JsonProperty("@service")
        private String service;
        @JsonProperty("@version")
        private String version;
        private Result result;
    }

    @Data
    public static class Result {
        private String srcLangType;
        private String tarLangType;
        private String translatedText;
    }
}
