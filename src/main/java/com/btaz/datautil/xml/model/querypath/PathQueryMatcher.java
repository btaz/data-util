package com.btaz.datautil.xml.model.querypath;

import com.btaz.datautil.xml.model.Element;

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
