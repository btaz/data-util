package com.btaz.util.xml.xmlpath;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class XmlPathParserTest {
    @Test
    public void testOfXmlPathParserWithAnyElementQueryShouldInitializeXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("*");

        // then
        assertThat(xmlPath.toString(), is(equalTo("*")));
    }

    @Test
    public void testOfXmlPathParserWithAnyAttributeElementQueryShouldInitializeXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("@*");

        // then
        assertThat(xmlPath.toString(), is(equalTo("@*")));
    }

    @Test
    public void testOfXmlPathParserWithAnyNodeQueryShouldInitializeAnyXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("node()");

        // then
        assertThat(xmlPath.toString(), is(equalTo("node()")));
    }

    @Test
    public void testOfXmlPathParserWithNodenameQueryShouldInitializeXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("/fruits");

        // then
        assertThat(xmlPath.toString(), is(equalTo("/fruits")));
    }

    @Test
    public void testOfXmlPathParserWithNestedNodenamesQueryShouldInitializeXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("/fruits/fruit/apple");

        // then
        assertThat(xmlPath.toString(), is(equalTo("/fruits/fruit/apple")));
    }

    @Test
    public void testOfXmlPathParserWithNodenamesAndChildElementQueryShouldInitializeXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("/fruits/*");

        // then
        assertThat(xmlPath.toString(), is(equalTo("/fruits/*")));
    }

    @Test
    public void testOfXmlPathParserWithNodenamesAndChildAttributeElementQueryShouldInitializeXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("/fruits/@*");

        // then
        assertThat(xmlPath.toString(), is(equalTo("/fruits/@*")));
    }

    @Test
    public void testOfXmlPathParserWithNodenamesAndChildAnyNodeQueryShouldInitializeXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("/fruits/node()");

        // then
        assertThat(xmlPath.toString(), is(equalTo("/fruits/node()")));
    }

    @Test
    public void testOfXmlPathParserWithSpecificNodenameAndChildNodenameAnyAttributeShouldInitializeXmlPath() throws Exception {
        // given

        // when
        XmlPath xmlPath = XmlPathParser.parse("/fruits/fruit[@*]");

        // then
        assertThat(xmlPath.toString(), is(equalTo("/fruits/fruit[@*]")));
    }
}
