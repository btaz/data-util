package com.btaz.util.tf;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class TemplateTest {
    @Test
    public void testOfTemplateSystemWithNullInputShouldReturnNull() throws Exception {
        // given
        String input = null;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", "Bob");
        map.put("fruit.a", "apples");
        map.put("fruit.b", "oranges");

        // when
        String output = Template.transform(input, map);

        // then
        assertThat(output, is(nullValue()));
    }

    @Test
    public void testOfTemplateSystemWithNullMapShouldDoNothing() throws Exception {
        // given
        String input = "Hello ${name}, as you may know ${name} there are some excellent fruits: ${fruit.a} and ${fruit.b}";

        Map<String,Object> map = null;

        // when
        String output = Template.transform(input, map);

        // then
        assertThat(output, is(not(nullValue())));
        assertThat(output, is(equalTo(input)));
    }

    @Test
    public void testOfTemplateSystemShouldReplaceAllTagsWithValues() throws Exception {
        // given
        String input = "Hello ${name}, as you may know ${name} there are some excellent fruits: ${fruit.a} and ${fruit.b}";
        String expected = "Hello Bob, as you may know Bob there are some excellent fruits: apples and oranges";

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", "Bob");
        map.put("fruit.a", "apples");
        map.put("fruit.b", "oranges");

        // when
        String output = Template.transform(input, map);

        // then
        assertThat(output, is(not(nullValue())));
        assertThat(output, is(equalTo(expected)));
    }
}
