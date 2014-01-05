package com.btaz.datautil.xml.diff;

import java.util.Iterator;

/**
 * This interface describes a report collector for differences between two XML trees
 * User: msundell
 */
public interface Report {
    /**
     * Add a new difference to the report
     * @param difference {@code Difference}
     */
    void add(Difference difference);

    /**
     * Are there any differences to reported
     * @return {@code boolean} true if there are differences
     */
    boolean hasDifferences();

    /**
     * Get an iterator for all differences
     * @return {@code Iterator} for all {@code Difference} objects
     */
    Iterator<Difference> getAllDifferences();
}
