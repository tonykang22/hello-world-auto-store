package com.github.kingwaggs.productmanager.common.domain.response;

import lombok.Data;

@Data
public class CommonResponse {
    public enum ResponseStatus {
        SUCCESS, ERROR
    }

    private ResponseStatus status;
    private Object body;

    public CommonResponse(ResponseStatus status, Object body) {
        this.status = status;
        this.body = body;
    }
}