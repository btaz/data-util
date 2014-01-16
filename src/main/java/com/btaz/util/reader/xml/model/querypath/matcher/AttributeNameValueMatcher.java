package com.btaz.util.reader.xml.model.querypath.matcher;

import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.querypath.MatchType;

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
     * @param element current XML tree element
     * @return {@code MatchType} defining match status
     */
    @Override
    public MatchType match(int level, Element element) {
        if(this.level != level) {
            return MatchType.NOT_A_MATCH;
        } else if(element.hasAttribute(attributeName) && element.attributeValue(attributeName).equals(attributeValue)) {
            return MatchType.NODE_MATCH;
        }
        return MatchType.NOT_A_MATCH;
    }
}
