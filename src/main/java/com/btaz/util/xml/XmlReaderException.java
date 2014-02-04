package com.btaz.util.xml;

/**
 * User: msundell
 */
public class XmlReaderException extends RuntimeException {
    public XmlReaderException(String message) {
        super(message);
    }

    public XmlReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlReaderException(Throwable cause) {
        super(cause);
    }
}
