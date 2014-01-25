package com.btaz.util.collections;

/**
 * User: msundell
 */
public interface KeyExtractor<T> {
    T extractKey(T item);
}
