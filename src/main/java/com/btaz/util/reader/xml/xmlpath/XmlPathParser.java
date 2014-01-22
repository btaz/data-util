package com.btaz.util.reader.xml.xmlpath;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: msundell
 */
public class XmlPathParser {
    private final static Pattern rePath = Pattern.compile("^(/[^/]+)+$");
    private final static Pattern rePathPart = Pattern.compile("/([^/]+)");

    /**
     * Parse XML path query
     * @param xmlPathQuery XML path query
     * @return @{code XmlPath} XML path
     */
    public static XmlPath parse(String xmlPathQuery) {
        // validations
        if(xmlPathQuery == null) {
            throw new XmlPathException("The XML path query can not be a null value");
        }
        xmlPathQuery = xmlPathQuery.trim();

        // simple patterns
        if(! xmlPathQuery.contains("/")) {
            if("*".equals(xmlPathQuery)) {
                return new XmlPath(new XmlPathAnyElement());
            } else if("@*".equals(xmlPathQuery)) {
                return new XmlPath(new XmlPathAnyAttributeElement());
            } else if("node()".equals(xmlPathQuery)) {
                return new XmlPath(new XmlPathAnyNode());
            } else if(xmlPathQuery.matches(XmlPathNodenameAttribute.REGEX_MATCH)) {
                return new XmlPath(new XmlPathNodenameAttribute(xmlPathQuery));
            } else if(xmlPathQuery.matches(XmlPathNodename.REGEX_MATCH)) {
                return new XmlPath(new XmlPathNodename(xmlPathQuery));
            }
        }

        // sub-tree patterns
        Matcher m = rePath.matcher(xmlPathQuery);
        if(! m.matches()) {
            throw new XmlPathException("Invalid xml path query: " + xmlPathQuery);
        }

        XmlPath.Builder builder = XmlPath.builder();
        m = rePathPart.matcher(xmlPathQuery);
        while(m.find()) {
            String part = m.group(1);
            if("*".equals(part)) {
                builder.add(new XmlPathAnyElement());
            } else if("@*".equals(part)) {
                builder.add(new XmlPathAnyAttributeElement());
            } else if("node()".equals(part)) {
                builder.add(new XmlPathAnyNode());
            } else if(part.matches(XmlPathNodenameAttribute.REGEX_MATCH)) {
                builder.add(new XmlPathNodenameAttribute(part));
            } else if (part.matches(XmlPathNodename.REGEX_MATCH)) {
                builder.add(new XmlPathNodename(part));
            } else {
                throw new XmlPathException("Invalid part(" + part + ") in xml path query: " + xmlPathQuery);
            }
        }
        return builder.construct();
    }
}
