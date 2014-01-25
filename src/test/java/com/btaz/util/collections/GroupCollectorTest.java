package com.btaz.util.collections;

import com.btaz.util.tf.Strings;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * User: msundell
 */
public class GroupCollectorTest {
    @Test
    public void testOfGroupCollectorShouldGroupItemsCorrectly() throws Exception {
        // given
        List<String> strings = Lists.createList("apple", "orange", "banana", "apple", "banana", "apple", "kiwi");

        // when
        GroupCollector<List<String>,String> groupCollector = new GroupCollector<List<String>,String>(strings, new MyComparator());
        ArrayList<String> results = new ArrayList<String>();
        for(List<String> list : groupCollector) {
            results.add(Strings.asCommaSeparatedValues(list));
        }

        // then
        assertThat(results.isEmpty(), is(false));
        assertThat(results.size(), is(equalTo(4)));
        assertThat(results.get(0), is(equalTo("apple,apple,apple")));
        assertThat(results.get(1), is(equalTo("banana,banana")));
        assertThat(results.get(2), is(equalTo("kiwi")));
        assertThat(results.get(3), is(equalTo("orange")));
    }

    @Test
    public void testOfGroupCollectorWithEmptyInputListShouldReturnNull() throws Exception {
        // given
        List<String> strings = Collections.emptyList();

        // when
        GroupCollector<List<String>,String> groupCollector = new GroupCollector<List<String>,String>(strings, new MyComparator());
        Iterator<List<String>> it = groupCollector.iterator();
        boolean hasNext = it.hasNext();
        List<String> result = it.next();

        // then
        assertThat(hasNext, is(false));
        assertThat(result, is(nullValue()));
    }

    public static class MyComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }
}
