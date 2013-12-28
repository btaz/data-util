package com.btaz.datautil.comparators;

import java.util.Comparator;

/**
 * User: msundell
 */
public class Lexical {
    public static Comparator<String> ascending() {
        return new LexicalAscendingComparator();
    }

    public static Comparator<String> descending() {
        return new LexicalDescendingComparator();
    }

    private static class LexicalAscendingComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    private static class LexicalDescendingComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2) * -1;
        }
    }
}
