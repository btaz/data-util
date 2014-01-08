package com.btaz.util.reader.xml.model.querypath;

import com.btaz.util.reader.xml.model.Element;

/**
 * User: msundell
 */
public interface PathQueryMatcher {
    /**
     * Match method. This method have to make a match decision based on the information provided.
     * @param level XML tree level
     * @param element current XML tree element
     * @return {@code MatchType} defining match status
     */
    MatchType match(int level, Element element);
}
