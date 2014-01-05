package com.btaz.datautil.files;

import com.btaz.datautil.DataUtilDefaults;
import com.btaz.datautil.DataUtilException;
import com.btaz.datautil.files.mapreduce.KeyComparator;
import com.btaz.datautil.files.mapreduce.MapReduceException;
import com.btaz.datautil.files.mapreduce.OutputCollector;
import com.btaz.datautil.files.mapreduce.Reducer;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: msundell
 */
public class FileReducer {
    /**
     * This method functions provide reduce (map-reduce) like functionality. For every row in the input data there is a
     * call to the reduce callback method. The input data must be pre-sorted matching the ReducableKeyMatcher.
     * @param inputFile input file
     * @param outputFile output file
     * @param reducer callback method, a class that implements the Reducer interface
     * @param comparator a key comparator, this is used to determine if two or more items share the same key.
     * @throws DataUtilException data util exception
     */
    public static void reduce(File inputFile, File outputFile, Reducer reducer, KeyComparator comparator)
            throws DataUtilException {
        // validations
        if(inputFile == null) {
            throw new DataUtilException("The inputFile parameter can not be a null value");
        }
        if(outputFile == null) {
            throw new DataUtilException("The outputFile parameter can not be a null value");
        }
        if(reducer == null) {
            throw new DataUtilException("The reducable parameter can not be a null value");
        }
        if(comparator == null) {
            throw new DataUtilException("The comparator parameter can not be a null value");
        }

        // reduce all data
        FileInputStream inputStream;
        OutputCollector collector = new OutputCollector(outputFile);
        QueueReader queueReader;

        // Aggregate all items matching the comparator and call the Reducable callback
        try {
            inputStream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,
                    DataUtilDefaults.charSet));
            queueReader = new QueueReader(br);

            // reduce data
            String prev = null;
            String curr;
            ArrayList<String> items = new ArrayList<String>();
            while(true) {
                curr = queueReader.readLine();
                if(curr == null) {
                    // no more data
                    reduceItems(collector, reducer, items);
                    break;
                } else if(prev == null) {
                    // first row in a new batch
                    items.add(curr);
                    prev = curr;
                } else if(comparator.compare(prev, curr) == 0) {
                    // same keys
                    items.add(curr);
                    prev = curr;
                } else {
                    // different keys
                    queueReader.push(curr);
                    reduceItems(collector, reducer, items);
                    items.clear();
                    prev = null;
                }
            }
            collector.close();
            inputStream.close();
        } catch (IOException e) {
            throw new DataUtilException(e);
        } catch (MapReduceException e) {
            throw new DataUtilException("Irrecoverable reduce operation", e);
        }
    }

    /**
     * Call reducer
     * @param writer writer
     * @param reducable reducable callback
     * @param items items
     * @throws IOException IO exception
     */
    private static void reduceItems(OutputCollector collector, Reducer reducable, ArrayList<String> items)
            throws IOException {
        if(items != null) {
            // call reducer
            reducable.reduce(items, collector);
        }
    }

    /**
     * This helper class applies a push-back queue on top of the BufferedReader
     */
    private static class QueueReader {
        private BufferedReader reader;
        private LinkedList<String> pushBackQueue;

        private QueueReader(BufferedReader reader) {
            this.reader = reader;
            pushBackQueue = new LinkedList<String>();
        }

        /**
         * Reds a line of text from the buffered reader. The method returns null if there's no more data.
         * A line of text is terminated by any of these:
         * - line feed ('\n')
         * - carriage return ('\r')
         * - carriage return + linefeed
         *
         * @return row as string not including any line-termination characters, or null if the end of the stream has
         * been reached. The method first attempts to readLine data from the push-back, and secondary from the reader.
         * @exception  IOException if an I/O error occurs
         */
        public String readLine() throws IOException {
            if(pushBackQueue.size() > 0) {
                return pushBackQueue.pop();
            } else {
                return reader.readLine();
            }
        }

        public void push(String row) throws IOException {
            pushBackQueue.push(row);
        }
    }
}
