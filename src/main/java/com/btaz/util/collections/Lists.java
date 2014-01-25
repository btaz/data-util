package com.btaz.util.collections;

import java.util.*;

/**
 * User: msundell
 */
public class Lists {
    /**
     * Convenience method for building a list from vararg arguments
     * @param args argument list
     * @param <T> list type
     * @return a new list created from var args
     */
    public static <T> List<T> createList(T... args) {
        ArrayList<T> newList = new ArrayList<T>();
        Collections.addAll(newList, args);
        return newList;
    }

    /**
     * Convenience method for building a list from an array
     * @param array
     * @param <T> list type
     * @return a new list created from var args
     */
    public static <T> List<T> arrayToList(T [] array) {
        return new ArrayList<T>(Arrays.asList(array));
    }

    /**
     * Extract a sub list from another list
     * @param list original list
     * @param criteria criteria used to determine whether or not to extract item
     * @param <T> list type
     * @return {@code List} of sub list items
     */
    public static <T> List<T> subList(List<T> list, Criteria<T> criteria) {
        ArrayList<T> subList = new ArrayList<T>();
        for(T item : list) {
            if(criteria.meetsCriteria(item)) {
                subList.add(item);
            }
        }
        return subList;
    }
}
