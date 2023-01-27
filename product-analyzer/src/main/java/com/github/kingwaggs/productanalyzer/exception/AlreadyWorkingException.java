package com.github.kingwaggs.productanalyzer.exception;

public class AlreadyWorkingException extends Exception {

    public AlreadyWorkingException(String message) {
        super(message);
    }

    public AlreadyWorkingException(String message, Throwable cause) {
        super(message, cause);
    }

}
