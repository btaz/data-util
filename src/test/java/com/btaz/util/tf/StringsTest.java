package com.btaz.util.tf;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * User: msundell
 */
public class StringsTest {
    @Test
    public void testOfBuildListFromArgsListShouldBuildListOfStrings() throws Exception {
        // given
        List<String> expected = new ArrayList<String>();
        expected.add("The");
        expected.add("dog");
        expected.add("bit");
        expected.add("the");
        expected.add("boy");

        // when
        List<String> strings = Strings.buildList("The", "dog", "bit", "the", "boy");

        // then
        Assert.assertThat(strings, is(equalTo(expected)));
    }

    @Test
    public void testOfBuildListFromEmptyArgsListShouldBuildEmptyListOfStrings() throws Exception {
        // given

        // when
        List<String> strings = Strings.buildList();

        // then
        Assert.assertThat(strings.isEmpty(), is(true));
    }

    @Test
    public void testOfCommaSeparatedStringBuilderShouldCreateCommaSeparatedList() throws Exception {
        // given
        List<String> inputList = Strings.buildList("Old", "MacDonald", "had", "a", "farm");

        // when
        String result = Strings.asCommaSeparatedList(inputList);

        // then
        assertThat(result, is(equalTo("Old,MacDonald,had,a,farm")));
    }

    @Test
    public void testOfCommaSeparatedStringBuilderWithEmptyListShouldCreateEmptyString() throws Exception {
        // given
        List<String> inputList = Collections.emptyList();

        // when
        String result = Strings.asCommaSeparatedList(inputList);

        // then
        assertThat(result, is(equalTo("")));
    }
}
