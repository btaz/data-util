package com.btaz.util.xml.diff;

import com.btaz.util.xml.model.Content;
import com.btaz.util.xml.model.Xml;
import com.btaz.util.xml.model.Element;
import org.junit.Test;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class DifferenceTest {
    @Test
    public void testOfCreatingANewDifferenceShouldYieldACorrectMessage() throws Exception {
        // given
        Content contentA = new Content("Juicy");
        Xml docA = new Xml()
                .addElement("<fruits>")
                .addElement("<orange>")
                .addContent(contentA);

        Content contentB = new Content("Rotten");
        Xml docB = new Xml()
                .addElement("<fruits>")
                .addElement("<orange>")
                .addContent(contentB);

        String reason = "Different character data";

        // when
        Difference difference = new Difference(contentA, contentB, reason);

        // then
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getReason(), is(equalTo(reason)));
        assertThat(difference.getPathA(), is(equalTo("<fruits><orange>Juicy")));
        assertThat(difference.getPathB(), is(equalTo("<fruits><orange>Rotten")));
        assertThat(difference.toString(), is(equalTo("Different character data\n - <fruits><orange>Juicy\n - <fruits><orange>Rotten\n")));
    }

    @Test
    public void testOfCreatingANewDifferenceShouldYieldAValidMessage() throws Exception {
        // given
        Element a = new Element("orange");
        Xml docA = new Xml()
                .addElement("<fruits>")
                .addElement(a);

        Element b = new Element("banana");
        Xml docB = new Xml()
                .addElement("<fruits>")
                .addElement(b);

        String reason = "Different elements";

        // when
        Difference difference = new Difference(a, b, reason);

        // then
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getReason(), is(equalTo(reason)));
        assertThat(difference.getPathA(), is(equalTo("<fruits><orange>")));
        assertThat(difference.getPathB(), is(equalTo("<fruits><banana>")));
        assertThat(difference.toString(), is(equalTo("Different elements\n - <fruits><orange>\n - <fruits><banana>\n")));
    }
}
