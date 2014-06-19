package com.btaz.util.xml.diff;

import java.util.*;

/**
 * In-memory report
 * User: msundell
 */
public class DefaultReport extends Report {
    private List<Difference> list;
    private Set<String> ignorePaths;

    /**
     * Initialize a new default report instance
     */
    public DefaultReport(String nameA, String nameB) {
        super(nameA, nameB);
        list = new ArrayList<Difference>();
    }

    /**
     * This method allows you to ignore specific paths
     * @param ignorePathList list of ignore paths
     */
    public DefaultReport(String nameA, String nameB, List<String> ignorePathList) {
        this(nameA, nameB);
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
            String pathA = trimNonXmlElements(difference.getPathA());
            String pathB = trimNonXmlElements(difference.getPathB());
            if (ignorePaths.contains(pathA) || ignorePaths.contains(pathB)) {
                // ignorable matched
                if(ignorePaths.contains(pathA) && ignorePaths.contains(pathB)) {
                    // both sides contains an ignorable difference
                    return;
                } else if(pathA.length() > 0 && ignorePaths.contains(pathB)) {
                    // B contains ignorable
                    difference = new Difference(difference.getPathA(), null, "Only in: " + getNameA());
                    list.add(difference);
                } else if(ignorePaths.contains(pathA) && pathB.length() > 0) {
                    // A contains ignorable
                    difference = new Difference(null, difference.getPathB(), "Only in: " + getNameB());
                    list.add(difference);
                }
            } else {
                // no ignorable matched
                list.add(difference);
            }
        }
    }

    /*
     * This method trims non XML element data from the end of a path
     * e.g. <float name="score">12.34567 would become <float name="score">, 12.34567 would be stripped out
     */
    @SuppressWarnings("RedundantStringConstructorCall") // avoid String substring memory leak in JDK 1.6
    private String trimNonXmlElements(String path) {
        if(path != null && path.length() > 0) {
            int pos = path.lastIndexOf(">");
            if(pos > -1) {
                path = new String(path.substring(0, pos+1));
            }
        }
        return path;
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
