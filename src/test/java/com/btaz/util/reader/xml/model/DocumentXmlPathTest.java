package com.btaz.util.reader.xml.model;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class DocumentXmlPathTest {
    @Test
    public void testOfPathQueryShouldFindElements() throws Exception {
        // given
        Document xml = new Document();
        xml.addElement("<fruits>")
                .addContent("Hey Fruity")
                .addElement("<banana />")
                .addElement("<orange />")
                .addElement("<lemon>");

        // when
        List<Node> nodes = xml.pathQuery("/fruits/orange");

        // then
        assertThat(nodes, is(not(nullValue())));
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0) instanceof Element, is(true));
        assertThat(((Element)nodes.get(0)).getName(), is(equalTo("orange")));
    }

    @Test
    public void testOfPathQueryShouldFindBothElements() throws Exception {
        // given
        Document xml = new Document();
        xml.addElement("<fruits>")
                .addContent("Hey Fruity")
                .addElement("<banana />")
                .addElement("<orange />")
                .addElement("<orange />")
                .addElement("<lemon>");

        // when
        List<Node> nodes = xml.pathQuery("/fruits/orange");

        // then
        assertThat(nodes, is(not(nullValue())));
        assertThat(nodes.size(), is(2));
        assertThat(nodes.get(0) instanceof Element, is(true));
        assertThat(nodes.get(1) instanceof Element, is(true));
        assertThat(((Element)nodes.get(0)).getName(), is(equalTo("orange")));
        assertThat(((Element)nodes.get(1)).getName(), is(equalTo("orange")));
    }

    @Test
    public void testOfPathQueryShouldNotFindElements() throws Exception {
        // given
        Document xml = new Document();
        xml.addElement("<fruits>")
                .addContent("Hey Fruity")
                .addElement("<banana />")
                .addElement("<orange />")
                .addElement("<berries>")
                .addElement("<blueberry />")
                .endElement()
                .addElement("<lemon>");

        // when
        List<Node> nodes = xml.pathQuery("/fruits/lingonberry");

        // then
        assertThat(nodes, is(not(nullValue())));
        assertThat(nodes.size(), is(0));
    }

    @Test
    public void testOfPathQueryWithAttributeNameShouldFindElements() throws Exception {
        // given
        Document xml = new Document();
        xml.addElement("<groceries>")
                .addElement("<fruit name=\"banana\" />")
                .addElement("<fruit name=\"orange\" />")
                .addElement("<container product=\"milk\" />")
                .addElement("<berry name=\"blueberry\">")
                .endElement()
                .addElement("<box product=\"cornflakes\" />")
                .endElement();

        // when
        List<Node> nodes = xml.pathQuery("/groceries/@name");

        // then
        assertThat(nodes, is(not(nullValue())));
        assertThat(nodes.size(), is(3));
        assertThat(nodes.get(0).toString(), is(equalTo("<fruit name=\"banana\" />")));
        assertThat(nodes.get(1).toString(), is(equalTo("<fruit name=\"orange\" />")));
        assertThat(nodes.get(2).toString(), is(equalTo("<berry name=\"blueberry\">")));
    }

    @Test
    public void testOfPathQueryWithAttributeNameValueShouldFindElements() throws Exception {
        // given
        Document xml = new Document();
        xml.addElement("<groceries>")
                .addElement("<fruit name=\"banana\" />")
                .addElement("<fruit name=\"orange\" />")
                .addElement("<container product=\"milk\" />")
                .addElement("<berry name=\"blueberry\">")
                .endElement()
                .addElement("<box product=\"cornflakes\" />")
                .endElement();

        // when
        List<Node> nodes = xml.pathQuery("/groceries/@name='blueberry'");

        // then
        assertThat(nodes, is(not(nullValue())));
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0).toString(), is(equalTo("<berry name=\"blueberry\">")));
    }

    @Test
    public void testOfPathQueryWithElementAndAttributeNameShouldFindElements() throws Exception {
        // given
        Document xml = new Document();
        xml.addElement("<groceries>")
                .addElement("<fruit name=\"banana\" />")
                .addElement("<fruit name=\"orange\" />")
                .addElement("<container product=\"milk\" />")
                .addElement("<berry name=\"blueberry\">")
                .endElement()
                .addElement("<box product=\"cornflakes\" />")
                .endElement();

        // when
        List<Node> nodes = xml.pathQuery("/groceries/fruit[@name]");

        // then
        assertThat(nodes, is(not(nullValue())));
        assertThat(nodes.size(), is(2));
        assertThat(nodes.get(0).toString(), is(equalTo("<fruit name=\"banana\" />")));
        assertThat(nodes.get(1).toString(), is(equalTo("<fruit name=\"orange\" />")));
    }

    @Test
    public void testOfPathQueryWithElementAndAttributeNameValueShouldFindElements() throws Exception {
        // given
        Document xml = new Document();
        xml.addElement("<groceries>")
                .addElement("<fruit name=\"banana\" />")
                .addElement("<fruit name=\"orange\" />")
                .addElement("<container product=\"milk\" />")
                .addElement("<berry name=\"blueberry\">")
                .endElement()
                .addElement("<box product=\"cornflakes\" />")
                .endElement();

        // when
        List<Node> nodes = xml.pathQuery("/groceries/container[@product='milk']");

        // then
        assertThat(nodes, is(not(nullValue())));
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0).toString(), is(equalTo("<container product=\"milk\" />")));
    }
}
