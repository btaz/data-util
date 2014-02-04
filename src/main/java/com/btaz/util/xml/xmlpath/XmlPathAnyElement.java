package com.btaz.util.xml.xmlpath;

import com.btaz.util.xml.model.Element;
import com.btaz.util.xml.model.Node;

/**
 * User: msundell
 */
public class XmlPathAnyElement implements XmlPathItem {
    @Override
    public boolean matches(Node node) {
        return !(node == null || !(node instanceof Element));
    }

    @Override
    public String toString() {
        return "*";
    }
}
