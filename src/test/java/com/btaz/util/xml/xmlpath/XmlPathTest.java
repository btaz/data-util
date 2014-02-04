package com.btaz.util.xml.xmlpath;

import com.btaz.util.xml.model.Element;
import com.btaz.util.xml.model.Node;
import org.junit.Test;

import java.util.LinkedList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class XmlPathTest {
    @Test
    public void testOfXmlPathWithAValidPathShouldCreateAListOfElements() throws Exception {
        // given
        String xmlPathQuery = "/response/result";
        LinkedList<Node> path = new LinkedList<Node>();
        path.add(new Element("response"));
        path.add(new Element("result"));

        // when
        XmlPath xmlPath = XmlPathParser.parse(xmlPathQuery);
        boolean result = xmlPath.matches(path);

        // then
        assertThat(result, is(true));
    }
}
