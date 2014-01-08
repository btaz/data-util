package com.btaz.util.reader.xml.model;

/**
 * User: msundell
 */
public abstract class Node {
    public abstract void setParent(Element element);
    public abstract Element getParent();

    /**
     * Get a {@code String} representation of the Node.
     * @param tree if true then create an XML representation of the {@code Node} and the whole subtree
     * @return {@code String} XML
     */
    public String toString(boolean tree) {
        return toString(tree, false, 0);
    }

    /**
     * Get a {@code String} representation of the Node.
     * @param tree if true then create an XML representation of the {@code Node} and the whole subtree
     * @param flat if true then create an XML representation without any newline characters
     * @return {@code String} XML
     */
    public String toString(boolean tree, boolean flat) {
        return toString(tree, flat, 0);
    }

    /**
     * Export node to string XML
     * @param tree tree
     * @param flat flat
     * @param level level
     * @return {@code String} XML
     */
    public abstract String toString(boolean tree, boolean flat, int level);
}
