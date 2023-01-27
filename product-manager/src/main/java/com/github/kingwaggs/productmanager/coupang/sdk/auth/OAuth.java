package com.github.kingwaggs.productmanager.coupang.sdk.auth;

import com.github.kingwaggs.productmanager.coupang.sdk.domain.Pair;

import java.util.List;
import java.util.Map;

public class OAuth implements Authentication {
    private String accessToken;

    public OAuth() {
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
        if (this.accessToken != null) {
            headerParams.put("Authorization", "Bearer " + this.accessToken);
        }

    }
}
