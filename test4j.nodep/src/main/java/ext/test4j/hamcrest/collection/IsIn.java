package ext.test4j.hamcrest.collection;

import ext.test4j.hamcrest.BaseMatcher;
import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.Matcher;

import java.util.Arrays;
import java.util.Collection;

public class IsIn<T> extends BaseMatcher<T> {
	private final Collection<T> collection;

	public IsIn(Collection<T> collection) {
		this.collection = collection;
	}

	public IsIn(T[] elements) {
		collection = Arrays.asList(elements);
	}

	public boolean matches(Object o) {
		return collection.contains(o);
	}

	public void describeTo(Description buffer) {
		buffer.appendText("one of ");
		buffer.appendValueList("{", ", ", "}", collection);
	}

	@Factory
	public static <T> Matcher<T> isIn(Collection<T> collection) {
		return new IsIn<T>(collection);
	}

	@Factory
	public static <T> Matcher<T> isIn(T[] elements) {
		return new IsIn<T>(elements);
	}

	@Factory
	public static <T> Matcher<T> isOneOf(T... elements) {
		return isIn(elements);
	}
}
