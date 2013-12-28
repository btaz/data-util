package com.btaz.datautil.files;

import com.btaz.utils.ResourceUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.btaz.utils.ResourceUtil.readFromFileIntoString;
import static com.btaz.datautil.files.FileCat.concatenate;
import static com.btaz.utils.ResourceUtil.writeStringToTempFile;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileCatTest {
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
    public void testConcatenatingMultipleShouldYieldOneFile() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        String input1 = ResourceUtil.createRandomData(1000, 100, true);
        String input2 = ResourceUtil.createRandomData(1000, 100, true);
        String expected = input1 + input2;

        List<File> catFiles = new ArrayList<File>();
        catFiles.add(writeStringToTempFile("input1", ".txt", input1));
        catFiles.add(writeStringToTempFile("input2", ".txt", input2));
        File outputFile = tracker.createFile(testDir, "output.txt");

        // when
        concatenate(catFiles, outputFile);
        String concatenatedFileData = readFromFileIntoString(outputFile);

        // then
        assertThat(outputFile.exists(), is(true));
        assertThat(expected, is(equalTo(concatenatedFileData)));
    }
}
