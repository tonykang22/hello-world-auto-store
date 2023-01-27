package com.github.kingwaggs.productmanager.common.exception;

public class NotEnoughCreditException extends Exception {

    public NotEnoughCreditException() {
        super();
    }

    public NotEnoughCreditException(String message) {
        super(message);
    }
}
