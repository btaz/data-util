package com.btaz.util.string;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringUtilTest {
    @Test
    public void testOfStringPadShouldYieldAPaddedString() throws Exception {
        // given
        String padCharacter = " ";
        int repeat = 5;

        // when
        String result = StringUtil.pad(repeat, padCharacter);

        // then
        assertThat(result, is(not(nullValue())));
        assertThat(result, is(equalTo("     ")));
    }

    @Test
    public void testOfStringPadWithBlankInputShouldYieldABlankString() throws Exception {
        // given
        String padCharacter = "";
        int repeat = 20;

        // when
        String result = StringUtil.pad(repeat, padCharacter);

        // then
        assertThat(result, is(not(nullValue())));
        assertThat(result, is(equalTo("")));
    }

    @Test
    public void testOfStringPadWithZeroWidthShouldYieldABlankString() throws Exception {
        // given
        String padCharacter = "-";
        int repeat = 0;

        // when
        String result = StringUtil.pad(repeat, padCharacter);

        // then
        assertThat(result, is(not(nullValue())));
        assertThat(result, is(equalTo("")));
    }

    @Test
    public void testOfStringPadWithANegativeWidthShouldYieldABlankString() throws Exception {
        // given
        String padCharacter = "-";
        int repeat = -5;

        // when
        String result = StringUtil.pad(repeat, padCharacter);

        // then
        assertThat(result, is(not(nullValue())));
        assertThat(result, is(equalTo("")));
    }
}