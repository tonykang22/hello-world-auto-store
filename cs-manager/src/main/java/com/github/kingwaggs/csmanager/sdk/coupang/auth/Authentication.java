package com.github.kingwaggs.csmanager.sdk.coupang.auth;

import com.github.kingwaggs.csmanager.sdk.coupang.domain.Pair;

import java.util.List;
import java.util.Map;

public interface Authentication {
        void applyToParams(List<Pair> var1, Map<String, String> var2);
}
