package com.btaz.datautil.xml.model;

import com.btaz.datautil.xml.model.querypath.PathQueryParser;

import java.util.List;

/**
 * The Document builder class has two major purposes:
 * - make it easy to build a model from XmlStax parser events
 * - make it easy to build a model for unit test purposes
 * User: msundell
 */
public class Document implements Cloneable {
    private String name;
    private Element root;
    private Element current;
    private ElementBuilder elementBuilder;

    /**
     * Initialize an empty XML document
     */
    public Document() {
        this(null);
    }

    /**
     * Initialize an empty XML document and give it a name
     */
    public Document(String name) {
        this.name = name;
        elementBuilder = new ElementBuilder();
    }

    /**
     * Get the name of this document
     * @return {@code String} name or null if not set
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this document
     * @param name document name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the root element for this {@code Document}
     * @return {@code Element}
     */
    public Element getRoot() {
        return root;
    }

    /**
     * Add a new XML fragment to this {@code Document}
     * @return {@code Document} so that you can easily chain adds
     */
    public Document addElement(String xmlFragment) {
        Element element = elementBuilder.getElement(xmlFragment);
        return addElement(element);
    }

    /**
     * Add a new {@code Element} to this {@code Document}
     * @return {@code Document} so that you can easily chain adds
     */
    public Document addElement(Element element) {
        if(current != null) {
            // attach to existing model
            current.addChildElement(element);
        } else {
            // attach to a new model
            root =  element;
        }
        if(! element.isEmptyElementTag()) {
            current = element;
        }
        return this;
    }

    /**
     * End this Element tag. This will effectively move the document cursor to the parent element of that last added
     * {@code Element}
     * <pre>
     *     // create this structure:
     *     // <fruits>
     *     //     <banana></banana>
     *     //     <orange />
     *     // </fruits>
     *     //
     *     // Note: any elements that are not closed at the end are auto-closed i.e. </fruits>
     *     Document doc = new Document()
     *         .addElement("<fruits>")
     *         .addElement("<banana>");
     *         .endElement();
     *         .addElement("<orange />"); // empty element tags do not need the endElement() call
     * </pre>
     * @return
     */
    public Document endElement() {
        current = current.getParent();
        return this;
    }

    /**
     * Add a {@code Content} element. The cursor will point to the parent element after calling this method since child
     * elements that are of the Content type can't have child elements.
     * @param text character data
     * @return {@code Document}
     */
    public Document addContent(String text) {
        if(current == null) {
            throw new XmlModelException("Content can not be attached as root element: " + text);
        }
        Content content = new Content(text);
        content.setParent(current);
        current.addChildElement(content);
        return this;
    }

    /**
     * Add a {@code Content} element. The cursor will point to the parent element after calling this method since child
     * elements that are of the Content type can't have child elements.
     * @param content {@code Content} object
     * @return {@code Document}
     */
    public Document addContent(Content content) {
        if(current == null) {
            throw new XmlModelException("Content can not be attached as root element: " + content.toString());
        }
        content.setParent(current);
        current.addChildElement(content);
        return this;
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
     * @throws XmlModelException XML model exception
     */
    public List<Node> pathQuery(String pathQuery) throws XmlModelException {
        return new PathQueryParser(root).pathQuery(pathQuery);
    }

    /**
     * Compare to document trees. Note, this is a strict comparison. For a relaxed comparison use the comparison method.
     * @param anObject the other document
     * @return {@code boolean} true if equal to the other document
     */
    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) return true;
        if (anObject == null || getClass() != anObject.getClass()) return false;

        Document document = (Document) anObject;

        //noinspection RedundantIfStatement
        if(! bfsComparison(root, document.root)) {
            return false;
        }

        return true;
    }

    /**
     * A strict breadth-first search traversal to evaluate two XML trees against each other
     * @param root first tree
     * @param other second tree
     * @return {@code boolean} true if the trees matches, false otherwise
     */
    private boolean bfsComparison(Node root, Node other) {
        if(root instanceof Content || other instanceof Content) {
            return root.equals(other);
        }
        if(! root.equals(other)) {
            return false;
        }

        List<Node> a = ((Element)root).getChildElements();
        List<Node> b = ((Element)other).getChildElements();
        if(a.size() != b.size()) {
            return false;
        }
        for(int i=0; i<a.size(); i++) {
            if(! a.get(i).equals(b.get(i))) {
                return false;
            }
        }

        for(int i=0; i<a.size(); i++) {
            if(! bfsComparison(a.get(i), b.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * BSF based generated hash code for an XML document tree
     * @return {@code int} hash code value
     */
    @Override
    public int hashCode() {
        int result = 0;

        if(root == null) {
            return result;
        }

        return bsfHashCode(root, result);
    }

    /**
     * Recursive hash code creator
     * @param node root node
     * @param result cumulative hash code value
     * @return result resulting cumulative hash code value
     */
    private int bsfHashCode(Node node, int result) {
        result += 31 * node.hashCode();

        if(node instanceof Content) {
            return result;
        }

        Element elem = (Element) node;
        List<Node> childElements = elem.getChildElements();

        for (Node childElement : childElements) {
            result += 31 * childElement.hashCode();
        }

        for (Node child : childElements) {
            if (child instanceof Content) {
                result += 31 * child.hashCode();
            } else {
                result = bsfHashCode(child, result);
            }
        }

        return result;
    }

    /**
     * This method is used to export a document as a XML {@code String}
     * @return {@code String} XML
     */
    @Override
    public String toString() {
        if(root == null) {
            return "";
        }
        return root.toString(true);
    }

    /**
     * This method is used to export a document as a XML {@code String}
     * @param flat export in a flat XML data format. Note that this will strip out all EOL characters
     * @return {@code String} XML
     */
    public String toString(boolean flat) {
        if(root == null) {
            return null;
        }
        return root.toString(true, flat);
    }

    /**
     * This method is used to export the root document element as a XML {@code String}
     * @return {@code String} XML
     */
    public String rootElementAsTagXml() {
        if(root == null) {
            return "";
        }
        return root.toString(false, true);
    }

    /**
     * Clone a document. This is method performs deep cloning
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        Document doc = new Document(name);
        if(root != null) {
            doc.addElement((Element)root.clone());
            deepCopy(root, doc.root);
        }

        return doc;
    }

    /**
     * Deep copy recursive helper method.
     * @param origElement original element
     * @param copyElement copy element
     */
    private void deepCopy(Element origElement, Element copyElement) {
        List<Node> children = origElement.getChildElements();
        for(Node node : children) {
            try {
                if(node instanceof Content) {
                    Content content = (Content) ((Content) node).clone();
                    copyElement.addChildElement(content);
                } else {
                    Element element = (Element) ((Element) node).clone();
                    copyElement.addChildElement(element);
                    deepCopy((Element)node, element);
                }
            } catch (CloneNotSupportedException e) {
                throw new XmlModelException("Unable to clone object", e);
            }
        }
    }
}
