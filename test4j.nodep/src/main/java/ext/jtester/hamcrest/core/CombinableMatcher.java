package ext.jtester.hamcrest.core;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.Factory;
import ext.jtester.hamcrest.Matcher;

import java.util.ArrayList;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CombinableMatcher<T> extends BaseMatcher<T> {
	private final Matcher<? super T> matcher;

	public CombinableMatcher(Matcher<? super T> matcher) {
		this.matcher = matcher;
	}

	public boolean matches(Object item) {
		return matcher.matches(item);
	}

	public void describeTo(Description description) {
		description.appendDescriptionOf(matcher);
	}

	public CombinableMatcher<T> and(Matcher other) {
		return new CombinableMatcher<T>(new AllOf(templatedListWith(other)));
	}

	public CombinableMatcher<T> or(Matcher other) {
		return new CombinableMatcher<T>(new AnyOf<T>(templatedListWith(other)));
	}

	private ArrayList<Matcher> templatedListWith(Matcher other) {
		ArrayList<Matcher> matchers = new ArrayList<Matcher>();
		matchers.add(matcher);
		matchers.add(other);
		return matchers;
	}

	/**
	 * This is useful for fluently combining matchers that must both pass. For
	 * example:
	 * 
	 * <pre>
	 * assertThat(string, both(containsString(&quot;a&quot;)).and(containsString(&quot;b&quot;)));
	 * </pre>
	 */
	@Factory
	public static <LHS> CombinableMatcher<LHS> both(Matcher<? super LHS> matcher) {
		return new CombinableMatcher<LHS>(matcher);
	}

	/**
	 * This is useful for fluently combining matchers where either may pass, for
	 * example:
	 * 
	 * <pre>
	 * assertThat(string, both(containsString(&quot;a&quot;)).and(containsString(&quot;b&quot;)));
	 * </pre>
	 */
	@Factory
	public static <LHS> CombinableMatcher<LHS> either(Matcher<? super LHS> matcher) {
		return new CombinableMatcher<LHS>(matcher);
	}
}