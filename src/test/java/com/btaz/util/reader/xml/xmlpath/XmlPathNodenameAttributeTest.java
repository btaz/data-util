package com.btaz.util.reader.xml.xmlpath;

import com.btaz.util.reader.xml.model.Element;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class XmlPathNodenameAttributeTest {
    @Test
    public void testOfAnyNodenameAnyAttributeShouldPass() throws Exception {
        // given
        String xmlPathQuery = "*[@*]";
        XmlPathNodenameAttribute query = new XmlPathNodenameAttribute(xmlPathQuery);

        Element element = new Element("fruit");
        element.addAttribute("name", "apple");

        // when
        boolean result = query.matches(element);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfSpecificNodenameAnyAttributeShouldPass() throws Exception {
        // given
        String xmlPathQuery = "fruit[@*]";
        XmlPathNodenameAttribute query = new XmlPathNodenameAttribute(xmlPathQuery);

        Element element = new Element("fruit");
        element.addAttribute("name", "apple");

        // when
        boolean result = query.matches(element);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfAnyNodenameSpecificAttributeNameShouldPass() throws Exception {
        // given
        String xmlPathQuery = "*[@name]";
        XmlPathNodenameAttribute query = new XmlPathNodenameAttribute(xmlPathQuery);

        Element element = new Element("fruit");
        element.addAttribute("name", "apple");

        // when
        boolean result = query.matches(element);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfAnyNodenameSpecificAttributeNameAndSpecificValueShouldPass() throws Exception {
        // given
        String xmlPathQuery = "*[@name='apple']";
        XmlPathNodenameAttribute query = new XmlPathNodenameAttribute(xmlPathQuery);

        Element element = new Element("fruit");
        element.addAttribute("name", "apple");

        // when
        boolean result = query.matches(element);

        // then
        assertThat(result, is(true));
    }


    @Test
    public void testOfAnyNodenameSpecificAttributeNameAndSpecificValueShouldFail() throws Exception {
        // given
        String xmlPathQuery = "*[@name='apple']";
        XmlPathNodenameAttribute query = new XmlPathNodenameAttribute(xmlPathQuery);

        Element element = new Element("fruit");
        element.addAttribute("name", "pear");

        // when
        boolean result = query.matches(element);

        // then
        assertThat(result, is(false));
    }

    @Test(expected = XmlPathException.class)
    public void testOfAnyNodenameAnyAttributeNameSpecificValueShouldThrowException() throws Exception {
        // given
        String xmlPathQuery = "*[@*='apple']";

        // when
        new XmlPathNodenameAttribute(xmlPathQuery);

        // then
    }
}
