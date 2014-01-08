package com.btaz.util.unit;

/**
 * User: msundell
 */
public class ResourceUtilException extends RuntimeException {
    public ResourceUtilException(String message) {
        super(message);
    }

    public ResourceUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceUtilException(Throwable cause) {
        super(cause);
    }
}
