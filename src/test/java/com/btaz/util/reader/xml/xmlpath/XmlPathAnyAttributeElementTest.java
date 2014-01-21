package com.btaz.util.reader.xml.xmlpath;

import com.btaz.util.reader.xml.model.Content;
import com.btaz.util.reader.xml.model.Element;
import com.btaz.util.reader.xml.model.Node;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * User: msundell
 */
public class XmlPathAnyAttributeElementTest {
    private XmlPathAnyAttributeElement xmlPathItem;

    @Before
    public void setUp() throws Exception {
        xmlPathItem = new XmlPathAnyAttributeElement();
    }

    @Test
    public void testWithAttributeElementShouldPass() throws Exception {
        // given
        Element element = new Element("fruit");
        element.addAttribute("name", "apple");

        // when
        boolean result = xmlPathItem.matches(element);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testWithNoAttributeElementShouldFail() throws Exception {
        // given
        Element element = new Element("fruit");

        // when
        boolean result = xmlPathItem.matches(element);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testWithContentNodeShouldFail() throws Exception {
        // given
        Content content = new Content("Hello Dolly!");

        // when
        boolean result = xmlPathItem.matches(content);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testWithNullValueShouldFail() throws Exception {
        // given
        Node node = null;

        // when
        boolean result = xmlPathItem.matches(node);

        // then
        assertThat(result, is(false));
    }
}
