package com.btaz.util.sets;

/**
 * User: msundell
 */
public interface Criteria <T> {
    boolean meetsCriteria(T item);
}
