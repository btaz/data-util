package com.btaz.datautil.xml.diff;

import com.btaz.datautil.xml.XmlReader;
import com.btaz.datautil.xml.model.Document;
import com.btaz.utils.ResourceUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class DifferenceReporterTest {
    private DifferenceReporter differenceReporter;

    @Before
    public void setUp() throws Exception {
        differenceReporter = new DifferenceReporter();
    }

    @Test
    public void testDifferenceReportsWithIdentical_A_And_B_ShouldHaveNoDifferences() throws Exception {
        // given

        /*
            <fruits>
              <yellow>
                <banana />
                <lemon />
              </yellow>
              <green>
                <apple />
                <pear />
                <grapes />
              </green>
            </fruits>
        */
        Document a = new Document()
                .addElement("<fruits>")
                .addElement("<yellow>")
                .addElement("<banana />")
                .addElement("<lemon />")
                .endElement()
                .addElement("<green>")
                .addElement("<apple>")
                .addContent("<pear>")
                .addContent("<grapes>");

        /*
            <fruits>
              <yellow>
                <banana />
                <lemon />
              </yellow>
              <green>
                <apple />
                <pear />
                <grapes />
              </green>
            </fruits>
        */
        Document b = new Document()
                .addElement("<fruits>")
                .addElement("<yellow>")
                .addElement("<banana />")
                .addElement("<lemon />")
                .endElement()
                .addElement("<green>")
                .addElement("<apple>")
                .addContent("<pear>")
                .addContent("<grapes>");

        // when
        Report report = differenceReporter.compare(a, b);

        // then
        assertThat(report, is(not(nullValue())));
        assertThat(report.hasDifferences(), is(false));
    }


    @Test
    public void testDifferenceReportsWithEmpty_A_And_B_ShouldHaveNoDifferences() throws Exception {
        // given

        Document a = new Document();
        Document b = new Document();

        // when
        Report report = differenceReporter.compare(a, b);

        // then
        assertThat(report, is(not(nullValue())));
        assertThat(report.hasDifferences(), is(false));
    }

    @Test
    public void testDifferenceReportsWithDifferencesInBoth_A_And_B_ShouldFindBoth() throws Exception {
        // given

        /*
            <fruits>
              <orange />
              <lemon />
              <pear />
            </fruits>
        */
        Document a = new Document()
                .addElement("<fruits>")
                    .addElement("<orange/>")
                    .addElement("<lemon />")
                    .addElement("<pear />");

        /*
            <fruits>
              <orange />
              <banana />
              <lemon />
            </fruits>
        */
        Document b = new Document()
                .addElement("<fruits>")
                    .addElement("<orange/>")
                    .addElement("<banana/>")
                    .addElement("<lemon />");

        // when
        Report report = differenceReporter.compare(a, b);

        // then
        Difference [] diff = getDifferences(report);
        assertThat(report, is(not(nullValue())));
        assertThat(report.hasDifferences(), is(true));
        assertThat(diff.length, is(2));

        assertThat(diff[0].getReason(), is(equalTo("Only in B")));
        assertThat(diff[0].getPathA(), is(equalTo("")));
        assertThat(diff[0].getPathB(), is(equalTo("<fruits><banana />")));

        assertThat(diff[1].getReason(), is(equalTo(("Only in A"))));
        assertThat(diff[1].getPathA(), is(equalTo(("<fruits><pear />"))));
        assertThat(diff[1].getPathB(), is(equalTo((""))));
    }

    @Test
    public void testDifferenceReportsWithDifferencesInBoth_A_And_B_RootShouldFindDifference() throws Exception {
        // given

        /*
            <fruits>
              <orange />
              <lemon />
              <beagle />
            </fruits>
        */
        Document a = new Document()
                .addElement("<fruits>")
                .addElement("<orange />")
                .addElement("<lemon />")
                .addElement("<beagle />");

        /*
            <vehicles>
              <truck />
              <bus />
              <car />
            </vehicles>
        */
        Document b = new Document()
                .addElement("<vehicles>")
                .addElement("<truck />")
                .addElement("<bus />")
                .addElement("<car />");

        // when
        Report report = differenceReporter.compare(a, b);

        // then
        Difference [] diff = getDifferences(report);
        assertThat(report, is(not(nullValue())));
        assertThat(report.hasDifferences(), is(true));
        assertThat(diff.length, is(1));

        assertThat(diff[0].getReason(), is(equalTo(("Both are different"))));
        assertThat(diff[0].getPathA(), is(equalTo(("<fruits>"))));
        assertThat(diff[0].getPathB(), is(equalTo(("<vehicles>"))));
    }

    @Test
    public void testOfDiffingTwoXmlFilesShouldFindAllDifferences() throws Exception {
        // given
        File inputFile;
        InputStream inputStream;
        XmlReader reader;

        inputFile = ResourceUtil.getTestResourceFile("sample-6a.xml");
        inputStream = new FileInputStream(inputFile);
        reader = new XmlReader(inputStream);
        Document doc1 = reader.read("/doc");
        XmlReader.silentClose(inputStream);

        inputFile = ResourceUtil.getTestResourceFile("sample-6b.xml");
        inputStream = new FileInputStream(inputFile);
        reader = new XmlReader(inputStream);
        Document doc2 = reader.read("/doc");
        XmlReader.silentClose(inputStream);

        // when
        Report report = new DifferenceReporter().compare(doc1, doc2);
        Difference [] differences = getDifferences(report);

        // then
        assertThat(report, is(not(nullValue())));
        assertThat(differences[0].getReason(), containsString("Only in B"));
        assertThat(differences[0].getPathB(), containsString("<str>"));
        assertThat(differences[1].getReason(), containsString("Only in B"));
        assertThat(differences[1].getPathB(), containsString("price"));
        assertThat(differences[2].getReason(), containsString("Only in A"));
        assertThat(differences[2].getPathA(), containsString("endoflife"));
    }

    /**
     * Test helper method that extracts all Differences from a Difference iterator
     * @param report report
     * @return Difference array;
     */
    private Difference [] getDifferences(Report report) {
        List<Difference> differences = new ArrayList<Difference>();
        if(report != null) {
            Iterator<Difference> it = report.getAllDifferences();
            while(it.hasNext()) {
                differences.add(it.next());
            }
        }
        return differences.toArray(new Difference[differences.size()]);
    }
}
