package com.btaz.util.mr;

import java.util.Comparator;

/**
 * User: msundell
 */
public interface KeyComparator extends Comparator<String> {
    @Override
    int compare(String o1, String o2);
}
