package com.btaz.util.xml.xmlpath;

import com.btaz.util.xml.model.Content;
import com.btaz.util.xml.model.Element;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * User: msundell
 */
public class XmlPathNodenameTest {
    private XmlPathNodename xmlPathItem;

    @Test
    public void testWithElementShouldPass() throws Exception {
        // given
        xmlPathItem = new XmlPathNodename("fruits");
        Element element = new Element("fruits");

        // when
        boolean result = xmlPathItem.matches(element);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testWithElementAndAttributeShouldPass() throws Exception {
        // given
        xmlPathItem = new XmlPathNodename("fruits");
        Element element = new Element("fruits");
        element.addAttribute("name", "apple");

        // when
        boolean result = xmlPathItem.matches(element);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testWithContentNodeShouldFail() throws Exception {
        // given
        xmlPathItem = new XmlPathNodename("fruits");
        Content content = new Content("fruits");

        // when
        boolean result = xmlPathItem.matches(content);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testWithNodeNullValueShouldFail() throws Exception {
        // given
        xmlPathItem = new XmlPathNodename("fruits");
        Element element = null;

        // when
        boolean result = xmlPathItem.matches(element);

        // then
        assertThat(result, is(false));
    }
}
