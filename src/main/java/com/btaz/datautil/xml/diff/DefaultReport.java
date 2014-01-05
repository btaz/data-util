package com.btaz.datautil.xml.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * In-memory report
 * User: msundell
 */
public class DefaultReport implements Report {
    private List<Difference> list;

    /**
     * {@inheritDoc}
     */
    public DefaultReport() {
        list = new ArrayList<Difference>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Difference difference) {
        list.add(difference);
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
