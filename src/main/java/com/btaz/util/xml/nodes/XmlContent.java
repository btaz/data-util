package com.btaz.util.xml.nodes;

/**
 * User: msundell
 */
public class XmlContent implements XmlNode {
    private final String text;

    public XmlContent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
