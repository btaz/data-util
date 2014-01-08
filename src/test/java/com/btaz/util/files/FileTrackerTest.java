package com.btaz.util.files;

import com.btaz.util.files.FileTracker;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileTrackerTest {
    @Test
    public void testOfFileTrackerWithTrackedFilesShouldTrackCount() throws Exception {
        // given
        File testDir = new File("target/test-dir");
        FileTracker tracker = new FileTracker();

        // when
        tracker.add(new File("test-1.txt"));
        tracker.add(new File("test-2.txt"));
        tracker.add(new File("test-3.txt"));
        tracker.add(new File("test-4.txt"));

        // then
        assertThat(tracker.size(), is(equalTo(4)));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testOfFileTrackerWithTrackedFilesShouldProperlyTrackFiles() throws Exception {
        // given
        FileTracker tracker = new FileTracker();

        File testDir = new File("target/test-dir");
        File info = new File(testDir, "info.txt");
        File fruits = new File(testDir, "fruits");
        File apple = new File(new File(testDir, "fruits"), "apple.txt");
        File pear = new File(new File(testDir, "fruits"), "pear.txt");

        testDir.mkdir();
        info.createNewFile();
        fruits.mkdir();
        apple.createNewFile();
        pear.createNewFile();

        tracker.add(testDir);
        tracker.add(info);
        tracker.add(fruits);
        tracker.add(apple);
        tracker.add(pear);

        // when
        tracker.deleteAll();

        // then
        assertThat(tracker.size(), is(equalTo(0)));
        assertThat(pear.exists(), is(false));
        assertThat(apple.exists(), is(false));
        assertThat(fruits.exists(), is(false));
        assertThat(info.exists(), is(false));
    }
}
