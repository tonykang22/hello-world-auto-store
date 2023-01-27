package com.github.kingwaggs.productmanager.coupang.sdk.exception;

import java.util.List;
import java.util.Map;

public class ApiException extends Exception{
    private int code;
    private Map<String, List<String>> responseHeaders;
    private String responseBody;

    public ApiException() {
        this.code = 0;
        this.responseHeaders = null;
        this.responseBody = null;
    }

    public ApiException(Throwable throwable) {
        super(throwable);
        this.code = 0;
        this.responseHeaders = null;
        this.responseBody = null;
    }

    public ApiException(String message) {
        super(message);
        this.code = 0;
        this.responseHeaders = null;
        this.responseBody = null;
    }

    public ApiException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders, String responseBody) {
        super(message, throwable);
        this.code = 0;
        this.responseHeaders = null;
        this.responseBody = null;
        this.code = code;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    public ApiException(String message, int code, Map<String, List<String>> responseHeaders, String responseBody) {
        this(message, (Throwable)null, code, responseHeaders, responseBody);
    }

    public ApiException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders) {
        this(message, throwable, code, responseHeaders, (String)null);
    }

    public ApiException(int code, Map<String, List<String>> responseHeaders, String responseBody) {
        this((String)null, (Throwable)null, code, responseHeaders, responseBody);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = 0;
        this.responseHeaders = null;
        this.responseBody = null;
        this.code = code;
    }

    public ApiException(int code, String message, Map<String, List<String>> responseHeaders, String responseBody) {
        this(code, message);
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    public int getCode() {
        return this.code;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return this.responseHeaders;
    }

    public String getResponseBody() {
        return this.responseBody;
    }
}
