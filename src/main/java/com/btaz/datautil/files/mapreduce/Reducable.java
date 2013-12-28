package com.btaz.datautil.files.mapreduce;

import java.util.List;

/**
 * User: msundell
 */
public interface Reducable {
    /**
     * This is a map-reduce reduce call
     * @param items <code>List</code> of <code>String</code> items
     * @return <code>List</code> of <code>String</code> items (output)
     */
    List<String> reduce(List<String> items);
}
