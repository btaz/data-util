package com.btaz.datautil.xml.xmlpath;

import java.util.List;

/**
 * XML path element. This element does not contain any other information than name and attributes.
 * User: msundell
 */
public class XmlPathElement {
    private String localName;
    private List<XmlPathElementAttribute> attributes;

    public XmlPathElement(String localName) {
        this.localName = localName;
    }

    public XmlPathElement(String localName, List attributes) {
        this(localName);
        if(attributes == null) {
            throw new XmlPathException("The attributes parameter can not be a null value");
        }
        this.attributes = attributes;
    }

    public String getLocalName() {
        return localName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlPathElement element = (XmlPathElement) o;

        if (localName != null ? !localName.equals(element.localName) : element.localName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return localName != null ? localName.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder elementText = new StringBuilder();
        elementText.append("<").append(localName);
        for(XmlPathElementAttribute attribute : attributes) {
            elementText.append(" ").append(attribute.getLocalName()).append("=")
                    .append("\"").append(attribute.getValue()).append("\"");
        }
        elementText.append(">");
        return elementText.toString();
    }

    public String toString(boolean endTag) {
        if(endTag) {
            return new StringBuilder().append("</").append(localName).append(">").toString();
        } else {
            return this.toString();
        }
    }
}
