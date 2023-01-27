package com.github.kingwaggs.ordermanager.coupangsdk.domain;

import java.util.List;
import java.util.Map;

public class ApiResponse<T> {
    private final int statusCode;
    private final Map<String, List<String>> headers;
    private final T data;

    // (Object) -> (T)
    public ApiResponse(int statusCode, Map<String, List<String>> headers) {
        this(statusCode, headers, (T) null);
    }

    public ApiResponse(int statusCode, Map<String, List<String>> headers, T data) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.data = data;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public T getData() {
        return this.data;
    }
}
