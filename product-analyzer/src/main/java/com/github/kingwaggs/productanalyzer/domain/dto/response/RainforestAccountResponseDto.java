package com.github.kingwaggs.productanalyzer.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RainforestAccountResponseDto {
    @JsonProperty("account_info")
    AccountInfo accountInfo;

    @Data
    public static class AccountInfo {
        @JsonProperty("api_key")
        private String apiKey;
        private String name;
        private String email;
        private String plan;
        @JsonProperty("credits_used")
        private int creditsUsed;
        @JsonProperty("credits_limit")
        private int creditsLimit;
        @JsonProperty("credits_remaining")
        private int creditsRemaining;
        @JsonProperty("credits_reset_at")
        private String creditsResetAt;
    }
}
