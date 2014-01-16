package com.btaz.util.api.rest;

/**
 * User: msundell
 */
public class RestApiException extends RuntimeException {
    public RestApiException(String message) {
        super(message);
    }

    public RestApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestApiException(Throwable cause) {
        super(cause);
    }
}
