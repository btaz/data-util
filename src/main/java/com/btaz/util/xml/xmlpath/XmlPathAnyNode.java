package com.btaz.util.xml.xmlpath;

import com.btaz.util.xml.model.Node;

/**
 * User: msundell
 */
public class XmlPathAnyNode implements XmlPathItem {
    @Override
    public boolean matches(Node node) {
        return node != null;
    }

    @Override
    public String toString() {
        return "node()";
    }
}
