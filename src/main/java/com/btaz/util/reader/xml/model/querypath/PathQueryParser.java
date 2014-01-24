package com.btaz.util.reader.xml.model.querypath;

import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.Node;
import com.btaz.util.reader.xml.model.XmlModelException;
import com.btaz.util.reader.xml.model.querypath.matcher.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: msundell
 */
public class PathQueryParser {
    private Element root;

    //
    // PathQuery REGEX
    //

    // path query format - /xxx/yyy/xxx
    private final static Pattern reQueryFormat = Pattern.compile("(/[^/]+)+");
    // /xxx/yyy/xxx - extract parts
    private final static Pattern reQueryParts = Pattern.compile("/([^/]+)");
    // nodename
    private final static Pattern reQueryNodename = Pattern.compile("([^\\[@]+)");
    // nodename[@name='value']
    private final static Pattern reQueryNodenameAttrNameValue = Pattern.compile("^([^\\[]+)\\[@([^=]+)='([^']+)'\\]$");
    // nodename[@name]
    private final static Pattern reQueryNodenameAttrName = Pattern.compile("([^\\[]+)\\[@(.+)\\]");
    // @name='value'
    private final static Pattern reQueryAttrNameValue = Pattern.compile("^@(.+)='(.+)'$");
    // @name
    private final static Pattern reQueryAttrName = Pattern.compile("@(.+)");
    // node()                 Matches any node of any kind
    private final static Pattern reQueryAnyNode = Pattern.compile("node\\(\\)");
    // &*                     Matches any Element Attribute node
    private final static Pattern reQueryElementAttribute = Pattern.compile("&\\*");
    // *                      Matches any Element node
    private final static Pattern reQueryAnyElement = Pattern.compile("\\*");

    public PathQueryParser(Element root) {
        this.root = root;
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
            Matcher m;
            String part = parts.group(1);

            if(part.matches(reQueryAnyNode.pattern())) {
                // nodename and attribute name match
                queryMatchers.add(new AnyNodeMatcher());
            } else if(part.matches(reQueryNodename.pattern())) {
                // nodename match
                queryMatchers.add(new NodenameMatcher(level, part));
            } else if(part.matches(reQueryAttrNameValue.pattern())) {
                // attribute name value match
                m = reQueryAttrNameValue.matcher(part);
                if(!m.find()) {
                    throw new XmlModelException("Internal parser error for: " + pathQuery);
                }
                queryMatchers.add(new AttributeNameValueMatcher(level, m.group(1), m.group(2)));
            } else if(part.matches(reQueryAttrName.pattern())) {
                // attribute name match
                m = reQueryAttrName.matcher(part);
                if(!m.find()) {
                    throw new XmlModelException("Internal parser error for: " + pathQuery);
                }
                queryMatchers.add(new AttributeNameMatcher(level, m.group(1)));
            } else if(part.matches(reQueryNodenameAttrNameValue.pattern())) {
                // nodename, attribute name and value match
                m = reQueryNodenameAttrNameValue.matcher(part);
                if(!m.find()) {
                    throw new XmlModelException("Internal parser error for: " + pathQuery);
                }
                queryMatchers.add(new ElementAttributeValueMatcher(level, m.group(1),  m.group(2),  m.group(3)));
            } else if(part.matches(reQueryNodenameAttrName.pattern())) {
                // nodename and attribute name match
                m = reQueryNodenameAttrName.matcher(part);
                if(!m.find()) {
                    throw new XmlModelException("Internal parser error for: " + pathQuery);
                }
                queryMatchers.add(new ElementAttributeMatcher(level, m.group(1), m.group(2)));
            } else {
                // no match
                throw new XmlModelException("Invalid pathQuery: " + pathQuery);
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
        MatchType matchType = queryMatchers.get(level).match(level, node);
        if(matchType == MatchType.NOT_A_MATCH) {
            // no reason to scan deeper
            //noinspection UnnecessaryReturnStatement
            return;
        } else if(matchType == MatchType.NODE_MATCH) {
            // we have a match
            if(level == queryMatchers.size()-1) {
                // full path match
                collector.add(node);
            } else if(node instanceof Element) {
                // scan deeper
                Element element = (Element) node;
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
