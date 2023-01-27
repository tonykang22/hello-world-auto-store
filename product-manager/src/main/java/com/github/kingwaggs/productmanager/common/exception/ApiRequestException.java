package com.github.kingwaggs.productmanager.common.exception;

public class ApiRequestException extends Exception {

    public ApiRequestException() {
        super();
    }

    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
