package com.btaz.util.tf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: msundell
 */
public class Strings {
    /**
     * This method builds a list of string object from a variable argument list as input
     * @param args variable argument list
     * @return {@code List} of {@code String} objects
     */
    public static List<String> buildList(String... args) {
        List<String> newList = new ArrayList<String>();
        Collections.addAll(newList, args);
        return newList;
    }

    /**
     * This method converts a collection of strings to a comma separated list of strings
     * @param strings Collection of strings
     * @return {@code String} new comma separated string
     */
    public static String asCommaSeparatedList(Collection<String> strings) {
        StringBuilder newString = new StringBuilder();
        boolean first = true;
        for(String str : strings) {
            if(! first) {
                newString.append(",");
            }
            first = false;
            newString.append(str);
        }
        return newString.toString();
    }
}
