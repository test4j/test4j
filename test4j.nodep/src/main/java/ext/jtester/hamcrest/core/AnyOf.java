package ext.jtester.hamcrest.core;

import java.util.Arrays;

import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.Factory;
import ext.jtester.hamcrest.Matcher;

/**
 * Calculates the logical disjunction of multiple matchers. Evaluation is
 * shortcut, so subsequent matchers are not called if an earlier matcher returns
 * <code>true</code>.
 */
@SuppressWarnings({ "rawtypes" })
public class AnyOf<T> extends ShortcutCombination<T> {

	public AnyOf(Iterable<Matcher> matchers) {
		super(matchers);
	}

	@Override
	public boolean matches(Object o) {
		return matches(o, true);
	}

	@Override
	public void describeTo(Description description) {
		describeTo(description, "or");
	}

	/**
	 * Evaluates to true if ANY of the passed in matchers evaluate to true.
	 */
	@Factory
	public static <T> AnyOf<T> anyOf(Iterable<Matcher> matchers) {
		return new AnyOf<T>(matchers);
	}

	/**
	 * Evaluates to true if ANY of the passed in matchers evaluate to true.
	 */
	@Factory
	public static <T> AnyOf<T> anyOf(Matcher... matchers) {
		return anyOf(Arrays.asList(matchers));
	}

	@Factory
	public static <T> Matcher notAny(Iterable<Matcher> matchers) {
		Matcher matcher = IsNot.not(AnyOf.anyOf(matchers));
		return matcher;
	}
}
