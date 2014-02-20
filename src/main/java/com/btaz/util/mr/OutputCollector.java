package com.btaz.util.mr;

/**
 * User: msundell
 */
public interface OutputCollector {
    void write(String output) throws MapReduceException;
}
