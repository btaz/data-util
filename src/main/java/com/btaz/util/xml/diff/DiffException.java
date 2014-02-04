package com.btaz.util.xml.diff;

/**
 * User: msundell
 */
public class DiffException extends RuntimeException {
    public DiffException(String message) {
        super(message);
    }

    public DiffException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiffException(Throwable cause) {
        super(cause);
    }
}
