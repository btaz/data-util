package com.btaz.util.mr;

import java.util.ArrayList;
import java.util.List;

/**
 * This output collector collect data into an in-memory List
 * Warning: This class is for test purposes. Normally output from a MapReduce program will not fit in memory.
 * Todo: create tests for this class
 * User: msundell
 */
public class ListOutputCollector implements OutputCollector {
    private List<String> data = new ArrayList<String>();

    /**
     * This method writes data to the collector
     * @param dataItem item
     * @throws MapReduceException
     */
    @Override
    public void write(String dataItem) throws MapReduceException {
        data.add(dataItem);
    }

    /**
     * Empties all collected data
     */
    public void clear() {
        data.clear();
    }

    /**
     * Get all collected data
     * @return {@code List} of {@code String} items
     */
    public List<String> getCollectedData() {
        return data;
    }
}
