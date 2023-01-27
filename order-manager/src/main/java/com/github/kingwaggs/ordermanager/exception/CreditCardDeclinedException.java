package com.github.kingwaggs.ordermanager.exception;

public class CreditCardDeclinedException extends Exception {

    public CreditCardDeclinedException() {
        super();
    }

    public CreditCardDeclinedException(String message) {
        super(message);
    }
}
