package com.btaz.util.reader.xml.model.querypath;

import com.btaz.util.reader.xml.model.Content;
import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.Node;
import com.btaz.util.reader.xml.model.XmlModelException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: msundell
 */
public class PathQueryParser {
    private Element root;

    private Pattern reQueryFormat;
    private Pattern reQueryParts;
    private Pattern reQueryNodename;
    private Pattern reQueryNodenameArray;
    private Pattern reQueryNodenameAttrName;
    private Pattern reQueryNodenameAttrNameValue;
    private Pattern reQueryAttrName;

    public PathQueryParser(Element root) {
        this.root = root;

        // PathQuery REGEX
        reQueryFormat = Pattern.compile("(/[^/]+)+");                                           // path query format
        reQueryParts = Pattern.compile("/([^/]+)");
        reQueryNodename = Pattern.compile("([^\\[@]+)");                                          // /nodename
        reQueryNodenameArray = Pattern.compile("([^\\[]+)\\[([0-9]+)\\]");                      // /nodename[0]
        reQueryNodenameAttrName = Pattern.compile("([^\\[]+)\\[@(.+)='(.+)'\\]");               // /nodename[@name]
        reQueryNodenameAttrNameValue = Pattern.compile("([^\\[]+)\\[@([^=]+)='([^']+)'\\]");    // /nodename[@name=value]
        reQueryAttrName = Pattern.compile("@(.+)");                                             // /@name
    }

    /**
     * Find elements through an XPath like pathQuery
     * e.g.
     *   /fruits/orange
     * will find the orange element:
     *   <fruits><orange/></fruits>
     *
     * @param pathQuery path pathQuery string
     * @return {@code List} of {@code Node} objects matching the query
     * @throws PathQueryException path query exception
     */
    public List<Node> pathQuery(String pathQuery) throws XmlModelException {
        // validations
        if(pathQuery == null || pathQuery.trim().length() == 0) {
            return new ArrayList<Node>();
        }
        pathQuery = pathQuery.trim();

        if(! reQueryFormat.matcher(pathQuery).matches()) {
            throw new XmlModelException("Invalid pathQuery path: " + pathQuery);
        }

        //
        // build parser
        //
        List<PathQueryMatcher> queryMatchers = new ArrayList<PathQueryMatcher>();

        // extract path query components
        Matcher parts = reQueryParts.matcher(pathQuery);
        int level = 0;
        while(parts.find()) {
            String part = parts.group(1);

            Matcher nodenameMatcher = reQueryNodename.matcher(part);
            if(nodenameMatcher.matches()) {
                queryMatchers.add(new NodenameMatcher(level, part));
            } else {
                throw new XmlModelException("Invalid pathQuery path: \"" + part + "\" in: " + pathQuery);
            }
            level += 1;
        }

        //
        // execute query
        //
        List<Node> collector = new ArrayList<Node>();
        treeWalker(root, 0, queryMatchers, collector);
        return collector;
    }

    /**
     * Recursive BFS tree walker
     * @param node current node
     */
    private void treeWalker(Node node, int level, List<PathQueryMatcher> queryMatchers, List<Node> collector) {
        // is Content node
        if(node instanceof Content) {
            return;
        }

        // is Element node
        Element element = (Element) node;
        MatchType matchType = queryMatchers.get(level).match(level, element);
        if(matchType == MatchType.NOT_A_MATCH) {
            // no reason to scan deeper
            //noinspection UnnecessaryReturnStatement
            return;
        } else if(matchType == MatchType.NODE_MATCH) {
            // we have a match
            if(level == queryMatchers.size()-1) {
                // full path match
                collector.add(node);
            } else {
                // scan deeper
                List<Node> childElements = element.getChildElements();
                for (Node childElement : childElements) {
                    treeWalker(childElement, level+1, queryMatchers, collector);
                }
            }
        } else {
            throw new PathQueryException("Unknown MatchType: " + matchType);
        }
    }
}
