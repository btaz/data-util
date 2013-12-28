package com.btaz.datautil.files.mapreduce;

import java.util.Comparator;

/**
 * User: msundell
 */
public interface KeyComparator extends Comparator<String> {
    @Override
    int compare(String o1, String o2);
}
