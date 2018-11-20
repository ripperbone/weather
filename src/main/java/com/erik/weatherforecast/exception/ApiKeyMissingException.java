package com.erik.weatherforecast.exception;

public class ApiKeyMissingException extends RuntimeException {

    public ApiKeyMissingException(String errorMessage) {
        super(errorMessage);
    }

    public ApiKeyMissingException(Exception ex) {
        super(ex);
    }
}
