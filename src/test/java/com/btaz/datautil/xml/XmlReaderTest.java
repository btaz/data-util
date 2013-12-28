package com.btaz.datautil.xml;

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
        String expected1 = "<doc><str name=\"id\">Demo+1</str><arr name=\"fruits\"><str>apple</str><str>orange &apos;n mandarin</str></arr><str name=\"country\">US</str></doc>";
        String expected2 = "<doc><str name=\"id\">Demo+2</str><arr name=\"fruits\"><str>pear</str><str>banana</str></arr><str name=\"country\">DE</str></doc>";
        String expected3 = "<doc><str name=\"id\">Demo+3</str><arr name=\"fruits\"><str>lemon</str><str>grapes &amp; berries</str></arr><str name=\"country\">FR</str></doc>";
        String expected4 = null;

        File inputFile = ResourceUtil.getTestResourceFile("sample-5.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        XmlReader reader = new XmlReader(inputStream);

        // when

        // - find element by XML path, then load all data into a node
        String xmlString1 = reader.read("/response/result/doc");
        String xmlString2 = reader.read("/response/result/doc");
        String xmlString3 = reader.read("/response/result/doc");
        String xmlString4 = reader.read("/response/result/doc");
        inputStream.close();

        // then
        assertThat(xmlString1, is(equalTo(expected1)));
        assertThat(xmlString2, is(equalTo(expected2)));
        assertThat(xmlString3, is(equalTo(expected3)));
        assertThat(xmlString4, is(equalTo(expected4)));
    }
}
