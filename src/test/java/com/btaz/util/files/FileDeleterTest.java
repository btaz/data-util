package com.btaz.util.files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileDeleterTest {
    private FileTracker fileTracker;

    @Before
    public void setUp() throws Exception {
        fileTracker = new FileTracker();
    }

    @After
    public void tearDown() throws Exception {
        fileTracker.deleteAll();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testOfDeletingFilesByExtensionShouldOnlyDeleteTheMatchingFiles() throws Exception {
        // given
        File testDir = fileTracker.createDir(new File("target/test-dir"));
        File file1 = fileTracker.createFile(new File(testDir, "test-1.part"));
        File file2 = fileTracker.createFile(new File(testDir, "test-2.other"));

        // when
        FileDeleter.deleteFilesByExtension(testDir, ".part");

        // then
        assertThat(file1, is(not(nullValue())));
        assertThat(file1.exists(), is(false));
        assertThat(file2, is(not(nullValue())));
        assertThat(file2.exists(), is(true));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testOfDeletingFilesByRegexShouldOnlyDeleteTheMatchingFiles() throws Exception {
        // given
        File testDir = fileTracker.createDir(new File("target/test-dir"));
        File file1 = fileTracker.createFile(new File(testDir, "test-1.other"));
        File file2 = fileTracker.createFile(new File(testDir, "test-2.other"));
        File file3 = fileTracker.createFile(new File(testDir, "test-3.other"));

        // when
        FileDeleter.deleteFilesByRegex(testDir, "test-(2|3)\\.other");

        // then
        assertThat(file1, is(not(nullValue())));
        assertThat(file1.exists(), is(true));
        assertThat(file2, is(not(nullValue())));
        assertThat(file2.exists(), is(false));
        assertThat(file3, is(not(nullValue())));
        assertThat(file3.exists(), is(false));
    }
}
