package com.btaz.util.xml.xmlpath;

import com.btaz.util.xml.model.Element;
import com.btaz.util.xml.model.Node;

/**
 * User: msundell
 */
public class XmlPathNodename implements XmlPathItem {
    public final static String REGEX_MATCH = "^[^=@'\\[\\]]+$";
    private final String nodename;

    public XmlPathNodename(String nodename) {
        this.nodename = nodename.trim();
    }

    @Override
    public boolean matches(Node node) {
        if(node == null || ! (node instanceof Element)) {
            return false;
        }
        Element element = (Element) node;
        return nodename.equals(element.getName());
    }

    @Override
    public String toString() {
        return nodename;
    }
}
