package ext.test4j.hamcrest.collection;

import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.Matcher;
import ext.test4j.hamcrest.TypeSafeMatcher;

/**
 * Tests if collection is empty.
 */
public class IsEmptyIterable<E> extends TypeSafeMatcher<Iterable<E>> {

    @Override
    public boolean matchesSafely(Iterable<E> iterable) {
        return !iterable.iterator().hasNext();
    }
    @Override
    public void describeMismatchSafely(Iterable<E> iter, Description mismatchDescription) {
        mismatchDescription.appendValueList("[", ",", "]", iter);
    }

    public void describeTo(Description description) {
        description.appendText("an empty iterable");
    }

    /**
     * Matches an empty iterable.
     */
    @Factory
    public static <E> Matcher<Iterable<E>> emptyIterable() {
        return new IsEmptyIterable<E>();
    }
}
