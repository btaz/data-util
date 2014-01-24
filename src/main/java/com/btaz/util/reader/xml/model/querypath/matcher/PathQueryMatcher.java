package com.btaz.util.reader.xml.model.querypath.matcher;

import com.btaz.util.reader.xml.model.Node;
import com.btaz.util.reader.xml.model.querypath.MatchType;

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
