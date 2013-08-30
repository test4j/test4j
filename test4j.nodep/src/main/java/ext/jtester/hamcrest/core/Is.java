package ext.jtester.hamcrest.core;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.Factory;
import ext.jtester.hamcrest.Matcher;

import static ext.jtester.hamcrest.core.IsEqual.equalTo;
import static ext.jtester.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Decorates another Matcher, retaining the behavior but allowing tests to be
 * slightly more expressive.
 * 
 * For example: assertThat(cheese, equalTo(smelly)) vs. assertThat(cheese,
 * is(equalTo(smelly)))
 */
public class Is<T> extends BaseMatcher<T> {
	private final Matcher<T> matcher;

	public Is(Matcher<T> matcher) {
		this.matcher = matcher;
	}

	public boolean matches(Object arg) {
		return matcher.matches(arg);
	}

	public void describeTo(Description description) {
		description.appendText("is ").appendDescriptionOf(matcher);
	}

	@Override
	public void describeMismatch(Object item, Description mismatchDescription) {
		// TODO(ngd): unit tests....
		matcher.describeMismatch(item, mismatchDescription);
	}

	/**
	 * Decorates another Matcher, retaining the behavior but allowing tests to
	 * be slightly more expressive.
	 * 
	 * For example: assertThat(cheese, equalTo(smelly)) vs. assertThat(cheese,
	 * is(equalTo(smelly)))
	 */
	@Factory
	public static <T> Matcher<T> is(Matcher<T> matcher) {
		return new Is<T>(matcher);
	}

	/**
	 * This is a shortcut to the frequently used is(equalTo(x)).
	 * 
	 * For example: assertThat(cheese, is(equalTo(smelly))) vs.
	 * assertThat(cheese, is(smelly))
	 */
	@Factory
	public static <T> Matcher<T> is(T value) {
		return is(equalTo(value));
	}

	@Factory
	// @Deprecated
	public static <T> Matcher<T> isA(Class<T> type) {
		final Matcher<T> typeMatcher = instanceOf(type);
		return is(typeMatcher);
	}
}
