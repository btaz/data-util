package com.btaz.datautil.xml.xmlpath;

/**
 * XML path element attribute. This attribute is only associated with XML path elements
 * User: msundell
 */
public class XmlPathElementAttribute {
    private final String localName;
    private final String value;

    public XmlPathElementAttribute(String localName, String value) {
        this.localName = localName;
        this.value = value;
    }

    public String getLocalName() {
        return localName;
    }

    public String getValue() {
        return value;
    }
}
