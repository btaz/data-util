package com.btaz.datautil.files;

import com.btaz.datautil.comparators.Lexical;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.btaz.utils.ResourceUtil.*;
import static com.btaz.datautil.files.FileMerger.merge;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileMergerTest {
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
    public void testOfMergeFilesShouldYieldASingleFile() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        List<File> sortedFiles = new ArrayList<File>();
        sortedFiles.add(tracker.getTestResource("sorted-1a.txt"));
        sortedFiles.add(tracker.getTestResource("sorted-1b.txt"));
        sortedFiles.add(tracker.getTestResource("sorted-1c.txt"));
        sortedFiles.add(tracker.getTestResource("sorted-1d.txt"));

        String sortedText = readTestResourceIntoString("sorted-1.txt");

        File mergedFile = tracker.createFile(testDir, "merged-file.txt");

        // when
        merge(testDir, sortedFiles, mergedFile, Lexical.ascending(), 4);
        String mergedText = readFromFileIntoString(mergedFile);

        // then
        assertThat(mergedFile.exists(), is(true));
        assertThat(mergedText, is(equalTo(sortedText)));
    }
}
