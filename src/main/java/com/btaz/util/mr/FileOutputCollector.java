package com.btaz.util.mr;

import com.btaz.util.DataUtilDefaults;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: msundell
 */
public class FileOutputCollector implements OutputCollector {
    private ArrayList<File> files;
    private BufferedWriter writer;

    /**
     * Default constructor
     * @param workDir work (output) directory for the output collector
     * @param prefix prefix for collector files
     * @exception MapReduceException map reduce exception
     */
    public FileOutputCollector(File workDir, String prefix) throws MapReduceException {
        if(prefix == null) {
            // todo: need a test for this
            prefix = "file";
        }
        try {
            files = new ArrayList<File>();
            File collectorFile = new File(workDir, prefix + "-1.part");
            files.add(collectorFile);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(collectorFile, false),
                    DataUtilDefaults.charSet));
        } catch (Exception e) {
            throw new MapReduceException("Failed to create an output collector", e);
        }
    }

    /**
     * Default constructor
     * @param collectorFile collector file that data is written to
     * @exception MapReduceException map reduce exception
     */
    public FileOutputCollector(File collectorFile) throws MapReduceException {
        if(collectorFile == null) {
            throw new MapReduceException("The collectorFile parameter can not be null");
        }
        try {
            files = new ArrayList<File>();
            files.add(collectorFile);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(collectorFile, false),
                    DataUtilDefaults.charSet));
        } catch (Exception e) {
            throw new MapReduceException("Failed to create an output collector", e);
        }
    }

    /**
     * Write to the output collector
     * @param output data to write
     * @exception MapReduceException map reduce exception
     */
    @Override
    public void write(String output) throws MapReduceException {
        try {
            writer.write(output + DataUtilDefaults.lineTerminator);
        } catch (IOException e) {
            throw new MapReduceException("Failed to write to the output collector", e);
        }
    }

    /**
     * Get a list of all created output collector files
     * @return <code>List</code> of <code>File</code> items
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Close the output collector
     * @throws MapReduceException map reduce exception
     */
    public void close() throws MapReduceException {
        try {
            if(writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            throw new MapReduceException("Failed to close the output collector", e);
        }
    }
}
