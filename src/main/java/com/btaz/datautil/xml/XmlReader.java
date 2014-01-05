package com.btaz.datautil.xml;

import com.btaz.datautil.DataUtilDefaults;
import com.btaz.datautil.xml.model.Document;
import com.btaz.datautil.xml.xmlpath.XmlPath;
import com.btaz.datautil.xml.xmlpath.XmlPathElement;
import com.btaz.datautil.xml.xmlpath.XmlPathElementAttribute;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

    /**
     * Initialize the XmlReader with an {@code InputStream} object
     * @param inputStream {@code InputStream}
     * @throws XmlReaderException XML reader exception
     */
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

    /**
     * Convenience method to create an {@code InputStream} from a {@code String}.
     * Don't forget to close the InputStream afterwards.
     * @param data input String
     * @return InputStream
     */
    public static InputStream toInputStream(String data) {
        try {
            return new ByteArrayInputStream(data.getBytes(DataUtilDefaults.charSet));
        } catch (UnsupportedEncodingException e) {
            throw new XmlReaderException(e);
        }
    }

    /**
     * Silent close on {@code InputStream} objects
     * @param inputStream InputStream object
     */
    public static void silentClose(InputStream inputStream) {
        try {
            if(inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * Read an XML element and it's content on an XPath like path
     * @param xmlPathQuery XML path pathQuery
     * @return <code>Document</code> captured XML model or null if there's no match
     */
    public Document read(String xmlPathQuery) {
        return read(xmlPathQuery, true);
    }

    /**
     * Read an XML element, or an XML element and it's content on an XPath like path
     * @param xmlPathQuery XML path pathQuery
     * @param captureContent capture content, if true capture the content within an element
     * @return <code>Document</code> captured XML model or null if there's no match
     */
    public Document read(String xmlPathQuery, boolean captureContent) {
        XmlPath xmlPath = new XmlPath(xmlPathQuery);
        Document doc = new Document();
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
                        List<XmlPathElementAttribute> attributes = getAttributes(reader);
                        element = new XmlPathElement(localName, attributes);

                        // set current path
                        elementPath.add(element);

                        // path match?
                        currentPath = new XmlPath(elementPath);
                        if(xmlPath.equals(currentPath)) {
                            // found the path
                            captureXml = true;
                        }

                        // capture XML?
                        if(captureXml) {
                            doc.addElement(element.toString());
                        }

                        // only capture start tag?
                        if(! captureContent && xmlPath.equals(currentPath)) {
                            return doc;
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
                            if(text.length() > 0) {
                                doc.addContent(text);
                            }
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        // element
                        localName = reader.getLocalName();
                        element = new XmlPathElement(localName);

                        // capture XML?
                        if(captureXml) {
                            doc.endElement();
                        }

                        // path match?
                        currentPath = new XmlPath(elementPath);
                        if(xmlPath.equals(currentPath)) {
                            // found the closing path
                            elementPath.remove(elementPath.size() - 1);
                            return doc;
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

        // no data was found for the pathQuery
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
