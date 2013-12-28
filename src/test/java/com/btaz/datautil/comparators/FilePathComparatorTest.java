package com.btaz.datautil.comparators;

import com.btaz.datautil.DataUtilException;
import com.btaz.datautil.files.comparator.FilePathComparator;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * These FilePathComparator tests are used to determine what path comes first. Since this comparator is used to perform
 * deletions we want the longer and deeper path to be sorted first, like this:
 * /a/b/c/d/e
 * /a/b/c/d
 * /a/b/a/b/e      (this is not a mistake, in the third field c comes before a in reverse sorting)
 * /a/b
 * /a
 *
 * User: msundell
 */
public class FilePathComparatorTest {
    @Test
    public void testOfFilePathComparatorWithTheSamePathShouldReturnZero() throws Exception {
        // given
        File f1 = new File("/a/b/c/d");
        File f2 = new File("/a/b/c/d");

        // when
        int result = new FilePathComparator().compare(f1, f2);

        // then
        assertThat(result, is(equalTo(0)));
    }

    @Test
    public void testOfFilePathComparatorWithLongerLengthPathShouldReturnBeforePath() throws Exception {
        // given
        File f1 = new File("/a/b/c/d");
        File f2 = new File("/a/b/c");

        // when
        int result = new FilePathComparator().compare(f1, f2);

        // then
        assertThat(result, is(lessThan(0)));
    }

    @Test
    public void testOfFilePathComparatorWithDifferentLengthPathShouldReturnBeforePath() throws Exception {
        // given
        File f1 = new File("/a/b/c/e/f/g");
        File f2 = new File("/a/b/c/d/e");

        // when
        int result = new FilePathComparator().compare(f1, f2);

        // then
        assertThat(result, is(lessThan(0)));
    }

    @Test
    public void testOfFilePathComparatorWithSameLengthPathShouldReturnBeforePath() throws Exception {
        // given
        File f1 = new File("/a/b/c/a");
        File f2 = new File("/a/b/c/d");

        // when
        int result = new FilePathComparator().compare(f1, f2);

        // then
        assertThat(result, is(greaterThan(0)));
    }

    @Test
    public void testOfFilePathComparatorWithLongerLengthPathShouldReturnAfterPath() throws Exception {
        // given
        File f1 = new File("/a/b/c");
        File f2 = new File("/a/b/c/d");

        // when
        int result = new FilePathComparator().compare(f1, f2);

        // then
        assertThat(result, is(greaterThan(0)));
    }

    @Test(expected = DataUtilException.class)
    public void testOfFilePathComparatorWithFirstFileNullValueShouldThrowException() throws Exception {
        // given
        File f1 = null;
        File f2 = new File("/a/b/c/d");

        // when
        new FilePathComparator().compare(f1, f2);

        // then
    }

    @Test(expected = DataUtilException.class)
    public void testOfFilePathComparatorWithSecondFileNullValueShouldThrowException() throws Exception {
        // given
        File f1 = new File("/a/b/c/d");
        File f2 = null;

        // when
        new FilePathComparator().compare(f1, f2);

        // then
    }
}
