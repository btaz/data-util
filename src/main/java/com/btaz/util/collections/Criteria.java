package com.btaz.util.collections;

/**
 * User: msundell
 */
public interface Criteria <T> {
    boolean meetsCriteria(T item);
}
