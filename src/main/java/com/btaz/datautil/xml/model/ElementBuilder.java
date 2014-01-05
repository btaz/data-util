package com.btaz.datautil.xml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to build XML elements from XML string fragments
 * Todo: convert to static
 * User: msundell
 */
public class ElementBuilder {
    private Pattern reXmlElement;
    private Pattern reAttributesString;
    private Pattern reAttribute;

    public ElementBuilder() {
        reXmlElement = Pattern.compile("< *(\\w+) *((?: *:?\\w+ *= *\"[^\"]*\" *)*) *(/)? *>");
        reAttributesString = Pattern.compile("(?: *:?\\w+ *= *\"[^\"]*\" *)+");
        reAttribute = Pattern.compile("(\\w+) *= *\"([^\"]*)\"");
    }

    public List<Attribute> getAttributes(String attributesString) throws XmlModelException {
        // verifications
        if(attributesString == null) {
            throw new XmlModelException("The attributesString parameter can not be a null value");
        }

        // empty list?
        attributesString = attributesString.trim();
        if(attributesString.length() == 0) {
            return new ArrayList<Attribute>();
        }

        // extract attributes
        Matcher ms = reAttributesString.matcher(attributesString);
        if(! ms.matches()) {
            throw new XmlModelException("Invalid attributes definition: " + attributesString);
        }

        List<Attribute> attributeList = new ArrayList<Attribute>();
        Matcher m = reAttribute.matcher(attributesString);
        while(m.find()) {
            attributeList.add(new Attribute(m.group(1), m.group(2)));
        }

        return attributeList;
    }

    public Element getElement(String elementString) throws XmlModelException {
        // validations
        if(elementString == null) {
            throw new XmlModelException("The elementString parameter can not be a null value");
        }
        elementString = elementString.trim();
        Matcher m = reXmlElement.matcher(elementString);
        if(! m.matches()) {
            throw new XmlModelException("The elementString parameter is not a valid XML element: " + elementString);
        }

        // create element
        String name = m.group(1);
        List<Attribute> attributes = getAttributes(m.group(2));
        boolean isEmptyElementTag = m.group(3) != null;

        Element element = new Element(name, isEmptyElementTag);
        element.addAttributes(attributes);

        return element;
    }
}
