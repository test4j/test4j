package ext.jtester.hamcrest.object;

import static ext.jtester.hamcrest.MatcherAssert.assertThat;
import static ext.jtester.hamcrest.core.IsEqual.equalTo;
import static ext.jtester.hamcrest.core.IsNot.not;
import static ext.jtester.hamcrest.object.HasToString.hasToString;

import org.testng.annotations.Test;

import ext.jtester.hamcrest.AbstractMatcherTest;
import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.StringDescription;

public class HasToStringTest extends AbstractMatcherTest {
	private static final String TO_STRING_RESULT = "toString result";
	private static final Object ARG = new Object() {
		@Override
		public String toString() {
			return TO_STRING_RESULT;
		}
	};

	@Override
	protected Matcher<?> createMatcher() {
		return hasToString(equalTo("irrelevant"));
	}

	@Test
	public void testPassesResultOfToStringToNestedMatcher() {
		assertThat(ARG, hasToString(equalTo(TO_STRING_RESULT)));
		assertThat(ARG, not(hasToString(equalTo("OTHER STRING"))));
	}

	@Test
	public void testProvidesConvenientShortcutForHasToStringEqualTo() {
		assertThat(ARG, hasToString(TO_STRING_RESULT));
		assertThat(ARG, not(hasToString("OTHER STRING")));
	}

	@Test
	public void testHasReadableDescription() {
		Matcher<? super String> toStringMatcher = equalTo(TO_STRING_RESULT);
		Matcher<Matcher<String>> matcher = hasToString(toStringMatcher);

		want.string(descriptionOf(matcher)).isEqualTo("with toString() " + descriptionOf(toStringMatcher));
	}

	@Test
	public void testMismatchContainsToStringValue() {
		String expectedMismatchString = "toString() was \"Cheese\"";
		assertMismatchDescription(expectedMismatchString, hasToString(TO_STRING_RESULT), "Cheese");
	}

	private String descriptionOf(Matcher<?> matcher) {
		return StringDescription.asString(matcher);
	}
}
