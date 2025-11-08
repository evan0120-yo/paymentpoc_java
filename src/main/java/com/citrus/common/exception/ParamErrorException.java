package com.citrus.common.exception;

public class ParamErrorException extends RuntimeException {

    public ParamErrorException() {
        super("param error");
    }

    public ParamErrorException(String message) {
        super(message);
    }

    public ParamErrorException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
