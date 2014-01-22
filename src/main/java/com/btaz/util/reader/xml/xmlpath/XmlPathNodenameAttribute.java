package com.btaz.util.reader.xml.xmlpath;

import com.btaz.util.reader.xml.XmlReaderException;
import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.Node;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class supports the following queries:
 *   *[@*]
 *   fruit[@*]
 *   fruit[@fruit]
 *   fruit[@fruit='apple']
 * User: msundell
 */
public class XmlPathNodenameAttribute implements XmlPathItem {
    public final static String REGEX_MATCH = "^.+\\[@.+\\]$";
    private final static Pattern reExtractParts = Pattern.compile("^(.+)\\[@([^'=\\]]+)(?:='(.+)')?\\]$");
    private final ComparisonMode comparisonMode;
    private final String nodename;
    private final String attributeName;
    private final String attributeValue;
    private final String xmlPathQuery;

    public XmlPathNodenameAttribute(String xmlPathQuery) {
        this.xmlPathQuery = xmlPathQuery;

        Matcher m = reExtractParts.matcher(xmlPathQuery);
        if(m.find()) {
            // capture nodename
            if(m.group(1) == null) {
                throw new XmlPathException("Invalid XML path query. Nodename must be specified: " + xmlPathQuery);
            }
            nodename = m.group(1).trim();

            // capture attribute name
            attributeName = (m.group(2) == null)? null : m.group(2).trim();

            // capture attribute value
            if((attributeName == null || "*".equals(attributeName)) && m.group(3) != null) {
                throw new XmlPathException("Invalid XML path query. Attribute name must be specified when an attribute value is set: "
                        + xmlPathQuery);
            }
            attributeValue = (m.group(3) == null)? null : m.group(3).trim();

            // set comparison mode
            if(attributeName != null && attributeValue != null) {
                comparisonMode = ComparisonMode.NODENAME_NAME_VALUE;
            } else if (attributeName != null) {
                comparisonMode = ComparisonMode.NODENAME_NAME;
            } else {
                comparisonMode = ComparisonMode.NODENAME;
            }
        } else {
            throw new XmlReaderException("Internal error, can't parse XML path query: " + xmlPathQuery);
        }
    }

    @Override
    public boolean matches(Node node) {
        if(! (node instanceof Element)) {
            return false;
        }
        Element element = (Element) node;
        switch(comparisonMode) {
            case NODENAME:
                if(nodename.equals("*") || nodename.equals(element.getName())) {
                    return true;
                }
                break;
            case NODENAME_NAME:
                if((nodename.equals("*") || nodename.equals(element.getName())) &&
                        (attributeName.equals("*") || element.hasAttribute(attributeName))) {
                    return true;
                }
                break;
            case NODENAME_NAME_VALUE:
                if((nodename.equals("*") || nodename.equals(element.getName()))
                        && (attributeName.equals("*") || attributeValue.equals(element.attributeValue(attributeName)))) {
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return xmlPathQuery;
    }

    private static enum ComparisonMode {
        NODENAME, NODENAME_NAME, NODENAME_NAME_VALUE
    }
}
