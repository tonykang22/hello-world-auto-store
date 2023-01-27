package com.github.kingwaggs.ordermanager.coupangsdk.auth;

import com.github.kingwaggs.ordermanager.coupangsdk.domain.Pair;

import java.util.List;
import java.util.Map;

public interface Authentication {
        void applyToParams(List<Pair> var1, Map<String, String> var2);
}
