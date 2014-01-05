package com.btaz.datautil.xml.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class ElementTest {
    @Test
    public void testOfEqualsAndHashCodeOnElementsShouldEvaluateToTrue() throws Exception {
        // given
        Element elem1 = new Element("fruit", true);
        elem1.addAttribute("name", "banana");
        elem1.addAttribute("count", "5");

        Element elem2 = new Element("fruit", true);
        elem2.addAttribute("name", "banana");
        elem2.addAttribute("count", "5");

        // when
        int hashCodeResult1 = elem1.hashCode();
        int hashCodeResult2 = elem2.hashCode();
        boolean equalsResult = elem1.equals(elem2);

        // then
        assertThat(hashCodeResult1, is(equalTo(hashCodeResult2)));
        assertThat(equalsResult, is(true));
    }

    @Test
    public void testOfEqualsAndHashCodeOnElementsWithDifferentEmptyElementTagShouldEvaluateToFalse() throws Exception {
        // given
        Element elem1 = new Element("fruit", true);
        elem1.addAttribute("name", "banana");
        elem1.addAttribute("count", "5");

        Element elem2 = new Element("fruit", false);
        elem2.addAttribute("name", "banana");
        elem2.addAttribute("count", "5");

        // when
        int hashCodeResult1 = elem1.hashCode();
        int hashCodeResult2 = elem2.hashCode();
        boolean equalsResult = elem1.equals(elem2);

        // then
        assertThat(hashCodeResult1, is(not(equalTo(hashCodeResult2))));
        assertThat(equalsResult, is(false));
    }

    @Test
    public void testOfEqualsOnElementsWithNullValueShouldEvaluateToFalse() throws Exception {
        // given
        Element elem1 = new Element("fruit", true);
        elem1.addAttribute("name", "banana");
        elem1.addAttribute("count", "5");

        // when
        @SuppressWarnings("ObjectEqualsNull")
        boolean equalsResult = elem1.equals(null);

        // then
        assertThat(equalsResult, is(false));
    }

    @Test
    public void testOfSettingParentElementShouldYieldACorrectElementObject() throws Exception {
        // given
        Element dad = new Element("Frank Sr");
        Element son = new Element("Frank Jr");

        // when
        son.setParent(dad);

        // then
        assertThat(son.getParent(), is(equalTo(dad)));
    }
}
