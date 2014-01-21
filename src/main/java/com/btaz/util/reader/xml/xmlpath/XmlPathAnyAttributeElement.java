package com.btaz.util.reader.xml.xmlpath;

import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.Node;

/**
 * User: msundell
 */
public class XmlPathAnyAttributeElement implements XmlPathItem {
    @Override
    public boolean matches(Node node) {
        if(node == null || ! (node instanceof Element)) {
            return false;
        }
        Element element = (Element) node;
        return element.attributeCount() > 0;
    }

    @Override
    public String toString() {
        return "@*";
    }
}
