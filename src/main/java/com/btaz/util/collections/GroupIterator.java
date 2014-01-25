package com.btaz.util.collections;

import java.util.*;

/**
 * User: msundell
 */
public class GroupIterator<E extends List<V>,V> implements Iterator<E> {
    private final LinkedList<V> source;
    private final Comparator<V> comparator;

    public GroupIterator(List<V> input, Comparator<V> comparator) {
        source = new LinkedList<V>(input);
        this.comparator = comparator;
    }

    @Override
    public boolean hasNext() {
        return !source.isEmpty();
    }

    @Override
    public E next() {
        if(source.isEmpty()) {
            return null;
        }
        V item;
        @SuppressWarnings("unchecked")
        E items = (E) new ArrayList<V>();
        items.add(item = source.poll());
        while(!source.isEmpty()) {
            if(comparator.compare(item, source.peek()) == 0) {
                items.add(source.poll());
            } else {
                return items;
            }
        }
        return items;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("The remove operation is not supported");
    }
}
