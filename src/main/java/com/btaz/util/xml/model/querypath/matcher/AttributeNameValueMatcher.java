package com.btaz.util.xml.model.querypath.matcher;

import com.btaz.util.xml.model.Element;
import com.btaz.util.xml.model.Node;
import com.btaz.util.xml.model.querypath.MatchType;

/**
 * User: msundell
 */
public class AttributeNameValueMatcher implements PathQueryMatcher {
    private final int level;
    private final String attributeName;
    private final String attributeValue;

    /**
     * Initialize a new object.
     * @param level defines on what level in the path query this matcher was created for
     * @param attributeName attribute name
     * @param attributeValue attribute value
     */
    public AttributeNameValueMatcher(int level, String attributeName, String attributeValue) {
        this.level = level;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
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
        } else if(element.hasAttribute(attributeName) && element.attributeValue(attributeName).equals(attributeValue)) {
            return MatchType.NODE_MATCH;
        }
        return MatchType.NOT_A_MATCH;
    }

    @Override
    public String toString() {
        return "@" + attributeName + "=['" + attributeValue + "']";
    }
}
