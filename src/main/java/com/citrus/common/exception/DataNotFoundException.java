package com.citrus.common.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
        super("Data not found");
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
