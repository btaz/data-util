package com.btaz.datautil.files.mapreduce;

import java.util.List;

/**
 * User: msundell
 */
public interface Reducer {
    /**
     * This is a map-reduce reduce call
     * @param items <code>List</code> of <code>String</code> items
     * @param collector output collector
     */
    void reduce(List<String> items, OutputCollector collector);
}
