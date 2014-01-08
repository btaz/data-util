package com.btaz.util.files;

import com.btaz.util.DataUtilException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static com.btaz.util.unit.ResourceUtil.getTestResourceFile;
import static com.btaz.util.unit.ResourceUtil.readFromFileIntoString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileSplitterTest {
    private FileTracker tracker;

    @Before
    public void setUp() throws Exception {
        tracker = new FileTracker();
    }

    @After
    public void tearDown() throws Exception {
        tracker.deleteAll();
    }

    @Test(expected = DataUtilException.class)
    public void testOfFileSplitterWithMissingInputFileShouldThrowException() throws Exception {
        // given
        File inputFile = null;

        // when
        FileSplitter.split(null, inputFile, 20000, false);

        // then
    }

    @Test(expected = DataUtilException.class)
    public void testOfFileSplitterWithInvalidMaxBytesValueShouldThrowException() throws Exception {
        // given
        File inputFile = getTestResourceFile("random-1.txt");

        // when
        FileSplitter.split(null, inputFile, 0, false);

        // then
    }

    @Test(expected = DataUtilException.class)
    public void testOfFileSplitterWithNonExistingFileShouldThrowException() throws Exception {
        // given
        File inputFile = new File("weDoNotHaveThisFileOnOurDisk.txt");

        // when
        FileSplitter.split(null, inputFile, 0, false);

        // then
    }

    @Test
    public void testOfFileSplitterShouldYieldMultipleFiles() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File inputFile = tracker.getTestResource("random-1.txt");
        String original = readFromFileIntoString(inputFile);

        // when
        List<File> splitFiles = FileSplitter.split(testDir, inputFile, 20000, false);
        tracker.add(splitFiles);
        StringBuilder text = new StringBuilder();
        for(File splitFile : splitFiles) {
            text.append(readFromFileIntoString(splitFile));
        }

        // then
        assertThat(splitFiles, is(not(nullValue())));
        assertThat(splitFiles.size(), is(greaterThan(5)));
        assertThat(splitFiles.size(), is(lessThan(10)));
        assertThat(text.toString(), is(equalTo(original)));
    }

    @Test
    public void testOfFileSplitterWithSkipHeaderEnabledShouldRemoveHeaderRow() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File inputFile = getTestResourceFile("test-2.txt");
        boolean skipHeader = true;

        // when
        List<File> splitFiles = FileSplitter.split(testDir, inputFile, 20000, skipHeader);
        tracker.add(splitFiles);

        // then
        assertThat(splitFiles, is(not(nullValue())));
        assertThat(splitFiles.size(), is(equalTo(1)));
        String splitData = readFromFileIntoString(splitFiles.get(0));
        assertThat(splitData, not(containsString("ID\tItem\tDescription")));
        assertThat(splitData.split("\n").length, is(equalTo(9)));
    }

    @Test
    public void testOfFileSplitterWithSkipHeaderDisabledShouldKeepHeaderRow() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File inputFile = getTestResourceFile("test-2.txt");
        boolean skipHeader = false;

        // when
        List<File> splitFiles = FileSplitter.split(testDir, inputFile, 20000, skipHeader);
        tracker.add(splitFiles);

        // then
        assertThat(splitFiles, is(not(nullValue())));
        assertThat(splitFiles.size(), is(equalTo(1)));
        String splitData = readFromFileIntoString(splitFiles.get(0));
        assertThat(splitData, containsString("ID\tItem\tDescription"));
        assertThat(splitData.split("\n").length, is(equalTo(10)));
    }
}
