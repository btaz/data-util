package com.btaz.util.reader.xml.xmlpath;

import com.btaz.util.reader.xml.model.Node;

import java.util.*;

/**
 * User: msundell
 */
public class XmlPath {
    private final boolean simplePattern;
    private final List<XmlPathItem> items;

    public XmlPath(XmlPathItem pathItem) {
        simplePattern = true;
        List<XmlPathItem> newList = new ArrayList<XmlPathItem>();
        newList.add(pathItem);
        items = Collections.unmodifiableList(newList);
    }

    private XmlPath(List<XmlPathItem> list) {
        simplePattern = false;
        this.items = list;
    }

    public boolean isSimplePattern() {
        return simplePattern;
    }

    /**
     * Compare an XML node path with the path query
     * @param nodePath XML data path
     * @return {@code boolean} true if a match
     */
    public boolean matches(LinkedList<Node> nodePath) {
        if(simplePattern) {
            // simple pattern
            return items.get(0).matches(nodePath.getLast());
        }
        // match the full pattern
        if(items.size() != nodePath.size()) {
            // different size
            return false;
        }
        for(int i=0; i<items.size(); i++) {
            if (! items.get(i).matches(nodePath.get(i))) {
                // failed a comparison
                return false;
            }
        }
        // pass all comparisons
        return true;
    }

    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        for(XmlPathItem item : items) {
            if(!simplePattern) {
                query.append("/");
            }
            query.append(item.toString());
        }
        return query.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Helper class used to build a new XmlPath from path parts
     */
    public static class Builder {
        private final List<XmlPathItem> pathItems;

        private Builder() {
            pathItems = new ArrayList<XmlPathItem>();
        }

        public void add(XmlPathItem item) {
            pathItems.add(item);
        }

        public XmlPath construct() {
            XmlPath newPath = new XmlPath(Collections.unmodifiableList(new ArrayList<XmlPathItem>(pathItems)));
            pathItems.clear();
            return newPath;
        }
    }
}
