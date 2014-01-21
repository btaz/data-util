package com.btaz.util.reader.xml.xmlpath;

import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.Node;

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
