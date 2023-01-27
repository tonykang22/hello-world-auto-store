package com.github.kingwaggs.productanalyzerv2.exception;

public class UnreachableException extends RuntimeException {

    public UnreachableException() {
        super();
    }

    public UnreachableException(String message) {
        super(message);
    }
}
