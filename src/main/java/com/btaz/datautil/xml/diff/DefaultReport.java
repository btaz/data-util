package com.btaz.datautil.xml.diff;

import java.util.*;

/**
 * In-memory report
 * User: msundell
 */
public class DefaultReport implements Report {
    private List<Difference> list;
    private Set<String> ignorePaths;

    /**
     * Initialize a new default report instance
     */
    public DefaultReport() {
        list = new ArrayList<Difference>();
    }

    /**
     * This method allows you to ignore specific paths
     * @param ignorePathList list of ignore paths
     */
    public DefaultReport(List<String> ignorePathList) {
        this();
        if(ignorePathList != null) {
            ignorePaths = new HashSet<String>();
            for(String ignorePath : ignorePathList) {
                ignorePaths.add(ignorePath.trim());
            }
        }
    }

    /**
     * Add a new difference to the report
     * @param difference {@code Difference}
     */
    @Override
    public void add(Difference difference) {
        if(ignorePaths == null) {
            list.add(difference);
        } else {
            if (!ignorePaths.contains(difference.getPathA()) && !ignorePaths.contains(difference.getPathB())) {
                list.add(difference);
            }
        }
    }

    /**
     * Are there any differences to reported
     * @return {@code boolean} true if there are differences
     */
    public boolean hasDifferences() {
        return list.size() > 0;
    }

    /**
     * Get an iterator for all differences
     * @return {@code Iterator} for all {@code Difference} objects
     */
    @Override
    public Iterator<Difference> getAllDifferences() {
        return list.iterator();
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for(Difference difference : list) {
            text.append(difference.getReason()).append("\n");
            if(difference.getPathA().length() > 0) {
                text.append(" - ").append(difference.getPathA()).append("\n");
            }
            if(difference.getPathB().length() > 0) {
                text.append(" - ").append(difference.getPathB()).append("\n");
            }
            text.append("\n");
        }
        return text.toString();
    }
}
