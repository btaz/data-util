package com.btaz.datautil.files.mapreduce;

/**
 * User: msundell
 */
public interface Mapper {
    /**
     * This is a map-reduce map call
     * @param value input data
     * @param collector output collector
     */
    void map(String value, OutputCollector collector);
}
