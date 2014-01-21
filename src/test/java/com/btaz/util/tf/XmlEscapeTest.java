package com.btaz.util.tf;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * User: msundell
 */
public class XmlEscapeTest {
    @Test
    public void testWithTextContentShouldBeEscaped() throws Exception {
        // given
        String input = "abc&123\"def'456<ghi>789";

        // when
        String output = XmlEscape.escape(input);

        // then
        assertThat(output, is(equalTo("abc&amp;123&quot;def&apos;456&lt;ghi&gt;789")));
    }
}
