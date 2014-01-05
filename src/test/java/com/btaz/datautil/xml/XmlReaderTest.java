package com.btaz.datautil.xml;

import com.btaz.datautil.xml.model.Document;
import com.btaz.utils.ResourceUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class XmlReaderTest {
    @Test
    public void testWithSimpleXmlFileShouldExtractSubTrees() throws Exception {
        // given
        String expected0 = "<result name=\"response\" numFound=\"3\" start=\"0\">";
        String expected1 = "<doc><str name=\"id\">Demo+1</str><arr name=\"fruits\"><str>apple</str><str>orange &apos;n mandarin</str></arr><str name=\"country\">US</str></doc>";
        String expected2 = "<doc><str name=\"id\">Demo+2</str><arr name=\"fruits\"><str>pear</str><str>banana</str></arr><str name=\"country\">DE</str></doc>";
        String expected3 = "<doc><str name=\"id\">Demo+3</str><arr name=\"fruits\"><str>lemon</str><str>grapes &amp; berries</str></arr><str name=\"country\">FR</str></doc>";

        File inputFile = ResourceUtil.getTestResourceFile("sample-5.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        XmlReader reader = new XmlReader(inputStream);

        // when

        // - find element by XML path, then load all data into a node
        Document doc0 = reader.read("/response/result", false);
        Document doc1 = reader.read("/response/result/doc");
        Document doc2 = reader.read("/response/result/doc");
        Document doc3 = reader.read("/response/result/doc");
        Document doc4 = reader.read("/response/result/doc");
        inputStream.close();

        // then
        assertThat(doc0.rootElementAsTagXml(), is(equalTo(expected0)));
        assertThat(doc1.toString(true), is(equalTo(expected1)));
        assertThat(doc2.toString(true), is(equalTo(expected2)));
        assertThat(doc3.toString(true), is(equalTo(expected3)));
        assertThat(doc4, is(equalTo(null)));
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

        InputStream inputStream = XmlReader.toInputStream(xmlString);
        XmlReader reader = new XmlReader(inputStream);
        inputStream.close();

        // when

        // - find element by XML path, then load all data into a node
        Document doc = reader.read("/doc");

        // then
        assertThat(doc.toString(true), is(equalTo(expected)));
    }
}
