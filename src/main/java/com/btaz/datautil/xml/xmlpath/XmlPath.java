package com.btaz.datautil.xml.xmlpath;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A extremely minimalistic implementation of XPath specifically to work with a STack parser.
 * User: msundell
 */
public class XmlPath {
    private boolean absolutePath;
    private List<XmlPathElement> path;

    /**
     * Build a XmlPath from a path pathQuery
     * @param pathQuery path pathQuery
     * @throws XmlPathException XML path exception
     */
    public XmlPath(String pathQuery) throws XmlPathException {
        if(pathQuery == null) {
            throw new XmlPathException("The pathQuery parameter can not be a null value");
        }
        absolutePath = true;
        path = extractPath(pathQuery);
    }

    public XmlPath(List<XmlPathElement> elementPath) {
        if(elementPath == null) {
            throw new XmlPathException("The elementPath parameter can not be a null value");
        }
        absolutePath = true;
        path = elementPath;         // todo: we may need a deep clone here
    }

    public boolean isAbsolutePath() {
        return absolutePath;
    }

    public List<XmlPathElement> getElements() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlPath xmlPath = (XmlPath) o;
        if(! toString().equals(xmlPath.toString())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Get a string representation of the path
     * @return <code>String</code> representation of the path
     */
    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        if(absolutePath) {
            // todo: this should be change to a single /
            query.append("//");
        }
        if(path != null) {
            int i = 0;
            for(XmlPathElement e : path) {
                if(i++ > 0) {
                    query.append("/");
                }
                query.append(e.getLocalName());
            }
        }
        return query.toString();
    }

    private List<XmlPathElement> extractPath(String pathQuery) {
        pathQuery = pathQuery.trim();
        ArrayList<XmlPathElement> elements = new ArrayList<XmlPathElement>();
        StringTokenizer tokenizer = new StringTokenizer(pathQuery, "/", true);
        while(tokenizer.hasMoreTokens()) {
            String part = tokenizer.nextToken();
            if (!"/".equals(part)) {
                XmlPathElement elem = new XmlPathElement(part);
                elements.add(elem);
            }
        }
        return elements;
    }
}
