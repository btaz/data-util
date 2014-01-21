package com.btaz.util.reader.xml.nodes;

/**
 * User: msundell
 */
public class XmlEndElement implements XmlNode {
    private final String localName;

    public XmlEndElement(String localName) {
        this.localName = localName;
    }

    public String getLocalName() {
        return localName;
    }
}
