package com.btaz.util.reader.xml.model.querypath.matcher;

import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.Node;
import com.btaz.util.reader.xml.model.querypath.MatchType;

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
        } else if(nodename.equals(element.getName())) {
            return MatchType.NODE_MATCH;
        }
        return MatchType.NOT_A_MATCH;
    }

    @Override
    public String toString() {
        return nodename;
    }
}
