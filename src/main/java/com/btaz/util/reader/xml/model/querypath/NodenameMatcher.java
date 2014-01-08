package com.btaz.util.reader.xml.model.querypath;

import com.btaz.util.reader.xml.model.Element;

/**
 * User: msundell
 */
public class NodenameMatcher implements PathQueryMatcher {
    private final int level;
    private final String nodename;

    /**
     * Initialize a new object.
     * @param level defines on what level in the path query this matcher was created for
     * @param nodename nodename
     */
    public NodenameMatcher(int level, String nodename) {
        this.level = level;
        this.nodename = nodename;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MatchType match(int level, Element element) {
        if(this.level != level) {
            return MatchType.NOT_A_MATCH;
        } else if(nodename.equals(element.getName())) {
            return MatchType.NODE_MATCH;
        }
        return MatchType.NOT_A_MATCH;
    }
}
