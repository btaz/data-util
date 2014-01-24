package com.btaz.util.sets;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * User: msundell
 */
public class SetsTest {
    @Test
    public void testOfOverlappingUnionShouldContainUnion() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("2", "3", "4");
        Set<String> expected = Sets.createSet("1", "2", "3", "4");

        // when
        Set<String> union = Sets.union(setA, setB);

        // then
        assertThat(union, is(not(nullValue())));
        assertThat(union.size(), is(equalTo(expected.size())));
        assertThat(union, Matchers.equalTo(expected));
    }

    @Test
    public void testOfFullOverlappingUnionShouldContainUnion() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("1", "2", "3");
        Set<String> expected = Sets.createSet("1", "2", "3");

        // when
        Set<String> union = Sets.union(setA, setB);

        // then
        assertThat(union, is(not(nullValue())));
        assertThat(union.size(), is(equalTo(expected.size())));
        assertThat(union, Matchers.equalTo(expected));
    }

    @Test
    public void testOfNonOverlappingUnionShouldContainUnion() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("4", "5", "6");
        Set<String> expected = Sets.createSet("1", "2", "3", "4", "5", "6");

        // when
        Set<String> union = Sets.union(setA, setB);

        // then
        assertThat(union, is(not(nullValue())));
        assertThat(union.size(), is(equalTo(expected.size())));
        assertThat(union, Matchers.equalTo(expected));
    }

    @Test
    public void testOfOverlappingIntersectionShouldContainIntersection() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("2", "3", "4");
        Set<String> expected = Sets.createSet("2", "3");

        // when
        Set<String> intersection = Sets.intersection(setA, setB);

        // then
        assertThat(intersection, is(not(nullValue())));
        assertThat(intersection.size(), is(equalTo(expected.size())));
        assertThat(intersection, Matchers.equalTo(expected));
    }

    @Test
    public void testOfOverlappingIntersectionWithCustomObjectShouldContainIntersection() throws Exception {
        // given
        Set<NameAge> setA = Sets.createSet(new NameAge("Joe", 55), new NameAge("Frank", 24));
        Set<NameAge> setB = Sets.createSet(new NameAge("Frank", 24), new NameAge("Anne", 33));
        Set<NameAge> expected = Sets.createSet(new NameAge("Frank", 24));

        // when
        Set<NameAge> intersection = Sets.intersection(setA, setB);

        // then
        assertThat(intersection, is(not(nullValue())));
        assertThat(intersection.size(), is(equalTo(expected.size())));
        assertThat(intersection, Matchers.equalTo(expected));
    }

    @Test
    public void testOfNonOverlappingIntersectionShouldContainEmptyIntersection() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("4", "5", "6");
        Set<String> expected = Collections.emptySet();

        // when
        Set<String> intersection = Sets.intersection(setA, setB);

        // then
        assertThat(intersection, is(not(nullValue())));
        assertThat(intersection.size(), is(equalTo(expected.size())));
        assertThat(intersection, Matchers.equalTo(expected));
    }

    @Test
    public void testOfOverlappingSymmetricDifferenceShouldContainDifference() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("2", "3", "4");
        Set<String> expected = Sets.createSet("1", "4");

        // when
        Set<String> symmetricDifference = Sets.symmetricDifference(setA, setB);

        // then
        assertThat(symmetricDifference, is(not(nullValue())));
        assertThat(symmetricDifference.size(), is(equalTo(expected.size())));
        assertThat(symmetricDifference, Matchers.equalTo(expected));
    }

    @Test
    public void testOfOverlappingSymmetricDifferenceWithCustomObjectShouldContainDifference() throws Exception {
        // given
        Set<NameAge> setA = Sets.createSet(new NameAge("Joe", 55), new NameAge("Frank", 24));
        Set<NameAge> setB = Sets.createSet(new NameAge("Frank", 24), new NameAge("Anne", 33));
        Set<NameAge> expected = Sets.createSet(new NameAge("Anne", 33), new NameAge("Joe", 55));

        // when
        Set<NameAge> symmetricDifference = Sets.symmetricDifference(setA, setB);

        // then
        assertThat(symmetricDifference, is(not(nullValue())));
        assertThat(symmetricDifference.size(), is(equalTo(expected.size())));
        assertThat(symmetricDifference, Matchers.equalTo(expected));
    }

    @Test
    public void testOfNoOverlappingSymmetricDifferenceShouldContainNoDifference() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("1", "2", "3");
        Set<String> expected = Collections.emptySet();

        // when
        Set<String> symmetricDifference = Sets.symmetricDifference(setA, setB);

        // then
        assertThat(symmetricDifference, is(not(nullValue())));
        assertThat(symmetricDifference.size(), is(equalTo(expected.size())));
        assertThat(symmetricDifference, Matchers.equalTo(expected));
    }

    @Test
    public void testOfDifferenceFromSetAFromBShouldFindRemainder() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("2");
        Set<String> expected = Sets.createSet("1", "3");

        // when
        Set<String> difference = Sets.difference(setA, setB);

        // then
        assertThat(difference, is(not(nullValue())));
        assertThat(difference.size(), is(equalTo(expected.size())));
        assertThat(difference, Matchers.equalTo(expected));
    }

    @Test
    public void testOfIsSubsetWithMatchingSetsShouldReturnTrue() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("1", "2", "3");

        // when
        boolean result = Sets.isSubset(setA, setB);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfIsSubsetWithSubsetSetsShouldReturnTrue() throws Exception {
        // given
        Set<String> setA = Sets.createSet("2", "3");
        Set<String> setB = Sets.createSet("1", "2", "3");

        // when
        boolean result = Sets.isSubset(setA, setB);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfIsSubsetWithANonSubsetSetsShouldReturnFalse() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("2", "3", "4");

        // when
        boolean result = Sets.isSubset(setA, setB);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testOfIsSupersetWithMatchingSetsShouldReturnTrue() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("1", "2", "3");

        // when
        boolean result = Sets.isSuperset(setA, setB);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfIsSupersetWithSupersetSetShouldReturnTrue() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("2", "3");

        // when
        boolean result = Sets.isSuperset(setA, setB);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testOfIsSupersetWithANonSupersetShouldReturnFalse() throws Exception {
        // given
        Set<String> setA = Sets.createSet("1", "2", "3");
        Set<String> setB = Sets.createSet("2", "3", "4");

        // when
        boolean result = Sets.isSuperset(setA, setB);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testOfSubsetShouldCreateSubset() throws Exception {
        // given
        Set<String> input = Sets.createSet("apple", "blood orange", "oranges", "banana", "apricot");
        Set<String> expected = Sets.createSet("apple", "apricot");

        // when
        Set<String> subset = Sets.subset(input, new Criteria<String>() {
            @Override
            public boolean meetsCriteria(String item) {
                return item.charAt(0) == 'a';
            }
        });

        // then
        Assert.assertThat(subset, is(equalTo(expected)));
    }

    @Test
    public void testOfSubsetWithEmptyListShouldCreateEmptySubset() throws Exception {
        // given
        Set<String> input = Sets.createSet();
        Set<String> expected = Collections.emptySet();

        // when
        Set<String> subset = Sets.subset(input, new Criteria<String>() {
            @Override
            public boolean meetsCriteria(String item) {
                return item.charAt(0) == 'a';
            }
        });

        // then
        Assert.assertThat(subset, is(equalTo(expected)));
    }

    @Test
    public void testOfKeysetShouldCreateKeyset() throws Exception {
        // given
        Set<String> input = Sets.createSet("apple", "blood orange", "oranges", "banana", "apricot");
        Set<String> expected = Sets.createSet("a", "b", "o");

        // when
        Set<String> keyset = Sets.keyset(input, new KeyExtractor<String>() {
            @Override
            public String extractKey(String item) {
                return Character.toString(item.charAt(0));
            }
        });

        // then
        Assert.assertThat(keyset, is(equalTo(expected)));
    }

    @Test
    public void testOfKeysetWithEmptyListShouldCreateEmptyKeyset() throws Exception {
        // given
        Set<String> input = Sets.createSet();
        Set<String> expected = Collections.emptySet();

        // when
        Set<String> keyset = Sets.keyset(input, new KeyExtractor<String>() {
            @Override
            public String extractKey(String item) {
                return Character.toString(item.charAt(0));
            }
        });

        // then
        Assert.assertThat(keyset, is(equalTo(expected)));
    }

    /**
     * Test class
     */
    private static class NameAge {
        private final String name;
        private final int age;

        private NameAge(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NameAge nameAge = (NameAge) o;

            if (age != nameAge.age) return false;
            if (!name.equals(nameAge.name)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            return result;
        }
    }
}
