package com.btaz.util.writer;

import com.btaz.util.files.FileTracker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.btaz.util.unit.ResourceUtil.readFromFileIntoString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class HtmlTableWriterTest {
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
    public void testOfHtmlTableWriterShouldCreateANewTableFile() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File outputFile = tracker.createFile(testDir, "tabular-data.html");

        // when
        HtmlTableWriter writer = new HtmlTableWriter(outputFile, "Sample Data", 2);
        writer.write("Fruit", "Color");
        writer.write("Banana", "Yellow");
        writer.write("Pear", "Green");
        writer.write("Orange", "Orange");
        writer.write("Apple", "Green");
        writer.close();

        String html = readFromFileIntoString(outputFile);

        // then
        assertThat(true, is(true));
        assertThat(html, is(not(nullValue())));
        assertThat(html, containsString("Sample Data"));
        assertThat(html, containsString("<table>"));
        assertThat(html, containsString("<tr><td>Fruit</td><td>Color</td></tr>"));
        assertThat(html, containsString("<tr><td>Banana</td><td>Yellow</td></tr>"));
        assertThat(html, containsString("<tr><td>Pear</td><td>Green</td></tr>"));
        assertThat(html, containsString("<tr><td>Orange</td><td>Orange</td></tr>"));
        assertThat(html, containsString("<tr><td>Apple</td><td>Green</td></tr>"));
    }
}
