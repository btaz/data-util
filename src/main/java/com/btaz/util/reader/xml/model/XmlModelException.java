package com.btaz.util.reader.xml.model;

/**
 * User: msundell
 */
public class XmlModelException extends RuntimeException {
    public XmlModelException(String message) {
        super(message);
    }

    public XmlModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlModelException(Throwable cause) {
        super(cause);
    }
}
