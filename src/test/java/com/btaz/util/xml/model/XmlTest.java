package com.btaz.util.xml.model;

import com.btaz.util.xml.XmlReader;
import com.btaz.util.unit.ResourceUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class XmlTest {
    private Xml xml;

    @Before
    public void setUp() throws Exception {
        xml = new Xml();
    }

    @Test
    public void testOfXmlModelInitializationShouldCreateAnEmptyModel() throws Exception {
        // given

        // when
        Xml xmlModel = new Xml();

        // then
        assertThat(xmlModel.getRoot(), is(nullValue()));
    }

    @Test
    public void testOfBuildingAnXmlModelShouldCreateAValidModel() throws Exception {
        // given

        // when
        xml.addElement(" <response> ")
                .addElement(" <lst name=\"responseHeader\"> ")
                .endElement()
                .addElement(" <result name=\"response\" numFound=\"274278\" start=\"0\"/> ");

        // then
        assertThat(xml.getRoot(), is(not(nullValue())));
        assertThat(xml.getRoot().getParent(), is(nullValue()));
        assertThat(xml.getRoot().getName(), is(equalTo("response")));
        assertThat(xml.getRoot().childElementCount(), is(equalTo(2)));
        assertThat(((Element) xml.getRoot().getChildElements().get(0)).getName(), is(equalTo("lst")));
        assertThat(((Element) xml.getRoot().getChildElements().get(0)).hasAttribute("name"), is(true));
        assertThat(((Element) xml.getRoot().getChildElements().get(0)).attributeValue("name"), is(equalTo("responseHeader")));
        assertThat(((Element) xml.getRoot().getChildElements().get(1)).getName(), is(equalTo("result")));
        assertThat(((Element) xml.getRoot().getChildElements().get(1)).hasAttribute("name"), is(true));
        assertThat(((Element) xml.getRoot().getChildElements().get(1)).attributeValue("name"), is(equalTo("response")));
        assertThat(((Element) xml.getRoot().getChildElements().get(1)).hasAttribute("numFound"), is(true));
        assertThat(((Element) xml.getRoot().getChildElements().get(1)).attributeValue("numFound"), is(equalTo("274278")));
        assertThat(((Element) xml.getRoot().getChildElements().get(1)).hasAttribute("start"), is(true));
        assertThat(((Element) xml.getRoot().getChildElements().get(1)).attributeValue("start"), is(equalTo("0")));
    }

    @Test
    public void testOfBuildingAnXmlModelWithContentShouldCreateAValidModel() throws Exception {
        // given

        // when
        xml.addElement(" <response> ")
                .addContent("My name is Bob")
                .endElement();

        // then
        assertThat(xml.getRoot(), is(not(nullValue())));
        assertThat(xml.getRoot().getParent(), is(nullValue()));
        assertThat(xml.getRoot().getName(), is(equalTo("response")));
        assertThat(xml.getRoot().childElementCount(), is(equalTo(1)));
        assertThat(((Content) xml.getRoot().getChildElements().get(0)).getText(), is(equalTo("My name is Bob")));
    }

    @Test
    public void testOfHashCodeMethodForDifferentDocumentsShouldYieldDifferentHashCodes() throws Exception {
        // given
        Xml xml1 = new Xml();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Xml xml2 = new Xml();
        xml2.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<lemon />");

        // when
        int hashCode1 = xml1.hashCode();
        int hashCode2 = xml2.hashCode();

        // then
        assertThat(hashCode1, is(not(equalTo(0))));
        assertThat(hashCode2, is(not(equalTo(0))));
        assertThat(hashCode1, is(not(equalTo(hashCode2))));
    }

    @Test
    public void testOfDocumentHashCodeMethodForIdenticalDocumentsShouldYieldIdenticalHashCodes() throws Exception {
        // given
        Xml xml1 = new Xml();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Xml xml2 = new Xml();
        xml2.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        // when
        int hashCode1 = xml1.hashCode();
        int hashCode2 = xml2.hashCode();

        // then
        assertThat(hashCode1, is(not(equalTo(0))));
        assertThat(hashCode2, is(not(equalTo(0))));
        assertThat(hashCode1, is(equalTo(hashCode2)));
    }

    @Test
    public void testOfDocumentHashCodeMethodForNearIdenticalDifferentChildOrderDocumentsShouldHaveDifferentHashCodes() throws Exception {
        // given
        Xml xml1 = new Xml();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Xml xml2 = new Xml();
        xml2.addElement("<fruits>")
                .addElement("<orange />")
                .addElement("<banana />");

        // when
        int hashCode1 = xml1.hashCode();
        int hashCode2 = xml2.hashCode();

        // then
        assertThat(hashCode1, is(not(equalTo(0))));
        assertThat(hashCode2, is(not(equalTo(0))));
        assertThat(hashCode1, is(equalTo(hashCode2)));
    }

    @Test
    public void testOfDocumentHashCodeMethodForNearIdenticalDocumentsShouldYieldDifferentHashCodes() throws Exception {
        // given
        Xml xml1 = new Xml();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Xml xml2 = new Xml();
        xml2.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange>");

        // when
        int hashCode1 = xml1.hashCode();
        int hashCode2 = xml2.hashCode();

        // then
        assertThat(hashCode1, is(not(equalTo(0))));
        assertThat(hashCode2, is(not(equalTo(0))));
        assertThat(hashCode1, is(not(equalTo(hashCode2))));
    }

    @Test
    public void testOfEqualsMethodForIdenticalDocumentsShouldEqualsTrue() throws Exception {
        // given
        Xml xml1 = new Xml();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Xml xml2 = new Xml();
        xml2.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        // when
        boolean result = xml1.equals(xml2);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfEqualsMethodForDifferentDocumentsShouldEqualsFalse() throws Exception {
        // given
        Xml xml1 = new Xml();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange>").addContent("Juicy");

        Xml xml2 = new Xml();
        xml2.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        // when
        boolean result = xml1.equals(xml2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testOfEqualsMethodForNearIdenticalDocumentsShouldEqualsFalse() throws Exception {
        // given
        Xml xml1 = new Xml();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Xml xml2 = new Xml();
        xml2.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange>");

        // when
        boolean result = xml1.equals(xml2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testOfEqualsMethodForNearIdenticalDifferentChildOrderDocumentsShouldEqualsFalse() throws Exception {
        // given
        Xml xml1 = new Xml();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Xml xml2 = new Xml();
        xml2.addElement("<fruits>")
                .addElement("<orange />")
                .addElement("<banana />");

        // when
        boolean result = xml1.equals(xml2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testOfDocumentCloneMethodShouldCreateACompleteCopy() throws Exception {
        // given
        File inputFile = ResourceUtil.getResourceFile("sample-5.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        XmlReader reader = new XmlReader(inputStream);

        // when
        Xml doc = reader.read("/response");
        Xml doc2 = (Xml) doc.clone();
        Xml doc3 = (Xml) doc.clone();
        Element elem = (Element) doc3.pathQuery("/response/result/doc").get(0);
        elem.addChildElement(new Element("new"));

        // then
        assertThat(doc, is(not(nullValue())));
        assertThat(doc2, is(not(nullValue())));
        assertThat(doc, is(equalTo(doc2)));
        assertThat(doc.toString(), is(equalTo(doc2.toString())));
        assertThat(doc, is(not(equalTo(doc3))));
    }
}
