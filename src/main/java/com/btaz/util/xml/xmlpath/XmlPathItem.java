package com.btaz.util.xml.xmlpath;

import com.btaz.util.xml.model.Node;

/**
 * User: msundell
 * XmlPath V2
 */
public interface XmlPathItem {
    boolean matches(Node node);
}
