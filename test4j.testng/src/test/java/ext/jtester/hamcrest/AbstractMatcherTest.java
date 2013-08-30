/*  Copyright (c) 2000-2006 hamcrest.org
 */
package ext.jtester.hamcrest;

import org.jtester.module.ICore;
import org.testng.annotations.Test;

public abstract class AbstractMatcherTest implements ICore {

	/**
	 * Create an instance of the Matcher so some generic safety-net tests can be
	 * run on it.
	 */
	protected abstract Matcher<?> createMatcher();

	public <T> void assertMatches(String message, Matcher<? super T> c, T arg) {
		want.bool(c.matches(arg)).is(message, true);
	}

	public static <T> void assertDoesNotMatch(String message, Matcher<? super T> c, T arg) {
		want.bool(c.matches(arg)).is(message, false);
	}

	public static void assertDescription(String expected, Matcher<?> matcher) {
		Description description = new StringDescription();
		description.appendDescriptionOf(matcher);
		want.string(description.toString()).isEqualTo("Expected description", expected);
	}

	public static <T> void assertMismatchDescription(String expected, Matcher<? super T> matcher, T arg) {
		Description description = new StringDescription();
		want.bool(matcher.matches(arg)).is("Precondtion: Matcher should not match item.", false);
		matcher.describeMismatch(arg, description);
		want.string(description.toString()).isEqualTo("Expected mismatch description", expected);
	}

	@Test
	public void testIsNullSafe() {
		// should not throw a NullPointerException
		createMatcher().matches(null);
	}

	@Test
	public void testCopesWithUnknownTypes() {
		// should not throw ClassCastException
		createMatcher().matches(new UnknownType());
	}

	public static class UnknownType {
	}

}
