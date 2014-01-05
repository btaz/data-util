package com.btaz.datautil.files;

import com.btaz.datautil.files.mapreduce.KeyComparator;
import com.btaz.datautil.files.mapreduce.OutputCollector;
import com.btaz.datautil.files.mapreduce.Reducer;
import com.btaz.utils.ResourceUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class FileReducerTest {
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
    public void testOfReducerWithInputDataShouldReduceData() throws Exception {
        // given
        File testDir = tracker.createDir(new File("target/test-dir"));
        File inputFile = tracker.getTestResource("test-4.txt");
        File expectedFile = tracker.getTestResource("reduced-4.txt");
        File outputFile = tracker.createFile(testDir, "reducer-output.txt");
        FruitReducer reducer = new FruitReducer();
        FruitKeyComparator comparator = new FruitKeyComparator();

        // when
        FileReducer.reduce(inputFile, outputFile, reducer, comparator);

        // then
        assertThat(outputFile.exists(), is(true));
        String actual = ResourceUtil.readFromFileIntoString(outputFile);
        String expected = ResourceUtil.readFromFileIntoString(expectedFile);
        assertThat(actual, is(equalTo(expected)));
    }

    /**
     * Map input data to fruit and Id. In this case we only about a specific fruit.
     * Map(,[fruit,id,description]) ==> list(fruit,id)
     */
    public static class FruitReducer implements Reducer {
        @Override
        public void reduce(List<String> items, OutputCollector collector) {
            String [] fields = items.get(0).split("\t");
            collector.write(fields[1] + "\t" + items.size());
        }
    }

    /**
     * Fruit key comparator, used to group items for the reducer
     */
    public static class FruitKeyComparator implements KeyComparator {
        @Override
        public int compare(String o1, String o2) {
            String [] f1 = o1.split("\t");
            String [] f2 = o2.split("\t");
            return f1[1].compareTo(f2[1]);
        }
    }
}
