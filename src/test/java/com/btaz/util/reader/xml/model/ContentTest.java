package com.btaz.util.reader.xml.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class ContentTest {
    @Test
    public void testOfContentWithIdenticalDataShouldEqualsTrue() throws Exception {
        // given
        Content content1 = new Content("Hello world!");
        Content content2 = new Content("Hello world!");

        // when
        int hashCode1 = content1.hashCode();
        int hashCode2 = content2.hashCode();
        boolean equalsResult = content1.equals(content2);

        // then
        assertThat(hashCode1, is(equalTo(hashCode2)));
        assertThat(equalsResult, is(true));
        assertThat(content1.getText(), is(equalTo(content2.getText())));
    }

    @Test
    public void testOfContentWithDifferentDataShouldEqualsFalse() throws Exception {
        // given
        Content content1 = new Content("Hello world!");
        Content content2 = new Content("Hello Bob!");

        // when
        int hashCode1 = content1.hashCode();
        int hashCode2 = content2.hashCode();
        boolean equalsResult = content1.equals(content2);

        // then
        assertThat(hashCode1, is(not(equalTo(hashCode2))));
        assertThat(equalsResult, is(false));
        assertThat(content1.getText(), is(not(equalTo(content2.getText()))));
    }

    @Test
    public void testOfSettingParentShouldYieldACorrectContentObject() throws Exception {
        // given
        Element element = new Element("mr-boss");
        Content content = new Content("Hello Bob!");

        // when
        content.setParent(element);

        // then
        assertThat(content.getParent(), is(equalTo(element)));
    }

    @Test
    public void testWithContentHavingTabNewlineMultiSpaceCharactersAndExportingToFlatShouldExportToFlatFormat() throws Exception {
        // given
        String input = "fruits: orange\tapple\tpear\ngrapes\tblueberry\nDescription: very    juicy";
        String expected = "fruits: orange apple pear grapes blueberry Description: very juicy";

        // when
        Content content = new Content(input);
        String output = content.toString(false, true);

        // then
        assertThat(output, is(not(nullValue())));
        assertThat(output, is(equalTo(expected)));
    }
}
