package com.btaz.datautil.files.mapreduce;

/**
 * User: msundell
 */
public class ReduceException extends RuntimeException {
    public ReduceException(String message) {
        super(message);
    }

    public ReduceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReduceException(Throwable cause) {
        super(cause);
    }
}
