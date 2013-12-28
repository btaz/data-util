package com.btaz.datautil.files.mapreduce;

/**
 * User: msundell
 */
public interface Mappable {
    /**
     * This is a map-reduce map call
     * @param row input data
     * @return <code>String</code> mapped output data
     */
    String map(String row);
}
