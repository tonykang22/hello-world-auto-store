package com.github.kingwaggs.productmanager.coupang.sdk.auth;

import com.github.kingwaggs.productmanager.coupang.sdk.domain.Pair;

import java.util.List;
import java.util.Map;

public interface Authentication {
        void applyToParams(List<Pair> var1, Map<String, String> var2);
}
