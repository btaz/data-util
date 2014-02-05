package com.btaz.util.files;

import com.btaz.util.comparator.Lexical;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.btaz.util.unit.ResourceUtil.*;
import static com.btaz.util.files.SortController.sortFile;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class SortControllerTest {
    private FileTracker tracker;

    @Before
    public void setUp() throws Exception {
        tracker = new FileTracker();
    }

    @After
    public void tearDown() throws Exception {
        tracker.deleteAll();
    }

    @Test
    public void sortingEmptyFileShouldYieldAnEmptyFile() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File inputFile = tracker.getTestResource("empty.txt");
        File expectedFile = tracker.getTestResource("empty.txt");
        String expectedData = readFromFileIntoString(expectedFile);
        File outputFile = tracker.createFile(testDir, "output.txt");

        // when
        sortFile(testDir, inputFile, outputFile, Lexical.ascending(), false);
        File [] allTestFiles = testDir.listFiles();
        tracker.add(allTestFiles);
        String outputData = readFromFileIntoString(outputFile);

        // then
        assertThat(outputFile.exists(), is(true));
        assertThat(outputData, is(equalTo(expectedData)));
    }

    @Test
    public void sortingShuffledFileShouldMatchPreSortedExpectedFile() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File inputFile = tracker.getTestResource("random-1.txt");
        File expectedFile = tracker.getTestResource("sorted-1.txt");
        String expectedData = readFromFileIntoString(expectedFile);
        File outputFile = tracker.createFile(testDir, "output.txt");

        // when
        sortFile(testDir, inputFile, outputFile, Lexical.ascending(), false);
        File [] allTestFiles = testDir.listFiles();
        tracker.add(allTestFiles);
        String outputData = readFromFileIntoString(outputFile);

        // then
        assertThat(outputFile.exists(), is(true));
        assertThat(outputData, is(equalTo(expectedData)));
    }
}
