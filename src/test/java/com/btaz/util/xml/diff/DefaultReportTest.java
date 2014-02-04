package com.btaz.util.xml.diff;

import com.btaz.util.collections.Lists;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class DefaultReportTest {
    @Test
    public void testOfDefaultReportWithTwoDifferencesShouldFilterOutTheIgnoreItemInA() throws Exception {
        // given
        List<String> ignoreList = Lists.createList("<doc><date name=\"index_timestamp\">");
        DefaultReport report = new DefaultReport("A", "B", ignoreList);
        report.add(new Difference("<doc><date name=\"index_timestamp\">", "<doc><int name=\"languages\">", "Both are different"));

        // when
        boolean hasDifferences = report.hasDifferences();
        Iterator<Difference> it = report.getAllDifferences();

        // then
        assertThat(hasDifferences, is(true));
        Difference difference = it.next();
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getReason(), is(not(equalTo("Both are different"))));
        assertThat(difference.getReason(), containsString("Only in: B"));
        assertThat(difference.getPathA(), is(equalTo("")));
        assertThat(difference.getPathB(), is(equalTo("<doc><int name=\"languages\">")));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testOfDefaultReportWithTwoDifferencesShouldFilterOutTheIgnoreItemInB() throws Exception {
        // given
        List<String> ignoreList = Lists.createList("<doc><date name=\"index_timestamp\">");
        DefaultReport report = new DefaultReport("A", "B", ignoreList);
        report.add(new Difference("<doc><int name=\"languages\">", "<doc><date name=\"index_timestamp\">", "Both are different"));

        // when
        boolean hasDifferences = report.hasDifferences();
        Iterator<Difference> it = report.getAllDifferences();

        // then
        assertThat(hasDifferences, is(true));
        Difference difference = it.next();
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.getReason(), is(not(equalTo("Both are different"))));
        assertThat(difference.getReason(), containsString("Only in: A"));
        assertThat(difference.getPathA(), is(equalTo("<doc><int name=\"languages\">")));
        assertThat(difference.getPathB(), is(equalTo("")));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testOfDefaultReportWithOneDifferenceShouldFilterOutTheDifference() throws Exception {
        // given
        List<String> ignoreList = Lists.createList("<doc><date name=\"index_timestamp\">");
        DefaultReport report = new DefaultReport("A", "B", ignoreList);
        report.add(new Difference("", "<doc><date name=\"index_timestamp\">", "Only in: B"));

        // when
        boolean hasDifferences = report.hasDifferences();

        // then
        assertThat(hasDifferences, is(false));
    }
}
