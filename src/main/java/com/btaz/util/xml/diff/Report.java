package com.btaz.util.xml.diff;

import java.util.Iterator;

/**
 * This abstract class describes a report collector for differences between two XML trees
 * User: msundell
 */
public abstract class Report {
    private final String nameA;
    private final String nameB;

    protected Report(String nameA, String nameB) {
        this.nameA = nameA;
        this.nameB = nameB;
    }

    public String getNameA() {
        return nameA;
    }

    public String getNameB() {
        return nameB;
    }

    /**
     * Add a new difference to the report
     * @param difference {@code Difference}
     */
    public abstract void add(Difference difference);

    /**
     * Are there any differences to reported
     * @return {@code boolean} true if there are differences
     */
    public abstract boolean hasDifferences();

    /**
     * Get an iterator for all differences
     * @return {@code Iterator} for all {@code Difference} objects
     */
    public abstract Iterator<Difference> getAllDifferences();
}
