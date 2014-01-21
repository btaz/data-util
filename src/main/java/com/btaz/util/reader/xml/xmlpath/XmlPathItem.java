package com.btaz.util.reader.xml.xmlpath;

import com.btaz.util.reader.xml.model.Node;

/**
 * User: msundell
 * XmlPath V2
 */
public interface XmlPathItem {
    boolean matches(Node node);
}
