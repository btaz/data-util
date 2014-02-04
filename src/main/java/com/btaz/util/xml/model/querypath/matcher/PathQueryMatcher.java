package com.btaz.util.xml.model.querypath.matcher;

import com.btaz.util.xml.model.Node;
import com.btaz.util.xml.model.querypath.MatchType;

/**
 * User: msundell
 */
public interface PathQueryMatcher {
    /**
     * Match method. This method have to make a match decision based on the information provided.
     * @param level XML tree level
     * @param node current XML tree node
     * @return {@code MatchType} defining match status
     */
    MatchType match(int level, Node node);
}
