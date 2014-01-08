package com.btaz.util.reader.xml.model;

import com.btaz.util.DataUtilDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: msundell
 */
public class Element extends Node implements Cloneable {
    private String name;
    private boolean emptyElementTag; // if true, then this element is an empty element tag
    private ArrayList<String> attributeNames; // used to iterate over attributes in order
    private HashMap<String,Attribute> attributesMap; // element attributes
    private ArrayList<Node> childElements; // element child elements
    private Element parent; // parent element

    /**
     * Initializes a newly created {@code Element} object with a name
     * @param name element name
     */
    public Element(String name) {
        if(name == null) {
            throw new XmlModelException("The name parameter can not be null value");
        }
        if(name.contains("\"")) {
            throw new XmlModelException("The name parameter can contain invalid character '\"' : " + name);
        } else if(name.contains("<")) {
            throw new XmlModelException("The name parameter can contain invalid character '<' : " + name);
        } else if(name.contains(">")) {
            throw new XmlModelException("The name parameter can contain invalid character '>' : " + name);
        }
        this.name = name;
        this.emptyElementTag = false;
        attributeNames = new ArrayList<String>();
        attributesMap = new HashMap<String,Attribute>();
        childElements = new ArrayList<Node>();
    }

    /**
     * Initializes a newly created {@code Element} object with a name and allows you to define if it is an empty element
     * tag
     * @param name element name
     * @param emptyElementTag if true then the underlying XML was defined as an empty element tag
     */
    public Element(String name, boolean emptyElementTag) {
        this(name);
        this.emptyElementTag = emptyElementTag;
    }

    /**
     * Make another {@code Element} object the parent
     * @param element parent
     */
    @Override
    public void setParent(Element element) {
        this.parent = element;
    }

    /**
     * Get parent {@code Element}
     * @return parent element
     */
    @Override
    public Element getParent() {
        return parent;
    }

    /**
     * Get {@code Element} name
     * @return element name
     */
    public String getName() {
        return name;
    }

    /**
     * Empty {@code} Element tag status
     * @return true if the element is of the empty element tag type
     */
    public boolean isEmptyElementTag() {
        return emptyElementTag;
    }

    /**
     * Add element attributes
     * @param attributes list of {@code Attribute} items
     */
    public void addAttributes(List<Attribute> attributes) {
        for(Attribute attribute : attributes) {
            addAttribute(attribute.getName(), attribute.getValue());
        }
    }

    /**
     * Add a new element attribute
     * @param name attribute name
     * @param value attribute value
     * @throws XmlModelException XML model exception
     */
    public void addAttribute(String name, String value) throws XmlModelException {
        name = name.trim();
        value = value.trim();
        if(attributesMap.containsKey(name)) {
            throw new XmlModelException("Duplicate attribute: " + name);
        }
        attributeNames.add(name);
        attributesMap.put(name, new Attribute(name, value));
    }

    /**
     * Get attribute value by name
     * @param name attribute name
     * @return {@code String} attribute value
     */
    public String attributeValue(String name) {
        return attributesMap.get(name).getValue();
    }

    /**
     * Find out if this element has an {@code Attribute} for a given attribute name
     * @param name attribute name
     * @return {@code boolean} true if the attribute exists
     */
    public boolean hasAttribute(String name) {
        return attributesMap.containsKey(name);
    }

    /**
     * Element {@code Attribute} count
     * @return {@code int} number of attributes
     */
    public int attributeCount() {
        return attributeNames.size();
    }

    /**
     * Get a list of the {@code Element} attribute names
     * @return {@code List} of {@code String} attribute names
     */
    public List<String> getAttributeNames() {
        return attributeNames;
    }

    /**
     * Retrieve a list of child elements
     * @return {@code List} of child {@code Node} elements
     */
    public List<Node> getChildElements() {
        return childElements;
    }

    /**
     * Child element count
     * @return {@code int} item count
     */
    public int childElementCount() {
        return childElements.size();
    }

    /**
     * Add a new child {@code Node} element
     * @param element element
     */
    public void addChildElement(Node element) {
        element.setParent(this);
        emptyElementTag = false; // can't be an empty element tag if you attach child elements
        childElements.add(element);
    }

    /**
     * Get a {@code String} representation of the Element
     * @return {@code String} XML
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append("<").append(name);

        for(String attributeName : attributeNames) {
            text.append(" ").append(attributeName)
                    .append("=")
                    .append('"').append(attributesMap.get(attributeName).getValue()).append('"');
        }

        if (!isEmptyElementTag()) {
            text.append(">");
        } else {
            text.append(" />");
        }
        return text.toString();
    }

    /**
     * Get a {@code String} representation of the Element
     * @param tree if true then create an XML representation of the {@code Element} and the whole subtree
     * @param flat if true then create an XML representation without any newline characters
     * @param level indentation level, this will only work if tree is set to true
     * @return {@code String} object representing this {@code Element} Element
     */
    @Override
    public String toString(boolean tree, boolean flat, int level) {
        String eol = (flat)? "" : DataUtilDefaults.lineTerminator;
        StringBuilder xml = new StringBuilder();
        if(! flat) {
            for(int i=0; i < level*DataUtilDefaults.tabSize; i++) {
                xml.append(" ");
            }
        }
        xml.append(toString()).append(eol);
        if( isEmptyElementTag()) {
            return xml.toString();
        }

        if(tree) {
            for(Node node : childElements) {
                xml.append(node.toString(true, flat, level+1));
            }
            if(! flat) {
                for(int i=0; i < level*DataUtilDefaults.tabSize; i++) {
                    xml.append(" ");
                }
            }
            if(! isEmptyElementTag()) {
                xml.append("</").append(name).append(">").append(eol);
            }
        }
        return xml.toString();
    }

    /**
     * Compares this {@code Element} to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@code
     * Element} object that represents the same name, attribute name and value sequence of characters as this object.
     * @param anObject The object to compare this {@code Element} against
     * @return {@code true} if the given object represents a {@code Element} equivalent to this element, {@code false}
     * otherwise
     */
    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) return true;
        if (anObject == null || getClass() != anObject.getClass()) return false;

        Element element = (Element) anObject;

        if (emptyElementTag != element.emptyElementTag) return false;
        if (!attributeNames.equals(element.attributeNames)) return false;
        if (!name.equals(element.name)) return false;
        for(String an : attributeNames) {
            if(! this.attributeValue(an).equals(element.attributeValue(an))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a hash code for this element.
     * @return  a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (emptyElementTag ? 1 : 0);
        result = 31 * result + attributeNames.hashCode();
        for(String an : attributeNames) {
            result += attributeValue(an).hashCode();
        }

        return result;
    }

    /**
     * Creates a detached deep clone of the Element object. References to parent and child objects are not copied.
     * @return [@code Object] cloned object
     * @throws CloneNotSupportedException exception
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        Element element = new Element(name);
        element.emptyElementTag = emptyElementTag;
        element.parent = null;
        element.attributeNames = new ArrayList<String>();
        for(String attributeName : attributeNames) {
            element.attributeNames.add(attributeName);
            element.attributesMap.put(attributeName, (Attribute)attributesMap.get(attributeName).clone());
        }
        return element;
    }
}
