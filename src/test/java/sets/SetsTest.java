package sets;

import org.hamcrest.Matchers;
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
}
