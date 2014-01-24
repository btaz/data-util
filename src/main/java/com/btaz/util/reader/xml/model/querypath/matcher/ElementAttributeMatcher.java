package com.btaz.util.reader.xml.model.querypath.matcher;

import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.Node;
import com.btaz.util.reader.xml.model.querypath.MatchType;

/**
 * User: msundell
 */
public class ElementAttributeMatcher implements PathQueryMatcher {
    private final int level;
    private final String elementName;
    private final String attributeName;

    /**
     * Initialize a new object.
     * @param level defines on what level in the path query this matcher was created for
     * @param elementName element name
     * @param attributeName attribute name
     */
    public ElementAttributeMatcher(int level, String elementName, String attributeName) {
        this.level = level;
        this.elementName = elementName;
        this.attributeName = attributeName;
    }

    /**
     * Match method. This method have to make a match decision based on the information provided.
     * @param level XML tree level
     * @param node current XML tree node
     * @return {@code MatchType} defining match status
     */
    @Override
    public MatchType match(int level, Node node) {
        if(! (node instanceof Element)) {
            return MatchType.NOT_A_MATCH;
        }
        Element element = (Element) node;
        if(this.level != level) {
            return MatchType.NOT_A_MATCH;
        } else if(element.getName().equals(elementName) && element.hasAttribute(attributeName)) {
            return MatchType.NODE_MATCH;
        }
        return MatchType.NOT_A_MATCH;
    }

    @Override
    public String toString() {
        return elementName + "@" + attributeName;
    }
}
