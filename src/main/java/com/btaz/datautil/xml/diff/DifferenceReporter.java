package com.btaz.datautil.xml.diff;

import com.btaz.datautil.xml.model.Document;
import com.btaz.datautil.xml.model.Element;
import com.btaz.datautil.xml.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to compare two XML {@code Document} objects with each other.
 * User: msundell
 */
public class DifferenceReporter {
    /**
     * This method is used to compare two XML {@code Document} objects with each other. The differences are written to
     * the {@code Report} object. This method uses the default arbitrator and reporter.
     * BFS (breadth-first search) comparison is used when comparing the XML trees.
     * The default arbitrator {@code DefaultArbitrator} is very strict and will not accept any differences.
     * @param a the {@code Document} object to compare with
     * @param b the {@code Document} object to compare with
     * @return @{code Report} object
     */
    public Report compare(Document a, Document b) {
        return compare(a, b, null, null);
    }

    /**
     * This method is used to compare two XML {@code Document} objects with each other. The differences are written to
     * the {@code Report} object. To control what is different and what is not different you can provide a arbitrator.
     * BFS (breadth-first search) comparison is used when comparing the XML trees.
     * The default arbitrator {@code DefaultArbitrator} is very strict and will not accept any differences.
     * @param a the {@code Document} object to compare with
     * @param b the {@code Document} object to compare with
     * @param arbitrator {@code Arbitrator} used for comparison, null will use the default arbitrator
     * @param report {@code Report} collector for differences in the documents
     * @return @{code Report} object
     */
    public Report compare(Document a, Document b, Arbitrator arbitrator, Report report) {
        // validations
        if(a == null) {
            throw new DiffException("The Document a parameter can not be a null value");
        }
        if(b == null) {
            throw new DiffException("The Document b parameter can not be a null value");
        }

        // initializations
        if(arbitrator == null) {
            arbitrator = new DefaultArbitrator();
        }
        if(report == null) {
            report = new DefaultReport();
        }

        // clone document
        try {
            a = (Document) a.clone();
            b = (Document) b.clone();
        } catch (CloneNotSupportedException e) {
            throw new DiffException("Can't clone the document", e);
        }

        // compare documents
        if(a.getRoot() == null) {
            // todo: write all of B to report
        } else if(b.getRoot() == null) {
            // todo: write all of A to report
        } else {
            List<Node> listA = new ArrayList<Node>();
            listA.add(a.getRoot());
            List<Node> listB = new ArrayList<Node>();
            listB.add(b.getRoot());
            dfsComparison(report, arbitrator, listA, listB);
        }

        return report;
    }

    /**
     * A strict depth-first search traversal to evaluate two XML trees against each other
     * @param report {@code Report} object
     * @param a {@code Node} first tree
     * @param b {@code Node} second tree
     * @return {@code boolean} true if the trees matches, false otherwise
     */
    private void dfsComparison(Report report, Arbitrator arbitrator, List<Node> a, List<Node> b) {
        // #1 pre-process lists
        arbitrator.preProcessor(a);
        arbitrator.preProcessor(b);

        // #2 diff nodes
        int i=0;
        int j=0;
        Difference difference;
        MATCHING:
        while(i<a.size() || j<b.size()) {
            // match mode
            if(i == a.size()) {
                report.add(new Difference(null, b.get(j), "Only in B"));
                j += 1;
                continue;
            } else if(j == b.size()) {
                report.add(new Difference(a.get(i), null, "Only in A"));
                i += 1;
                continue;
            }

            difference = arbitrator.compare(a.get(i), b.get(j));
            if (difference == null) {
                // nodes are the same

                // - if both nodes are Element, then scan children
                if(a.get(i) instanceof Element && b.get(j) instanceof Element) {
                    List<Node> listA = ((Element)a.get(i)).getChildElements();
                    List<Node> listB = ((Element)b.get(j)).getChildElements();
                    if(listA.size() > 0 || listB.size() > 0) {
                        dfsComparison(report, arbitrator, listA, listB);
                    }
                }

                // - advance to the next node pair
                i += 1;
                j += 1;
                continue;
            }
            // scan
            List<Difference> list;
            int s;

            // - scan B
            s = j;
            list = new ArrayList<Difference>();
            while(s < b.size()) {
                difference = arbitrator.compare(a.get(i), b.get(s));
                if(difference != null) {
                    // different - add difference to list and continue scan
                    list.add(new Difference(null, b.get(s), "Only in B"));
                    s += 1;
                } else {
                    // match - write list to report, add match, and continue matching
                    for(Difference d : list) {
                        report.add(d);
                    }
                    i += 1;
                    j = s + 1;
                    continue MATCHING;
                }
            }

            // - scan A
            s = i;
            list = new ArrayList<Difference>();
            while(s < a.size()) {
                difference = arbitrator.compare(a.get(s), b.get(j));
                if(difference != null) {
                    // different - add difference to list and continue scan
                    list.add(new Difference(a.get(s), null, "Only in A"));
                    s += 1;
                } else {
                    // match - write list to report, add match, and continue matching
                    for(Difference d : list) {
                        report.add(d);
                    }
                    i = s + 1;
                    j += 1;
                    continue MATCHING;
                }
            }

            // reconcile
            report.add(new Difference(a.get(i), b.get(j), "Both are different"));
            i += 1;
            j += 1;
        }
    }
}
