package com.btaz.util.reader.xml.diff;

import com.btaz.util.DataUtilDefaults;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * File based report. This reporter stores a limited amount of differences in memory. All differences are written to a
 * user defined file.
 * User: msundell
 */
public class FileReport implements Report {
    private List<Difference> list;
    private BufferedWriter writer;
    private int differenceCount;
    private final int DIFFERENCE_COUNT_MAX = 20;

    /**
     * Initialize a new file report instance
     */
    public FileReport(File reportFile) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(reportFile.getAbsoluteFile(), false), DataUtilDefaults.charSet));
        } catch (Exception e) {
            throw new DiffException("Failed to open report file: " + reportFile.getAbsolutePath());
        }

        list = new ArrayList<Difference>();
        differenceCount = 0;
    }

    /**
     * Add a new difference to the report
     * @param difference {@code Difference}
     */
    @Override
    public void add(Difference difference) {
        differenceCount += 1;
        if(differenceCount <= DIFFERENCE_COUNT_MAX) {
            list.add(difference);
        }
        try {
            writer.write(difference.toString());
        } catch (IOException e) {
            throw new DiffException("Failed to write difference to report file", e);
        }
    }

    /**
     * Close the file report instance and it's underlying writer
     * @throws DiffException
     */
    public void close() throws DiffException {
        try {
            if(writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            throw new DiffException("Failed to close report file", e);
        }
    }

    /**
     * Are there any differences to reported
     * @return {@code boolean} true if there are differences
     */
    @Override
    public boolean hasDifferences() {
        return list.size() > 0;
    }

    /**
     * Get an iterator for all differences
     * @return {@code Iterator} for all {@code Difference} objects
     */
    @Override
    public Iterator<Difference> getAllDifferences() {
        return list.iterator();
    }

    /**
     * Write all in-memory differences into a String object
     * @return {@code String} all differences as a String object
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for(Difference difference : list) {
            text.append(difference.getReason()).append("\n");
            if(difference.getPathA().length() > 0) {
                text.append(" - ").append(difference.getPathA()).append("\n");
            }
            if(difference.getPathB().length() > 0) {
                text.append(" - ").append(difference.getPathB()).append("\n");
            }
            text.append("\n");
        }
        return text.toString();
    }

    /**
     * Implement a finalize to attempt to close the writer
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(writer != null) {
            writer.close();
        }
    }
}
