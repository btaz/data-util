package com.btaz.util.xml.diff;

import com.btaz.util.xml.model.Content;
import com.btaz.util.xml.model.Xml;
import com.btaz.util.xml.model.Element;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class DefaultArbitratorTest {
    private Arbitrator arbitrator;

    @Before
    public void setUp() throws Exception {
        arbitrator = new DefaultArbitrator();
    }

    @Test
    public void testWithIdenticalContentShouldBeTrue() throws Exception {
        // given
        Content a = new Content("Juicy");
        Content b = new Content("Juicy");

        // when
        Difference difference = arbitrator.compare(a, b);

        // then
        assertThat(difference, is(nullValue()));
    }

    @Test
    public void testWithDifferentContentShouldBeFalse() throws Exception {
        // given
        Content a = new Content("Juicy");
        Xml da = new Xml()
                .addElement("<orange>")
                .addContent(a);

        Content b = new Content("Rotten");
        Xml db = new Xml()
                .addElement("<orange>")
                .addContent(b);

        // when
        Difference difference = arbitrator.compare(a, b);

        // then
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getPathA(), is(equalTo("<orange>Juicy")));
        assertThat(difference.getPathB(), is(equalTo("<orange>Rotten")));
        assertThat(difference.getReason(), is(equalTo("Content is different")));
    }

    @Test
    public void testWithDifferentTypesShouldBeFalse() throws Exception {
        // given
        Content a = new Content("Juicy");
        Xml da = new Xml()
                .addElement("<orange>")
                .addContent(a);

        Element b = new Element("navel");
        Xml db = new Xml()
                .addElement("<orange>")
                .addElement(b);

        // when
        Difference difference = arbitrator.compare(a, b);

        // then
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getPathA(), is(equalTo("<orange>Juicy")));
        assertThat(difference.getPathB(), is(equalTo("<orange><navel>")));
        assertThat(difference.getReason(), is(equalTo("The nodes are different")));
    }

    @Test
    public void testWithDifferentElementAttributesShouldBeFalse() throws Exception {
        // given
        Xml a = new Xml()
                .addElement("<fruits>")
                .addElement("<pear>");

        Xml b = new Xml()
                .addElement("<fruits>")
                .addElement("<pear type=\"winter\">");

        Element ea = (Element) a.pathQuery("/fruits/pear").get(0);
        Element eb = (Element) b.pathQuery("/fruits/pear").get(0);

        // when
        Difference difference = arbitrator.compare(ea, eb);

        // then
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getPathA(), is(equalTo("<fruits><pear>")));
        assertThat(difference.getPathB(), is(equalTo("<fruits><pear type=\"winter\">")));
        assertThat(difference.getReason(), is(equalTo("Element attribute names are different")));
    }

    @Test
    public void testWithDifferentElementAttributes2ShouldBeFalse() throws Exception {
        // given
        Xml a = new Xml()
                .addElement("<fruits>")
                .addElement("<pear color=\"yellow\">");

        Xml b = new Xml()
                .addElement("<fruits>")
                .addElement("<pear type=\"winter\">");

        Element ea = (Element) a.pathQuery("/fruits/pear").get(0);
        Element eb = (Element) b.pathQuery("/fruits/pear").get(0);

        // when
        Difference difference = arbitrator.compare(ea, eb);

        // then
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getPathA(), is(equalTo("<fruits><pear color=\"yellow\">")));
        assertThat(difference.getPathB(), is(equalTo("<fruits><pear type=\"winter\">")));
        assertThat(difference.getReason(), is(equalTo("Element attribute names are different")));
    }

    @Test
    public void testWithDifferentElementAttributeValuesShouldBeFalse() throws Exception {
        // given
        Xml a = new Xml()
                .addElement("<fruits>")
                .addElement("<pear color=\"yellow\">");

        Xml b = new Xml()
                .addElement("<fruits>")
                .addElement("<pear color=\"green\">");

        Element ea = (Element) a.pathQuery("/fruits/pear").get(0);
        Element eb = (Element) b.pathQuery("/fruits/pear").get(0);

        // when
        Difference difference = arbitrator.compare(ea, eb);

        // then
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getPathA(), is(equalTo("<fruits><pear color=\"yellow\">")));
        assertThat(difference.getPathB(), is(equalTo("<fruits><pear color=\"green\">")));
        assertThat(difference.getReason(), is(equalTo("Element attribute values are different")));
    }

    @Test
    public void testWithIdenticalElementsWithDifferentChildElementCountsShouldBeFalse() throws Exception {
        // given
        Xml a = new Xml()
                .addElement("<fruits>")
                .addElement("<pear />")
                .addElement("<orange />");

        Xml b = new Xml()
                .addElement("<fruits>")
                .addElement("<pear />");

        Element ea = (Element) a.pathQuery("/fruits").get(0);
        Element eb = (Element) b.pathQuery("/fruits").get(0);

        // when
        Difference difference = arbitrator.compare(ea, eb);

        // then
        assertThat(difference, is(nullValue()));
    }
}
