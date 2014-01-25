package com.btaz.util.collections;

import java.util.*;

/**
 * This is a group collector. It will group items matching the Comparator.
 * User: msundell
 */
public class GroupCollector<E extends List<V>,V> implements Iterable<E> {
    private final List<V> source;
    private final Comparator<V> comparator;

    public GroupCollector(Collection<V> collection, Comparator<V> comparator) {
        List<V> list = new LinkedList<V>(collection);
        this.comparator = comparator;
        Collections.sort(list, comparator);
        source = Collections.unmodifiableList(list);
    }

    public Iterator<E> iterator() {
        return new GroupIterator<E,V>(source, comparator);
    }
}
