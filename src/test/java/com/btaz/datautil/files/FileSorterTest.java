package com.btaz.datautil.files;

import com.btaz.datautil.comparators.Lexical;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.btaz.utils.ResourceUtil.readFromFileIntoString;
import static com.btaz.datautil.files.FileSorter.sort;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileSorterTest {
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
    public void testOfSortingAFileShouldYieldASortedFile() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        List<File> files = new ArrayList<File>();
        File input = tracker.getTestResource("random-1.txt");
        files.add(input);
        String expectedText = readFromFileIntoString(tracker.getTestResource("sorted-1.txt"));

        // when
        List<File> sortedFiles = sort(testDir, files, Lexical.ascending());
        tracker.add(sortedFiles);

        // then
        assertThat(sortedFiles, is(not(nullValue())));
        assertThat(sortedFiles.size(), is(equalTo(1)));
        String sortedText = readFromFileIntoString(sortedFiles.get(0));
        assertThat(sortedText, is(equalTo(expectedText)));
    }
}
