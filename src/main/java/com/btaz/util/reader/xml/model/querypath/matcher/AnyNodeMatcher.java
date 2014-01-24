package com.btaz.util.reader.xml.model.querypath.matcher;

import com.btaz.util.reader.xml.model.Node;
import com.btaz.util.reader.xml.model.querypath.MatchType;

/**
 * User: msundell
 */
public class AnyNodeMatcher implements PathQueryMatcher {
    @Override
    public MatchType match(int level, Node node) {
        return MatchType.NODE_MATCH;
    }

    @Override
    public String toString() {
        return "node()";
    }
}
