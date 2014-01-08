package com.btaz.util.reader.xml.model.querypath;

/**
 * User: msundell
 */
public class PathQueryException extends RuntimeException {
    public PathQueryException(String message) {
        super(message);
    }

    public PathQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathQueryException(Throwable cause) {
        super(cause);
    }
}
