package ext.test4j.hamcrest.collection;

import static ext.test4j.hamcrest.core.IsEqual.equalTo;

import java.util.Iterator;

import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.FeatureMatcher;
import ext.test4j.hamcrest.Matcher;

public class IsIterableWithSize<E> extends FeatureMatcher<Iterable<E>, Integer> {

    public IsIterableWithSize(Matcher<? super Integer> sizeMatcher) {
        super(sizeMatcher, "an iterable with size", "iterable size");
    }
    

    @Override
    protected Integer featureValueOf(Iterable<E> actual) {
      int size = 0;
      for (Iterator<E> iterator = actual.iterator(); iterator.hasNext(); iterator.next()) {
        size++;
      }
      return size;
    }

    @Factory
    public static <E> Matcher<Iterable<E>> iterableWithSize(Matcher<? super Integer> sizeMatcher) {
        return new IsIterableWithSize<E>(sizeMatcher);
    }

    @Factory
    public static <E> Matcher<Iterable<E>> iterableWithSize(int size) {
        return iterableWithSize(equalTo(size));
    }
}
