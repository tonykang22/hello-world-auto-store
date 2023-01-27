package com.github.kingwaggs.productmanager.exchangeagency.exception;

public class ExchangeRateException extends Exception {
    public ExchangeRateException(String message, Exception exception) {
        super(message, exception);
    }
}
