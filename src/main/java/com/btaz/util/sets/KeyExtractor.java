package com.btaz.util.sets;

/**
 * User: msundell
 */
public interface KeyExtractor<T> {
    T extractKey(T item);
}
