package ext.test4j.hamcrest.core;

import java.util.Arrays;

import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.DiagnosingMatcher;
import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.Matcher;

/**
 * Calculates the logical conjunction of multiple matchers. Evaluation is
 * shortcut, so subsequent matchers are not called if an earlier matcher returns
 * <code>false</code>.
 */
@SuppressWarnings("rawtypes")
public class AllOf<T> extends DiagnosingMatcher<T> {

	private final Iterable<Matcher> matchers;

	public AllOf(Iterable<Matcher> matchers) {
		this.matchers = matchers;
	}

	@Override
	public boolean matches(Object o, Description mismatchDescription) {
		for (Matcher matcher : matchers) {
			if (!matcher.matches(o)) {
				mismatchDescription.appendDescriptionOf(matcher).appendText(" ");
				matcher.describeMismatch(o, mismatchDescription);
				return false;
			}
		}
		return true;
	}

	public void describeTo(Description description) {
		description.appendList("(", " " + "and" + " ", ")", matchers);
	}

	/**
	 * Evaluates to true only if ALL of the passed in matchers evaluate to true.
	 */
	@Factory
	public static <T> Matcher<T> allOf(Iterable<Matcher> matchers) {
		return new AllOf<T>(matchers);
	}

	/**
	 * Evaluates to true only if ALL of the passed in matchers evaluate to true.
	 */
	@Factory
	public static <T> Matcher<T> allOf(Matcher... matchers) {
		return allOf(Arrays.asList(matchers));
	}

	@SuppressWarnings("unchecked")
	@Factory
	public static <T> Matcher<T> notAll(Iterable<Matcher> matchers) {
		Matcher matcher = IsNot.not(AllOf.allOf(matchers));
		return matcher;
	}
}
