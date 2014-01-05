package com.btaz.datautil.xml.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class ElementBuilderTest {
    private ElementBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new ElementBuilder();
    }

    @Test(expected = XmlModelException.class)
    public void testOfAttributesWithNullValueStringShouldThrowException() throws Exception {
        // given

        // when
        builder.getAttributes(null);

        // then
    }

    @Test
    public void testOfAttributesWithEmptyStringShouldCreateEmptyAttributeList() throws Exception {
        // given

        // when
        List<Attribute> attributes = builder.getAttributes("    ");

        // then
        assertThat(attributes, is(not(nullValue())));
        assertThat(attributes.size(), is(equalTo(0)));
    }

    @Test(expected = XmlModelException.class)
    public void testOfAttributesWithInvalidAttributeNameShouldThrowException() throws Exception {
        // given
        String input = "\"rows\"=\"10\"";

        // when
        builder.getAttributes(input);

        // then
    }

    @Test(expected = XmlModelException.class)
    public void testOfAttributesWithInvalidAttributeValueShouldThrowException() throws Exception {
        // given
        String input = "rows=\"\"10\"";

        // when
        builder.getAttributes(input);

        // then
    }

    @Test
    public void testOfAttributesWithValidAttributeStringShouldCreateAttributesList() throws Exception {
        // given
        String input = "rows=\"10\" columns=\"4\" ";

        // when
        List<Attribute> attributes = builder.getAttributes(input);

        // then
        assertThat(attributes, is(not(nullValue())));
        assertThat(attributes.size(), is(equalTo(2)));
        assertThat(attributes.get(0).getName(), is(equalTo("rows")));
        assertThat(attributes.get(0).getValue(), is(equalTo("10")));
        assertThat(attributes.get(1).getName(), is(equalTo("columns")));
        assertThat(attributes.get(1).getValue(), is(equalTo("4")));
    }

    @Test
    public void testOfAttributesWithValidAttributeStringExtraPaddingShouldCreateAttributesList() throws Exception {
        // given
        String input = "  rows  =  \"25\"   columns   =   \"8\"   ";

        // when
        List<Attribute> attributes = builder.getAttributes(input);

        // then
        assertThat(attributes, is(not(nullValue())));
        assertThat(attributes.size(), is(equalTo(2)));
        assertThat(attributes.get(0).getName(), is(equalTo("rows")));
        assertThat(attributes.get(0).getValue(), is(equalTo("25")));
        assertThat(attributes.get(1).getName(), is(equalTo("columns")));
        assertThat(attributes.get(1).getValue(), is(equalTo("8")));
    }


    @Test
    public void testOfAttributesWithValidAttributeStringWithEmptyValueShouldCreateAttributesList() throws Exception {
        // given
        String input = "  rows  =  \"\"   columns   =   \"\"   ";

        // when
        List<Attribute> attributes = builder.getAttributes(input);

        // then
        assertThat(attributes, is(not(nullValue())));
        assertThat(attributes.size(), is(equalTo(2)));
        assertThat(attributes.get(0).getName(), is(equalTo("rows")));
        assertThat(attributes.get(0).getValue(), is(equalTo("")));
        assertThat(attributes.get(1).getName(), is(equalTo("columns")));
        assertThat(attributes.get(1).getValue(), is(equalTo("")));
    }

    @Test(expected = XmlModelException.class)
    public void testOfElementWithNullValueParameterShouldThrowException() throws Exception {
        // given

        // when
        builder.getElement(null);

        // then
    }

    @Test(expected = XmlModelException.class)
    public void testOfElementWithInvalidElementXmlShouldThrowException() throws Exception {
        // given
        String input = " <result name=\"response\" numFound=\"274278\" start=\"0\" ";

        // when
        builder.getElement(input);

        // then
    }

    @Test
    public void testOfElementWithValidElementXmlShouldCreateElement() throws Exception {
        // given
        String input = " <result name=\"response\" numFound=\"274278\" start=\"0\"> ";

        // when
        Element element = builder.getElement(input);

        // then
        assertThat(element, is(not(nullValue())));
        assertThat(element.getName(), is(equalTo("result")));
        assertThat(element.isEmptyElementTag(), is(false));
        assertThat(element.attributeCount(), is(equalTo(3)));
        assertThat(element.attributeValue("name"), is(equalTo("response")));
        assertThat(element.attributeValue("numFound"), is(equalTo("274278")));
        assertThat(element.attributeValue("start"), is(equalTo("0")));
    }

    @Test
    public void testOfElementWithValidElementXmlAndExtraPaddingShouldCreateElement() throws Exception {
        // given
        String input = " <  result  name  =  \"response\"  numFound  =  \"12\"  start  =  \"10\"  > ";

        // when
        Element element = builder.getElement(input);

        // then
        assertThat(element, is(not(nullValue())));
        assertThat(element.getName(), is(equalTo("result")));
        assertThat(element.isEmptyElementTag(), is(false));
        assertThat(element.attributeValue("name"), is(equalTo("response")));
        assertThat(element.attributeValue("numFound"), is(equalTo("12")));
        assertThat(element.attributeValue("start"), is(equalTo("10")));
    }

    @Test
    public void testOfElementWithValidCloseTagElementXmlShouldCreateElement() throws Exception {
        // given
        String input = " <result name=\"response\" numFound=\"555\" start=\"3\" / > ";

        // when
        Element element = builder.getElement(input);

        // then
        assertThat(element, is(not(nullValue())));
        assertThat(element.getName(), is(equalTo("result")));
        assertThat(element.isEmptyElementTag(), is(true));
        assertThat(element.attributeValue("name"), is(equalTo("response")));
        assertThat(element.attributeValue("numFound"), is(equalTo("555")));
        assertThat(element.attributeValue("start"), is(equalTo("3")));
    }

    @Test
    public void testOfElementWithNoAttributesToStringShouldCreateValidXmlString() throws Exception {
        // given
        String input = " < fruit > ";
        String expected = "<fruit>";

        // when
        Element element = builder.getElement(input);

        // then
        assertThat(element, is(not(nullValue())));
        assertThat(element.toString(), is(equalTo(expected)));
    }

    @Test
    public void testOfElementWithAttributesToStringShouldCreateValidXmlString() throws Exception {
        // given
        String input = " <result name=\"response\" numFound=\"274278\" start=\"0\"> ";
        String expected = "<result name=\"response\" numFound=\"274278\" start=\"0\">";

        // when
        Element element = builder.getElement(input);

        // then
        assertThat(element, is(not(nullValue())));
        assertThat(element.toString(), is(equalTo(expected)));
    }

    @Test
    public void testOfElementWithEmptyElementTagToStringShouldCreateValidXmlString() throws Exception {
        // given
        String input = " <result name=\"response\" / > ";
        String expected = "<result name=\"response\" />";

        // when
        Element element = builder.getElement(input);

        // then
        assertThat(element, is(not(nullValue())));
        assertThat(element.toString(), is(equalTo(expected)));
    }
}
