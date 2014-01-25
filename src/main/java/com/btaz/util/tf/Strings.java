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
    public static String asCommaSeparatedValues(Collection<String> strings) {
        return Strings.asTokenSeparatedValues(",", strings);
    }

    /**
     * This method converts a collection of strings to a comma separated list of strings
     * @param args list of string arguments
     * @return {@code String} new comma separated string
     */
    public static String asCommaSeparatedValues(String... args) {
        List<String> newList = new ArrayList<String>();
        Collections.addAll(newList, args);
        return Strings.asTokenSeparatedValues(",", newList);
    }

    /**
     * This method converts a collection of strings to a tab separated list of strings
     * @param strings Collection of strings
     * @return {@code String} new tab separated string
     */
    public static String asTabSeparatedValues(Collection<String> strings) {
        return Strings.asTokenSeparatedValues("\t", strings);
    }

    /**
     * This method converts a collection of strings to a tab separated list of strings
     * @param args list of string arguments
     * @return {@code String} new tab separated string
     */
    public static String asTabSeparatedValues(String... args) {
        List<String> newList = new ArrayList<String>();
        Collections.addAll(newList, args);
        return Strings.asTokenSeparatedValues("\t", newList);
    }

    /**
     * This method converts a collection of strings to a token separated list of strings
     * @param args list of string arguments
     * @return {@code String} new token separated string
     */
    public static String asTokenSeparatedValues(String token, String... args) {
        List<String> newList = new ArrayList<String>();
        Collections.addAll(newList, args);
        return Strings.asTokenSeparatedValues(token, newList);
    }

    /**
     * This method converts a collection of strings to a token separated list of strings
     * @param strings Collection of strings
     * @return {@code String} new token separated string
     */
    public static String asTokenSeparatedValues(String token, Collection<String> strings) {
        StringBuilder newString = new StringBuilder();
        boolean first = true;
        for(String str : strings) {
            if(! first) {
                newString.append(token);
            }
            first = false;
            newString.append(str);
        }
        return newString.toString();
    }
}
