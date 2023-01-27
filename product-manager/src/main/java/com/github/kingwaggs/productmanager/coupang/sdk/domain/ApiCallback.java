package com.github.kingwaggs.productmanager.coupang.sdk.domain;

import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;

import java.util.List;
import java.util.Map;

public interface ApiCallback<T> {
    void onFailure(ApiException var1, int var2, Map<String, List<String>> var3);

    void onSuccess(Object var1, int var2, Map<String, List<String>> var3);

    void onUploadProgress(long var1, long var3, boolean var5);

    void onDownloadProgress(long var1, long var3, boolean var5);
}
