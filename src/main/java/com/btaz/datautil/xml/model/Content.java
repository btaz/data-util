package com.btaz.datautil.xml.model;

import com.btaz.datautil.DataUtilDefaults;

/**
 * User: msundell
 */
public class Content extends Node implements Cloneable {
    private String text;
    private Element parent;

    /**
     * Initializes a newly created {@code Content} object with a character data
     */
    public Content(String text) {
        if(text == null) {
            throw new XmlModelException("The text parameter can not be a null value");
        }
        this.text = text;
    }

    /**
     * Get {@code Content} character data
     * @return {@code String} data
     */
    public String getText() {
        return text;
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
     * Get a string representation of this content
     * @return {@code String} data
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * Get a string representation of this content
     * @param tree tree representation of the data
     * @param flat flat render without new line characters
     * @param level indentation level, requires tree to be set to true
     * @return {@code String} data
     */
    @Override
    public String toString(boolean tree, boolean flat, int level) {
        StringBuilder xml = new StringBuilder();
        if(! flat) {
            for(int i=0; i < level*DataUtilDefaults.tabSize; i++) {
                xml.append(" ");
            }
        }
        if(!flat) {
            xml.append(toString());
            xml.append(DataUtilDefaults.lineTerminator);
        } else {
            // if flat format, then replace newline with a space
            xml.append(toString().replace("\n", " "));
        }
        return xml.toString();
    }


    /**
     * Compares this {@code Content} to the specified object.  The result is {@code true} if and only if the argument is
     * not {@code null} and is a {@code Content} object that represents the same character data as this object.
     * @param anObject The object to compare this {@code Content} against
     * @return {@code true} if the given object represents a {@code Content} equivalent to this content, {@code false}
     * otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        //noinspection RedundantIfStatement
        if (!text.equals(content.text)) return false;

        return true;
    }

    /**
     * Returns a hash code for this content.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return text.hashCode();
    }

    /**
     * Creates a detached deep clone of the Content object. References to parent is not copied.
     * @return [@code Object] cloned object
     * @throws CloneNotSupportedException exception
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new Content(text);
    }
}
