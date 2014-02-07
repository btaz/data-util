package com.btaz.util.xml;

import com.btaz.util.xml.model.Xml;
import com.btaz.util.unit.ResourceUtil;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * User: msundell
 */
public class XmlReaderTest {
    @Test
    public void testOfSimpleAnyElementQueryShouldFindMatch() throws Exception {
        // given
        InputStream inputStream = ResourceUtil.getResourceAsStream("things.xml");
        XmlReader reader = new XmlReader(inputStream);

        // when
        boolean result = reader.find("*");
        reader.close();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfTargetedNodenameAttributeNameValueQueryShouldFindAndRetrieveDocument() throws Exception {
        // given
        InputStream inputStream = ResourceUtil.getResourceAsStream("things.xml");
        XmlReader reader = new XmlReader(inputStream);
        String expected = "<planet order=\"5\" name=\"Jupiter\" dia=\"11.209\" au=\"5.20\" moons=\"67\" mass=\"317.8\" orbitalPeriod=\"11.86\" rings=\"yes\"></planet>";

        // when
        boolean result = reader.find("/things/planets/planet[@orbitalPeriod='11.86']");
        Xml doc = reader.read("/things/planets/planet[@orbitalPeriod='11.86']");
        reader.close();

        // then
        assertThat(result, is(true));
        assertThat(doc.toString(true), is(equalTo(expected)));
    }

    @Test
    public void testOfNodenameQueryShouldFindMatch() throws Exception {
        // given
        InputStream inputStream = ResourceUtil.getResourceAsStream("things.xml");
        XmlReader reader = new XmlReader(inputStream);

        // when
        boolean result = reader.find("/things/metals/gold");
        reader.close();

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfFindAndReadShouldFindDocument() throws Exception {
        // given
        InputStream inputStream = ResourceUtil.getResourceAsStream("things.xml");
        XmlReader reader = new XmlReader(inputStream);

        // when
        boolean result = reader.find("/things/metals/gold");
        Xml doc = reader.read("/things/metals/gold");
        reader.close();

        // then
        assertThat(result, is(true));
        assertThat(doc, is(not(nullValue())));
        assertThat(doc.getRoot().getName(), is(equalTo("gold")));
    }

    @Test
    public void testOfFindAndMultipleReadsShouldRetrieveDocuments() throws Exception {
        // given
        InputStream inputStream = ResourceUtil.getResourceAsStream("things.xml");
        XmlReader reader = new XmlReader(inputStream);

        // when
        boolean result = reader.find("/things/fruits/fruit");
        Xml doc1 = reader.read("*");
        Xml doc2 = reader.read("*");
        Xml doc3 = reader.read("*");
        Xml doc4 = reader.read("*");
        Xml doc5 = reader.read("*");
        Xml doc6 = reader.read("*");
        Xml doc7 = reader.read("*");
        boolean result2 = reader.find("/things/metals");
        reader.close();

        // then
        assertThat(result, is(true));
        // - strawberry
        assertThat(doc1, is(not(nullValue())));
        assertThat(doc1.getRoot().attributeValue("name"), is(equalTo("strawberry")));
        // - orange
        assertThat(doc2, is(not(nullValue())));
        assertThat(doc2.getRoot().attributeValue("name"), is(equalTo("orange")));
        // - lemon
        assertThat(doc3, is(not(nullValue())));
        assertThat(doc3.getRoot().attributeValue("name"), is(equalTo("lemon")));
        // - pineapple
        assertThat(doc4, is(not(nullValue())));
        assertThat(doc4.getRoot().attributeValue("name"), is(equalTo("pineapple")));
        // - apple
        assertThat(doc5, is(not(nullValue())));
        assertThat(doc5.getRoot().attributeValue("name"), is(equalTo("apple")));
        // - pear
        assertThat(doc6, is(not(nullValue())));
        assertThat(doc6.getRoot().attributeValue("name"), is(equalTo("pear")));
        // - none
        assertThat(doc7, is(nullValue()));
        assertThat(result2, is(true));
    }

    @Test
    public void testWithSimpleXmlFileShouldExtractSubTrees() throws Exception {
        // given
        String expected0 = "<result name=\"response\" numFound=\"3\" start=\"0\">";
        String expected1 = "<doc><str name=\"id\">Demo+1</str><arr name=\"fruits\"><str>apple</str><str>orange &apos;n mandarin</str></arr><str name=\"country\">US</str></doc>";
        String expected2 = "<doc><str name=\"id\">Demo+2</str><arr name=\"fruits\"><str>pear</str><str>banana</str></arr><str name=\"country\">DE</str></doc>";
        String expected3 = "<doc><str name=\"id\">Demo+3</str><arr name=\"fruits\"><str>lemon</str><str>grapes &amp; berries</str></arr><str name=\"country\">FR</str></doc>";

        File inputFile = ResourceUtil.getResourceFile("sample-5.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        XmlReader reader = new XmlReader(inputStream);

        // when

        // - find element by XML path, then load all data into a node
        Xml doc0 = reader.read("/response/result", false);
        Xml doc1 = reader.read("/response/result/doc");
        Xml doc2 = reader.read("/response/result/doc");
        Xml doc3 = reader.read("/response/result/doc");
        Xml doc4 = reader.read("/response/result/doc");
        inputStream.close();

        // then
        Assert.assertThat(doc0.rootElementAsTagXml(), is(IsEqual.equalTo(expected0)));
        Assert.assertThat(doc1.toString(true), is(IsEqual.equalTo(expected1)));
        Assert.assertThat(doc2.toString(true), is(IsEqual.equalTo(expected2)));
        Assert.assertThat(doc3.toString(true), is(IsEqual.equalTo(expected3)));
        Assert.assertThat(doc4, is(IsEqual.equalTo(null)));
    }

    @Test
    public void testWithSimpleXmlStringShouldExtractSubTrees() throws Exception {
        // given
        String expected = "<doc><str name=\"id\">Demo+2</str><arr name=\"fruits\"><str>pear</str><str>banana</str></arr><str name=\"country\">DE</str></doc>";

        String xmlString =
                        "<doc>" +
                        "    <str name=\"id\">Demo+2</str>" +
                        "    <arr name=\"fruits\"><str>pear</str><str>banana</str></arr>" +
                        "    <str name=\"country\">DE</str>" +
                        "</doc>";

        XmlReader reader = new XmlReader(xmlString);
        reader.close();

        // when
        // - find element by XML path, then load all data into a node
        Xml doc = reader.read("/doc");

        // then
        Assert.assertThat(doc.toString(true), is(IsEqual.equalTo(expected)));
    }

    @Test
    public void testReadingAllXmlFromAResourceShouldReadTheWholeModel() throws Exception {
        // given
        String resourceName = "/fruits.xml";
        String expectedXml = "<fruits><fruit name=\"apple\">Green</fruit><fruit name=\"orange\">Orange</fruit>" +
                             "<fruit name=\"pear\">Green</fruit></fruits>";

        // when
        Xml xml = XmlReader.readAllFromResource(resourceName);

        // then
        assertThat(xml, is(not(nullValue())));
        assertThat(xml.toString(true), is(equalTo(expectedXml)));
    }
}
