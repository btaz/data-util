package sets;

import java.util.*;

/**
 * User: msundell
 */
public class Sets {
    /**
     * Convenience method for building a set from vararg arguments
     * @param args argument list
     * @param <T> set type
     * @return a new set created from var args
     */
    public static <T> Set<T> createSet(T... args) {
        HashSet<T> newSet = new HashSet<T>();
        Collections.addAll(newSet, args);
        return newSet;
    }

    /**
     * Convenience method for building a set from an array
     * @param array
     * @param <T>
     * @return a new set created from var args
     */
    public static <T> Set<T> arrayToSet(T [] array) {
        return new HashSet<T>(Arrays.asList(array));
    }

    /**
     * This method finds the intersection between set A and set B
     * @param setA set A
     * @param setB set B
     * @param <T> type
     * @return a new set containing the intersection
     */
    public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
        Set<T> intersection = new HashSet<T>(setA);
        intersection.retainAll(setB);
        return intersection;
    }

    /**
     * This method finds the union of set A and set B
     * @param setA set A
     * @param setB set B
     * @param <T> type
     * @return a new set containing the union
     */
    public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
        Set<T> union = new HashSet<T>(setA);
        union.addAll(setB);
        return union;
    }

    /**
     * This method finds the difference between set A and set B i.e. set A minus set B
     * @param setA set A
     * @param setB set B
     * @param <T> type
     * @return a new set containing the difference
     */
    public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
        Set<T> difference = new HashSet<T>(setA);
        difference.removeAll(setB);
        return difference;
    }

    /**
     * This method finds the symmetric difference between set A and set B i.e. which items does not exist in either
     * set A or set B. This is the opposite of the intersect of set A and set B.
     * @param setA set A
     * @param setB set B
     * @param <T> type
     * @return a new set containing the symmetric difference
     */
    public static <T> Set<T> symmetricDifference(Set<T> setA, Set<T> setB) {
        Set<T> union = union(setA, setB);
        Set<T> intersection = intersection(setA, setB);
        return difference(union, intersection);
    }

    /**
     * This method returns true if set A is a subset of set B
     * i.e. it answers the question if all items in A exists in B
     * @param setA set A
     * @param setB set B
     * @param <T> type
     * @return {@code boolean} true if A is a subset of B
     */
    public static <T> boolean isSubset(Set<T> setA, Set<T> setB) {
        return setB.containsAll(setA);
    }

    /**
     * This method returns true if set A is a superset of set B
     * i.e. it answers the question if A contains all items from B
     * @param setA set A
     * @param setB set B
     * @param <T> type
     * @return {@code boolean} true if A is a superset of B
     */
    public static <T> boolean isSuperset(Set<T> setA, Set<T> setB) {
        return setA.containsAll(setB);
    }
}
