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
        String result = Strings.asCommaSeparatedValues(inputList);

        // then
        assertThat(result, is(equalTo("Old,MacDonald,had,a,farm")));
    }

    @Test
    public void testOfCommaSeparatedStringBuilderWithArgsShouldCreateCommaSeparatedList() throws Exception {
        // given

        // when
        String result = Strings.asCommaSeparatedValues("Old", "MacDonald", "had", "a", "farm");

        // then
        assertThat(result, is(equalTo("Old,MacDonald,had,a,farm")));
    }

    @Test
    public void testOfTabSeparatedStringBuilderShouldCreateTabSeparatedList() throws Exception {
        // given
        List<String> inputList = Strings.buildList("Hello", "Bob", "Monster");

        // when
        String result = Strings.asTabSeparatedValues(inputList);

        // then
        assertThat(result, is(equalTo("Hello\tBob\tMonster")));
    }

    @Test
    public void testOfTabSeparatedStringBuilderWithArgsShouldCreateTabSeparatedList() throws Exception {
        // given

        // when
        String result = Strings.asTabSeparatedValues("Hello", "Bob", "Monster");

        // then
        assertThat(result, is(equalTo("Hello\tBob\tMonster")));
    }

    @Test
    public void testOfTokenSeparatedStringBuilderShouldCreateTokenSeparatedList() throws Exception {
        // given
        List<String> inputList = Strings.buildList("Fruits", "Monkeys", "Pirates");

        // when
        String result = Strings.asTokenSeparatedValues("||", inputList);

        // then
        assertThat(result, is(equalTo("Fruits||Monkeys||Pirates")));
    }

    @Test
    public void testOfTokenSeparatedStringBuilderWithArgsShouldCreateTokenSeparatedList() throws Exception {
        // given

        // when
        String result = Strings.asTokenSeparatedValues("||", "Fruits", "Monkeys", "Pirates");

        // then
        assertThat(result, is(equalTo("Fruits||Monkeys||Pirates")));
    }

    @Test
    public void testOfTokenSeparatedStringBuilderWithNoArgsShouldCreateEmptyString() throws Exception {
        // given

        // when
        String result = Strings.asTokenSeparatedValues("||");

        // then
        assertThat(result, is(equalTo("")));
    }

    @Test
    public void testOfCommaSeparatedStringBuilderWithEmptyListShouldCreateEmptyString() throws Exception {
        // given
        List<String> inputList = Collections.emptyList();

        // when
        String result = Strings.asCommaSeparatedValues(inputList);

        // then
        assertThat(result, is(equalTo("")));
    }
}
