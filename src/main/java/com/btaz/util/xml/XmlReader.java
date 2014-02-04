package com.btaz.util.xml;

import com.btaz.util.xml.model.*;
import com.btaz.util.xml.nodes.XmlContent;
import com.btaz.util.xml.nodes.XmlEndElement;
import com.btaz.util.xml.nodes.XmlNode;
import com.btaz.util.xml.nodes.XmlStartElement;
import com.btaz.util.xml.xmlpath.XmlPath;
import com.btaz.util.xml.xmlpath.XmlPathParser;
import com.btaz.util.tf.XmlEscape;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This is an XML utility class that makes it easier to extract XML subtrees from XML data.
 * This classes uses XML path a query format akin to but not identical to XPath
 *
 * Examples:
 *   *                                  Matches any Element node
 *   &amp;*                             Matches any Element Attribute node
 *   node()                             Matches any node of any kind
 *   nodename                           Matches any nodes with the name "nodename"
 *   /fruits                            Matches fruits element
 *   /fruits/fruit                      Matches fruits/fruit element
 *   /fruits/*                          Matches any child element of the fruits element
 *   /fruits/fruit[&amp;name='apple']   Matches any fruits/fruit element that has the name attribute value 'apple'
 */
public class XmlReader {
    private LinkedList<Node> currentPath;
    private LinkedList<XmlNode> nodeQueue;
    private XMLStreamReader reader;
    private InputStream inputStream;

    /**
     * Initialize XML reader with a String
     * @param xmlData XML data
     */
    public XmlReader(String xmlData) {
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes(Charset.forName("UTF-8")));
        init(inputStream);
    }

    /**
     * Initialize XML reader with an input stream
     * @param inputStream input stream
     */
    public XmlReader(InputStream inputStream) {
        init(inputStream);
    }

    /**
     * Initialize XML reader for processing
     * @param inputStream input stream
     */
    private void init(InputStream inputStream) {
        this.inputStream = inputStream;
        currentPath = new LinkedList<Node>();
        nodeQueue = new LinkedList<XmlNode>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty("javax.xml.stream.isCoalescing", true);
        factory.setProperty("javax.xml.stream.isReplacingEntityReferences", true);
        try {
            reader = factory.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            throw new XmlReaderException(e);
        }
    }

    /**
     * Find position in input stream that matches XML path query
     * @param xmlPathQuery XML path query
     * @return {@code boolean} true if found
     */
    public boolean find(String xmlPathQuery) {
        XmlPath xmlPath = XmlPathParser.parse(xmlPathQuery);

        XmlNode node;
        while((node = pullXmlNode()) != null) {
            if(node instanceof XmlStartElement) {
                XmlStartElement startElement = (XmlStartElement) node;
                Element element = new Element(startElement.getLocalName());
                element.addAttributes(startElement.getAttributes());
                currentPath.addLast(element);
                if(xmlPath.matches(currentPath)) {
                    nodeQueue.push(node);
                    currentPath.removeLast();
                    return true;
                }
            } else if(node instanceof XmlEndElement) {
                if(currentPath.getLast() instanceof Content) {
                    currentPath.removeLast();
                }
                currentPath.removeLast();
            } else if(node instanceof XmlContent) {
                XmlContent content = (XmlContent) node;
                if(currentPath.getLast() instanceof Content) {
                    currentPath.removeLast();
                }
                currentPath.addLast(new Content(content.getText()));
            } else {
                throw new XmlReaderException("Unknown XmlNode type: " + node);
            }
        }
        return false;
    }

    /**
     * Read an XML {@code Xml} object from the input stream
     * @param xmlPathQuery XML path query
     * @return {@code Xml}
     */
    public Xml read(String xmlPathQuery) {
        return read(xmlPathQuery, true);
    }

    /**
     * Read an XML {@code Xml} object from the input stream
     * @param xmlPathQuery XML path query
     * @param captureContent capture child content
     * @return {@code Xml}
     */
    public Xml read(String xmlPathQuery, boolean captureContent) {
        XmlPath xmlPath = XmlPathParser.parse(xmlPathQuery);
        Xml doc = new Xml();
        boolean record = false;
        int levelIndex = 0;

        XmlNode node;
        while((node = pullXmlNode()) != null) {
            if(node instanceof XmlStartElement) {
                XmlStartElement startElement = (XmlStartElement) node;
                Element element = new Element(startElement.getLocalName());
                element.addAttributes(startElement.getAttributes());
                currentPath.addLast(element);
                if(xmlPath.matches(currentPath)) {
                    if(!captureContent) {
                        // capture single element only
                        doc.addElement(element);
                        return doc;
                    }
                    // turn on document builder
                    record = true;
                }
                if(record) {
                    // build document
                    doc.addElement(element);
                    levelIndex += 1;
                }
            } else if(node instanceof XmlEndElement) {
                if(xmlPath.isSimplePattern() && levelIndex == 0) {
                    // a simple pattern query can not read further up
                    nodeQueue.push(node);
                    return null;
                }
                if(currentPath.getLast() instanceof Content) {
                    currentPath.removeLast();
                }
                currentPath.removeLast();
                if(record) {
                    // build document
                    doc.endElement();
                    levelIndex -= 1;
                    if(levelIndex == 0) {
                        return doc;
                    }
                }
            } else if(node instanceof XmlContent) {
                XmlContent content = (XmlContent) node;
                if(currentPath.getLast() instanceof Content) {
                    currentPath.removeLast();
                }
                currentPath.addLast(new Content(content.getText()));
                if(record) {
                    // build document
                    doc.addContent(new Content(content.getText()));
                }
            } else {
                // don't know how to handle this type, internal error
                throw new XmlReaderException("Unknown XmlNode type: " + node);
            }
        }
        // no element was found matching the query
        return null;
    }

    /**
     * Pull an XML node object from the input stream
     * @return {@code XmlNode}
     */
    private XmlNode pullXmlNode() {
        // read from queue
        if(! nodeQueue.isEmpty()) {
            return nodeQueue.poll();
        }

        // read from stream
        try {
            while(reader.hasNext()) {
                int event = reader.next();
                switch(event) {
                    case XMLStreamConstants.START_ELEMENT:
                        return new XmlStartElement(reader.getLocalName(), getAttributes(reader));
                    case XMLStreamConstants.CHARACTERS:
                        // capture XML?
                        String text = filterWhitespace(reader.getText());
                        if(text.length() > 0) {
                            return new XmlContent(text);
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        return new XmlEndElement(reader.getLocalName());
                    default:
                        // do nothing
                }
            }
        } catch (XMLStreamException e) {
            throw new XmlReaderException(e);
        }
        // nothing to return
        return null;
    }

    /**
     * This method filters out white space characters
     * @param text original text
     * @return {@code String} filtered text
     */
    private String filterWhitespace(String text) {
        text = text.replace("\n", " ");
        text = text.replaceAll(" {2,}", " ");
        text = XmlEscape.escape(text);
        return text.trim();
    }

    /**
     * Silent close on {@code InputStream} objects
     */
    public void close() {
        try {
            if(inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * Extract XML element attributes form XML input stream
     * @param reader XML reader
     * @return {@code List} of {@code Attribute} items
     */
    private List<Attribute> getAttributes(XMLStreamReader reader) {
        List<Attribute> list = new ArrayList<Attribute>();
        for(int i=0; i<reader.getAttributeCount(); i++) {
            list.add(new Attribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i)));
        }
        return list;
    }
}
