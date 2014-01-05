package com.btaz.datautil.files.mapreduce;

/**
 * This exception is used to indicate that an error occured during the map process that is irrecoverable
 * User: msundell
 */
public class MapReduceException extends RuntimeException {
    public MapReduceException(String message) {
        super(message);
    }

    public MapReduceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapReduceException(Throwable cause) {
        super(cause);
    }
}
