package com.btaz.datautil.files.mapreduce;

/**
 * This exception is used to indicate that an error occured during the map process that is irrecoverable
 * User: msundell
 */
public class MapException extends RuntimeException {
    public MapException(String message) {
        super(message);
    }

    public MapException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapException(Throwable cause) {
        super(cause);
    }
}
