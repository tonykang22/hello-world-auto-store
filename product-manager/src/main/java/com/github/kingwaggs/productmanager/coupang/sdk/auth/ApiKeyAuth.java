package com.github.kingwaggs.productmanager.coupang.sdk.auth;

import com.github.kingwaggs.productmanager.coupang.sdk.domain.Pair;

import java.util.List;
import java.util.Map;

public class ApiKeyAuth implements Authentication {
    private final String location;
    private final String paramName;
    private String apiKey;
    private String apiKeyPrefix;

    public ApiKeyAuth(String location, String paramName) {
        this.location = location;
        this.paramName = paramName;
    }

    public String getLocation() {
        return this.location;
    }

    public String getParamName() {
        return this.paramName;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKeyPrefix() {
        return this.apiKeyPrefix;
    }

    public void setApiKeyPrefix(String apiKeyPrefix) {
        this.apiKeyPrefix = apiKeyPrefix;
    }

    public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
        if (this.apiKey != null) {
            String value;
            if (this.apiKeyPrefix != null) {
                value = this.apiKeyPrefix + " " + this.apiKey;
            } else {
                value = this.apiKey;
            }

            if ("query".equals(this.location)) {
                queryParams.add(new Pair(this.paramName, value));
            } else if ("header".equals(this.location)) {
                headerParams.put(this.paramName, value);
            }

        }
    }
}
