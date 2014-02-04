package com.btaz.util.xml.diff;

import com.btaz.util.xml.XmlReader;
import com.btaz.util.xml.model.Xml;
import com.btaz.util.unit.ResourceUtil;
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
public class XmlReporterTest {
    private XmlReporter xmlReporter;

    @Before
    public void setUp() throws Exception {
        xmlReporter = new XmlReporter();
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
        Xml a = new Xml()
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
        Xml b = new Xml()
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
        Report report = xmlReporter.compare(a, b);

        // then
        assertThat(report, is(not(nullValue())));
        assertThat(report.hasDifferences(), is(false));
    }


    @Test
    public void testDifferenceReportsWithEmpty_A_And_B_ShouldHaveNoDifferences() throws Exception {
        // given

        Xml a = new Xml();
        Xml b = new Xml();

        // when
        Report report = xmlReporter.compare(a, b);

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
        Xml a = new Xml()
                .addElement("<fruits>")
                    .addElement("<orange/>")
                    .addElement("<lemon />")
                    .addElement("<pear />");
        a.setName("A");

        /*
            <fruits>
              <orange />
              <banana />
              <lemon />
            </fruits>
        */
        Xml b = new Xml()
                .addElement("<fruits>")
                    .addElement("<orange/>")
                    .addElement("<banana/>")
                    .addElement("<lemon />");
        b.setName("B");

        // when
        Report report = xmlReporter.compare(a, b);

        // then
        Difference [] diff = getDifferences(report);
        assertThat(report, is(not(nullValue())));
        assertThat(report.hasDifferences(), is(true));
        assertThat(diff.length, is(2));

        assertThat(diff[0].getReason(), is(equalTo("Only in: B")));
        assertThat(diff[0].getPathA(), is(equalTo("")));
        assertThat(diff[0].getPathB(), is(equalTo("<fruits><banana />")));

        assertThat(diff[1].getReason(), is(equalTo(("Only in: A"))));
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
        Xml a = new Xml()
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
        Xml b = new Xml()
                .addElement("<vehicles>")
                .addElement("<truck />")
                .addElement("<bus />")
                .addElement("<car />");

        // when
        Report report = xmlReporter.compare(a, b);

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

        inputFile = ResourceUtil.getResourceFile("sample-6a.xml");
        inputStream = new FileInputStream(inputFile);
        reader = new XmlReader(inputStream);
        Xml doc1 = reader.read("/doc");
        doc1.setName("A");
        inputStream.close();

        inputFile = ResourceUtil.getResourceFile("sample-6b.xml");
        inputStream = new FileInputStream(inputFile);
        reader = new XmlReader(inputStream);
        Xml doc2 = reader.read("/doc");
        doc2.setName("B");
        inputStream.close();

        // when
        Report report = new XmlReporter().compare(doc1, doc2);
        Difference [] differences = getDifferences(report);

        // then
        assertThat(report, is(not(nullValue())));
        assertThat(differences[0].getReason(), containsString("Only in: B"));
        assertThat(differences[0].getPathB(), containsString("<str>"));
        assertThat(differences[1].getReason(), containsString("Only in: B"));
        assertThat(differences[1].getPathB(), containsString("price"));
        assertThat(differences[2].getReason(), containsString("Only in: A"));
        assertThat(differences[2].getPathA(), containsString("endoflife"));
    }

    @Test
    public void testOfDiffingTwoXmlFilesWithDifferentElementOrderShouldFindNoDifferences() throws Exception {
        // given
        File inputFile;
        InputStream inputStream;
        XmlReader reader;

        inputFile = ResourceUtil.getResourceFile("sample-7a.xml");
        inputStream = new FileInputStream(inputFile);
        reader = new XmlReader(inputStream);
        Xml doc1 = reader.read("/doc");
        inputStream.close();

        inputFile = ResourceUtil.getResourceFile("sample-7b.xml");
        inputStream = new FileInputStream(inputFile);
        reader = new XmlReader(inputStream);
        Xml doc2 = reader.read("/doc");
        inputStream.close();

        // when
        Report report = new XmlReporter().compare(doc1, doc2, new SortedChildElementsArbitrator(), null);

        // then
        assertThat(report, is(not(nullValue())));
        assertThat(report.hasDifferences(), is(false));
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
