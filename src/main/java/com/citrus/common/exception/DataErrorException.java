package com.citrus.common.exception;

public class DataErrorException extends RuntimeException {

    public DataErrorException() {
        super("Data error failed");
    }

    public DataErrorException(String message) {
        super(message);
    }

    public DataErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
