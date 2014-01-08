package com.btaz.util;

/**
 * User: msundell
 */
public class DataUtilException extends RuntimeException {
    public DataUtilException(String message) {
        super(message);
    }

    public DataUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataUtilException(Throwable cause) {
        super(cause);
    }
}
