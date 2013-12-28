package com.btaz.datautil.xml;

import com.btaz.datautil.xml.xmlpath.XmlPathElementAttribute;
import com.btaz.datautil.xml.xmlpath.XmlPathElement;
import com.btaz.datautil.xml.xmlpath.XmlPath;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an XML utility class that makes it easier to extract XML subtrees as a single row of data.
 * This class has a few limitations. Character data is modified e.g. EOLs are replaced with space. Multiple consecutive
 * spaces will be collapsed into a single one. All characters that XML can't handle will be escaped i.e. &amp; &quot;
 * &apos; &lt; &gt;
 * User: msundell
 */
public class XmlReader {
    private XMLStreamReader reader;
    private ArrayList<XmlPathElement> elementPath;

    public XmlReader(InputStream inputStream) throws XmlReaderException {
        elementPath = new ArrayList<XmlPathElement>();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty("javax.xml.stream.isCoalescing", true);
        factory.setProperty("javax.xml.stream.isReplacingEntityReferences", true);
        try {
            reader = factory.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            throw new XmlReaderException(e);
        }
    }

    public String read(String xmlPathQuery) {
        XmlPath xmlPath = new XmlPath(xmlPathQuery);
        StringBuilder xmlData = new StringBuilder();
        boolean captureXml = false;

        try {
            String localName;
            XmlPathElement element;
            XmlPath currentPath;
            while(reader.hasNext()) {
                int event = reader.next();
                switch(event) {
                    case XMLStreamConstants.START_ELEMENT:
                        // element
                        localName = reader.getLocalName();
                        List attributes = getAttributes(reader);
                        element = new XmlPathElement(localName, attributes);

                        // set current path
                        elementPath.add(element);

                        // path match?
                        currentPath = new XmlPath(elementPath);
                        if(xmlPath.equals(currentPath)) {
                            // found the path
                            captureXml = true;
                            xmlData = new StringBuilder();
                        }

                        // capture XML?
                        if(captureXml) {
                            xmlData.append(element.toString());
                        }

                        break;
                    case XMLStreamConstants.CHARACTERS:
                        // capture XML?
                        if(captureXml) {
                            String text = reader.getText();
                            text = text.replace("\n", " ");
                            text = text.replaceAll(" {2,}", " ");
                            text = text.replaceAll("&", "&amp;");
                            text = text.replaceAll("\"", "&quot;");
                            text = text.replaceAll("'", "&apos;");
                            text = text.replaceAll("<", "&lt;");
                            text = text.replaceAll(">", "&gt;");
                            text = text.trim();
                            xmlData.append(text);
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        // element
                        localName = reader.getLocalName();
                        element = new XmlPathElement(localName);

                        // capture XML?
                        if(captureXml) {
                            xmlData.append(element.toString(true));
                        }

                        // path match?
                        currentPath = new XmlPath(elementPath);
                        if(xmlPath.equals(currentPath)) {
                            // found the closing path
                            elementPath.remove(elementPath.size() - 1);
                            return xmlData.toString();
                        }

                        // set current path
                        elementPath.remove(elementPath.size() - 1);

                        break;
                    case XMLStreamConstants.START_DOCUMENT:
                        break;
                    default:
                        // do nothing
                }
            }
        } catch (XMLStreamException e) {
            throw new XmlReaderException(e);
        }

        if(captureXml) {
            throw new XmlReaderException("Invalid input stream, found opening tag but no closing tag for: " + xmlPathQuery);
        }

        // no data was found for the query
        return null;
    }

    private List<XmlPathElementAttribute> getAttributes(XMLStreamReader reader) {
        ArrayList<XmlPathElementAttribute> list = new ArrayList<XmlPathElementAttribute>();
        for(int i=0; i<reader.getAttributeCount(); i++) {
            list.add(new XmlPathElementAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i)));
        }
        return list;
    }
}
