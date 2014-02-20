package com.btaz.util.files;

import com.btaz.util.mr.MapReduceException;
import com.btaz.util.mr.Mapper;
import com.btaz.util.mr.OutputCollector;
import com.btaz.util.unit.ResourceUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileMapperTest {
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
    public void testOfMappingInputFileShouldMapFile() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File inputFile = tracker.getTestResource("test-3.txt");
        Mapper mapper = new FruitMapper("Lime");

        // when
        List<File> mappedFiles = FileMapper.map(testDir, inputFile, mapper, null);
        tracker.add(mappedFiles);

        // then
        assertThat(mappedFiles, is(not(nullValue())));
        assertThat(mappedFiles.size(), is(equalTo(1)));
        String mappedData = ResourceUtil.readFromFileIntoString(mappedFiles.get(0));
        assertThat(countOccurrences(mappedData, "lime"), is(equalTo(4)));
    }

    /**
     * Helper method that tests to see how many times a string occur in an input string. All matches ignore case.
     * @param input input string
     * @param substring substring
     * @return int count
     */
    private int countOccurrences(String input, String substring) {
        int count = 0;
        int index = 0;
        input = input.toLowerCase();
        substring = substring.toLowerCase();

        while(true) {
            index = input.indexOf(substring, index);
            if(index == -1) {
                break;
            }
            count += 1;
            index += substring.length();
        }
        return count;
    }

    /**
     * Map input data to fruit and Id. In this case we only about a specific fruit.
     * Map(,[fruit,id,description]) ==> list(fruit,id)
     */
    public static class FruitMapper implements Mapper {
        private String fruit;

        private FruitMapper(String fruit) {
            this.fruit = fruit;
        }

        @Override
        public void map(String value, OutputCollector collector) {
            if(value == null || value.trim().length() == 0) {
                // skip empty rows
                return;
            }
            String [] fields = value.split("\t");
            if(fields.length != 3) {
                throw new MapReduceException("Invalid field count, expected 3 but found: " + fields.length);
            } else if("ID".equalsIgnoreCase(fields[0])) {
                // skip header row
            } else if(fruit.equalsIgnoreCase(fields[1])) {
                // found a matching fruit KEY(fruit,ID)
                collector.write(fields[1].toLowerCase() + "\t" + fields[0]);
            }

            // non-matching fruit
        }
    }
}
