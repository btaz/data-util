package com.btaz.util.collections;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * User: msundell
 */
public class ListsTest {
    @Test
    public void testCreateListFromArgumentListShouldCreateList() throws Exception {
        // given

        // when
        List<String> result = Lists.createList("Hello", "Bob", "how", "are", "you?");

        // then
        assertThat(result, is(hasItems("Hello", "Bob", "how", "are", "you?")));
    }

    @Test
    public void testCreateListFromArrayListShouldCreateList() throws Exception {
        // given
        String[] items = {"Hello", "Bob", "how", "are", "you?"};

        // when
        List<String> result = Lists.arrayToList(items);

        // then
        assertThat(result, is(hasItems("Hello", "Bob", "how", "are", "you?")));
    }

    @Test
    public void testOfSubListCreationShouldCreateSubList() throws Exception {
        // given
        List<String> input = Lists.createList("Hello", "Bob", "how", "are", "you?");

        // when
        List<String> result = Lists.subList(input, new Criteria<String>() {
            @Override
            public boolean meetsCriteria(String item) {
                return item.contains("o");
            }
        });

        // then
        assertThat(result, is(not(nullValue())));
        assertThat(result, is(hasItems("Hello", "Bob", "how", "you?")));
    }

    @Test
    public void testOfSubListCreationWithNoInputShouldCreateEmptySubList() throws Exception {
        // given
        List<String> input = Collections.emptyList();

        // when
        List<String> result = Lists.subList(input, new Criteria<String>() {
            @Override
            public boolean meetsCriteria(String item) {
                return item.contains("o");
            }
        });

        // then
        assertThat(result, is(not(nullValue())));
        assertThat(result.size(), is(equalTo(0)));
    }
}
