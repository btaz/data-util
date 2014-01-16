package com.btaz.util.reader.xml.model;

import com.btaz.util.reader.xml.XmlReader;
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
public class DocumentTest {
    private Document model;

    @Before
    public void setUp() throws Exception {
        model = new Document();
    }

    @Test
    public void testOfXmlModelInitializationShouldCreateAnEmptyModel() throws Exception {
        // given

        // when
        Document newModel = new Document();

        // then
        assertThat(newModel.getRoot(), is(nullValue()));
    }

    @Test
    public void testOfBuildingAnXmlModelShouldCreateAValidModel() throws Exception {
        // given

        // when
        model.addElement(" <response> ")
                .addElement(" <lst name=\"responseHeader\"> ")
                .endElement()
                .addElement(" <result name=\"response\" numFound=\"274278\" start=\"0\"/> ");

        // then
        assertThat(model.getRoot(), is(not(nullValue())));
        assertThat(model.getRoot().getParent(), is(nullValue()));
        assertThat(model.getRoot().getName(), is(equalTo("response")));
        assertThat(model.getRoot().childElementCount(), is(equalTo(2)));
        assertThat(((Element)model.getRoot().getChildElements().get(0)).getName(), is(equalTo("lst")));
        assertThat(((Element)model.getRoot().getChildElements().get(0)).hasAttribute("name"), is(true));
        assertThat(((Element)model.getRoot().getChildElements().get(0)).attributeValue("name"), is(equalTo("responseHeader")));
        assertThat(((Element)model.getRoot().getChildElements().get(1)).getName(), is(equalTo("result")));
        assertThat(((Element)model.getRoot().getChildElements().get(1)).hasAttribute("name"), is(true));
        assertThat(((Element)model.getRoot().getChildElements().get(1)).attributeValue("name"), is(equalTo("response")));
        assertThat(((Element)model.getRoot().getChildElements().get(1)).hasAttribute("numFound"), is(true));
        assertThat(((Element)model.getRoot().getChildElements().get(1)).attributeValue("numFound"), is(equalTo("274278")));
        assertThat(((Element)model.getRoot().getChildElements().get(1)).hasAttribute("start"), is(true));
        assertThat(((Element)model.getRoot().getChildElements().get(1)).attributeValue("start"), is(equalTo("0")));
    }

    @Test
    public void testOfBuildingAnXmlModelWithContentShouldCreateAValidModel() throws Exception {
        // given

        // when
        model.addElement(" <response> ")
                .addContent("My name is Bob")
                .endElement();

        // then
        assertThat(model.getRoot(), is(not(nullValue())));
        assertThat(model.getRoot().getParent(), is(nullValue()));
        assertThat(model.getRoot().getName(), is(equalTo("response")));
        assertThat(model.getRoot().childElementCount(), is(equalTo(1)));
        assertThat(((Content)model.getRoot().getChildElements().get(0)).getText(), is(equalTo("My name is Bob")));
    }

    @Test
    public void testOfHashCodeMethodForDifferentDocumentsShouldYieldDifferentHashCodes() throws Exception {
        // given
        Document xml1 = new Document();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Document xml2 = new Document();
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
        Document xml1 = new Document();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Document xml2 = new Document();
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
        Document xml1 = new Document();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Document xml2 = new Document();
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
        Document xml1 = new Document();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Document xml2 = new Document();
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
        Document xml1 = new Document();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Document xml2 = new Document();
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
        Document xml1 = new Document();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange>").addContent("Juicy");

        Document xml2 = new Document();
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
        Document xml1 = new Document();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Document xml2 = new Document();
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
        Document xml1 = new Document();
        xml1.addElement("<fruits>")
                .addElement("<banana />")
                .addElement("<orange />");

        Document xml2 = new Document();
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
        Document doc = reader.read("/response");
        Document doc2 = (Document) doc.clone();
        Document doc3 = (Document) doc.clone();
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
