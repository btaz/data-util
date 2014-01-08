package com.btaz.util.tf;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 * Date: 1/8/14
 * Time: 8:02 AM
 */
public class HtmlEscapeTest {
    @Test
    public void testOfUnescapedHtmlShouldYieldProperlyEscapedHtml() throws Exception {
        // given
        String input = "Hello <World> & \"People\" & /all/";
        String expected = "Hello &lt;World&gt; &amp; &quot;People&quot; &amp; &#x2F;all&#x2F;";

        // when
        String output = HtmlEscape.escape(input);

        // then
        assertThat(output, is(not(nullValue())));
        assertThat(output, is(equalTo(expected)));
    }
}
