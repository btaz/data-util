package com.btaz.datautil.xml.diff;

import com.btaz.datautil.files.FileTracker;
import com.btaz.datautil.xml.model.Document;
import com.btaz.datautil.xml.model.Node;
import com.btaz.utils.ResourceUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileReportTest {
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
    public void testOfFileReportShouldWriteDataToFile() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File reportFile = new File(testDir, "differences.log");
        tracker.add(reportFile);
        FileReport report = new FileReport(reportFile);

        Document doc = new Document()
                .addElement("<fruits>")
                .addElement("<apple />")
                .addElement("<orange />");

        List<Node> nodeA = doc.pathQuery("/fruits/apple");
        List<Node> nodeB = doc.pathQuery("/fruits/orange");

        // when
        report.add(new Difference(nodeA.get(0), nodeB.get(0), "Different fruits"));
        report.add(new Difference(nodeA.get(0), nodeB.get(0), "Different again"));
        report.close();

        // then
        assertThat(reportFile.exists(), is(true));
        String reportText = ResourceUtil.readFromFileIntoString(reportFile);
        assertThat(reportText, containsString("Different fruits"));
        assertThat(reportText, containsString("Different again"));
        assertThat(reportText, containsString("<fruits><apple />"));
        assertThat(reportText, containsString("<fruits><orange />"));
    }
}
