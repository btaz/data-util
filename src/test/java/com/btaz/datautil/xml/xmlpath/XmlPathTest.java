package com.btaz.datautil.xml.xmlpath;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class XmlPathTest {
    @Test
    public void testOfXmlPathWithAValidPathShouldCreateAListOfElements() throws Exception {
        // given
        String xmlPathQuery = "//response/result";

        // when
        XmlPath xmlPath = new XmlPath(xmlPathQuery);

        // then
        List<XmlPathElement> elements = xmlPath.getElements();
        assertThat(elements, is(not(nullValue())));
        assertThat(xmlPath.toString(), is(equalTo("//response/result")));
        assertThat(elements.size(), is(equalTo(2)));
        assertThat(elements.get(0).getLocalName(), is(equalTo("response")));
        assertThat(elements.get(1).getLocalName(), is(equalTo("result")));
    }

    @Test
    public void testOfTwoXmlPathsWithIdenticalQueriesShouldMatch() throws Exception {
        // given
        String xmlPathQuery1 = "/response/result";
        String xmlPathQuery2 = "/response/result";

        // when
        XmlPath xmlPath1 = new XmlPath(xmlPathQuery1);
        XmlPath xmlPath2 = new XmlPath(xmlPathQuery2);

        // then
        assertThat(xmlPath1, is(not(nullValue())));
        assertThat(xmlPath2, is(not(nullValue())));
        assertThat(xmlPath1, is(equalTo(xmlPath2)));
        assertThat(xmlPath1.hashCode(), is(equalTo(xmlPath2.hashCode())));
    }

    @Test
    public void testOfTwoXmlPathsWithDifferentQueriesShouldNotMatch() throws Exception {
        // given
        String xmlPathQuery1 = "/response/result";
        String xmlPathQuery2 = "/response/banana";

        // when
        XmlPath xmlPath1 = new XmlPath(xmlPathQuery1);
        XmlPath xmlPath2 = new XmlPath(xmlPathQuery2);

        // then
        assertThat(xmlPath1, is(not(nullValue())));
        assertThat(xmlPath2, is(not(nullValue())));
        assertThat(xmlPath1.toString(), is(equalTo("//response/result")));
        assertThat(xmlPath2.toString(), is(equalTo("//response/banana")));
        assertThat(xmlPath1, is(not(equalTo(xmlPath2))));
        assertThat(xmlPath1.hashCode(), is(not(equalTo(xmlPath2.hashCode()))));
    }

    @Test
    public void testOfXmlPathWithAValidElementListShouldCreateAValidPath() throws Exception {
        // given
        List<XmlPathElement> elementList = new ArrayList<XmlPathElement>();
        elementList.add(new XmlPathElement("response"));
        elementList.add(new XmlPathElement("result"));
        elementList.add(new XmlPathElement("doc"));

        // when
        XmlPath xmlPath = new XmlPath(elementList);

        // then
        List<XmlPathElement> elements = xmlPath.getElements();
        assertThat(elements, is(not(nullValue())));
        assertThat(elements.size(), is(equalTo(3)));
        assertThat(xmlPath.toString(), is(equalTo("//response/result/doc")));
        assertThat(elements.get(0).getLocalName(), is(equalTo("response")));
        assertThat(elements.get(1).getLocalName(), is(equalTo("result")));
        assertThat(elements.get(2).getLocalName(), is(equalTo("doc")));
    }
}
