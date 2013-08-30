package org.jtester.hamcrest.matcher;

import static mockit.internal.util.Utilities.getField;
import mockit.internal.expectations.argumentMatching.ArgumentMatcher;
import mockit.internal.expectations.argumentMatching.ArgumentMismatch;
import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.Matcher;
import ext.test4j.hamcrest.StringDescription;
import ext.test4j.hamcrest.core.IsEqual;
import ext.test4j.hamcrest.core.IsSame;
import ext.test4j.hamcrest.number.OrderingComparison;

/**
 * Adapts the<br>
 * <br>
 * {@code ext.jtester.hamcrest.Matcher} interface to
 * {@link mockit.external.hamcrest.Matcher}.
 */
@SuppressWarnings({ "rawtypes" })
public final class JMockitAdapter implements ArgumentMatcher {
	private final Matcher hamcrestMatcher;

	public static JMockitAdapter create(final Matcher matcher) {
		return new JMockitAdapter(matcher);
	}

	private JMockitAdapter(Matcher matcher) {
		hamcrestMatcher = matcher;
	}

	public boolean matches(Object item) {
		return hamcrestMatcher.matches(item);
	}

	public void writeMismatchPhrase(ArgumentMismatch description) {
		Description strDescription = new StringDescription();
		hamcrestMatcher.describeTo(strDescription);
		description.append(strDescription.toString());
	}

	public Object getInnerValue() {
		Matcher innerMatcher = hamcrestMatcher;

		while (innerMatcher instanceof ext.test4j.hamcrest.core.Is
				|| innerMatcher instanceof ext.test4j.hamcrest.core.IsNot) {
			innerMatcher = getField(innerMatcher.getClass(), Matcher.class, innerMatcher);
		}

		if (innerMatcher instanceof IsEqual || innerMatcher instanceof IsSame
				|| innerMatcher instanceof OrderingComparison) {
			return getField(innerMatcher.getClass(), Object.class, innerMatcher);
		} else {
			return null;
		}
	}
}
