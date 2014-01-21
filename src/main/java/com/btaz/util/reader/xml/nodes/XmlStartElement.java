package com.btaz.util.reader.xml.nodes;

import com.btaz.util.reader.xml.model.Attribute;

import java.util.List;

/**
 * User: msundell
 */
public class XmlStartElement implements XmlNode {
    private final String localName;
    private final List<Attribute> attributes;

    public XmlStartElement(String localName, List<Attribute> attributes) {
        this.localName = localName;
        this.attributes = attributes;
    }

    public String getLocalName() {
        return localName;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}

