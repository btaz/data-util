package com.btaz.util.writer;

import com.btaz.util.files.FileDeleter;
import com.btaz.util.files.FileTracker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.btaz.util.unit.ResourceUtil.readFromFileIntoString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class HtmlTableWriterTest {
    private FileTracker tracker;
    private File testDir;

    @Before
    public void setUp() throws Exception {
        tracker = new FileTracker();
    }

    @After
    public void tearDown() throws Exception {
        FileDeleter.deleteFilesByExtension(testDir, ".html");
        tracker.deleteAll();
    }

    @Test
    public void testOfHtmlTableWriterShouldCreateANewTableFile() throws Exception {
        // given
        testDir = tracker.createDir(new File("target/test-dir"));

        File outputFile = new File(testDir, "tabular-data.html");

        // when
        HtmlTableWriter writer = new HtmlTableWriter(testDir, "tabular-data", "Sample Data", "Fruits",
                "Are good for you...", 2);
        writer.write("Fruit", "Color");
        writer.write("Banana", "Yellow");
        writer.write("Pear", "Green");
        writer.write("Orange", "Orange");
        writer.write("Apple", "Green");
        writer.close();

        // then
        assertThat(outputFile.exists(), is(true));
        String html = readFromFileIntoString(outputFile);
        assertThat(html, is(not(nullValue())));
        assertThat(html, containsString("<title>Sample Data</title>"));
        assertThat(html, containsString("<h1>Fruits</h1>"));
        assertThat(html, containsString("Are good for you..."));
        assertThat(html, containsString("<table>"));
        assertThat(html, containsString("<tr><td>Fruit</td><td>Color</td></tr>"));
        assertThat(html, containsString("<tr><td>Banana</td><td>Yellow</td></tr>"));
        assertThat(html, containsString("<tr><td>Pear</td><td>Green</td></tr>"));
        assertThat(html, containsString("<tr><td>Orange</td><td>Orange</td></tr>"));
        assertThat(html, containsString("<tr><td>Apple</td><td>Green</td></tr>"));
    }

    @Test
    public void testOfHtmlTableWriterAndPaginationShouldCreateTwoNewTableFiles() throws Exception {
        // given
        testDir = tracker.createDir(new File("target/test-dir"));

        File outputFile = new File(testDir, "paginated-data.html");
        File outputFile2 = new File(testDir, "paginated-data-2.html");
        File outputFile3 = new File(testDir, "paginated-data-3.html");

        // when
        HtmlTableWriter writer = new HtmlTableWriter(testDir, "paginated-data", "Sample Data", "Fruits",
                "Are good for you...", 2, 3);
        writer.write("Fruit", "Color");
        writer.write("Banana", "Yellow");
        writer.write("Pear", "Green");
        writer.write("Orange", "Orange");
        writer.write("Apple", "Green");
        writer.write("Grape", "Purple");
        writer.write("Cherry", "Red");
        writer.write("Water Melon", "Green");
        writer.close();

        // then

        // - page 1
        assertThat(outputFile.exists(), is(true));
        String html = readFromFileIntoString(outputFile);
        assertThat(html, is(not(nullValue())));
        assertThat(html, containsString("<title>Sample Data</title>"));
        assertThat(html, containsString("<h1>Fruits</h1>"));
        assertThat(html, containsString("Are good for you..."));
        assertThat(html, containsString("<table>"));
        assertThat(html, containsString("<tr><td>Fruit</td><td>Color</td></tr>"));
        assertThat(html, containsString("<tr><td>Banana</td><td>Yellow</td></tr>"));
        assertThat(html, containsString("<tr><td>Pear</td><td>Green</td></tr>"));

        // - page 2
        assertThat(outputFile2.exists(), is(true));
        String html2 = readFromFileIntoString(outputFile2);
        assertThat(html2, is(not(nullValue())));
        assertThat(html2, containsString("<title>Sample Data</title>"));
        assertThat(html2, containsString("<h1>Fruits</h1>"));
        assertThat(html2, containsString("Are good for you..."));
        assertThat(html2, containsString("<table>"));
        assertThat(html2, containsString("<tr><td>Orange</td><td>Orange</td></tr>"));
        assertThat(html2, containsString("<tr><td>Apple</td><td>Green</td></tr>"));
        assertThat(html2, containsString("<tr><td>Grape</td><td>Purple</td></tr>"));

        // - page 3
        assertThat(outputFile3.exists(), is(true));
        String html3 = readFromFileIntoString(outputFile3);
        assertThat(html3, is(not(nullValue())));
        assertThat(html3, containsString("<title>Sample Data</title>"));
        assertThat(html3, containsString("<h1>Fruits</h1>"));
        assertThat(html3, containsString("Are good for you..."));
        assertThat(html3, containsString("<table>"));
        assertThat(html3, containsString("<tr><td>Cherry</td><td>Red</td></tr>"));
        assertThat(html3, containsString("<tr><td>Water Melon</td><td>Green</td></tr>"));
    }
}
