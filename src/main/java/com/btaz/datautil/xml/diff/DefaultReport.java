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
     * {@inheritDoc}
     */
    public DefaultReport() {
        list = new ArrayList<Difference>();
    }

    /**
     * This method allows you to ignore specific paths
     * @param ignorePaths ignore paths
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public boolean hasDifferences() {
        return list.size() > 0;
    }

    /**
     * {@inheritDoc}
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
