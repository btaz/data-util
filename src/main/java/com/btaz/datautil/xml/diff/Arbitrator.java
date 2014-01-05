package com.btaz.datautil.xml.diff;

import com.btaz.datautil.xml.model.Node;

import java.util.List;

/**
 * This interface defines the methods required to arbitrate differences between two XML trees.
 * User: msundell
 */
public interface Arbitrator {
    /**
     * Compare the two nodes to each other. The Arbitrator pre-process method will have been run for all child elements
     * of parameter nodes a and b before this method is called.
     * The node parameters are guaranteed to be a non-null value.
     * @param a {@code Node}
     * @param b {@code Node}
     * @return {@code Difference} null if identical otherwise a difference object
     */
    Difference compare(Node a, Node b);

    /**
     * Pre-process child elements. This method was created to support:
     * - ignore Elements or content
     * - modify elements or content before comparison
     * - sorting child elements
     * @param list {@code List} of {@code DiffNode} items or null if the parent object have no child elements
     */
    void preProcessor(List<Node> list);
}
