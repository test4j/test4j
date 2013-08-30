package org.jtester.hamcrest.matcher;

import static mockit.internal.util.Utilities.getField;
import mockit.internal.expectations.argumentMatching.ArgumentMatcher;
import mockit.internal.expectations.argumentMatching.ArgumentMismatch;
import ext.jtester.hamcrest.Description;
import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.StringDescription;
import ext.jtester.hamcrest.core.IsEqual;
import ext.jtester.hamcrest.core.IsSame;
import ext.jtester.hamcrest.number.OrderingComparison;

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

		while (innerMatcher instanceof ext.jtester.hamcrest.core.Is
				|| innerMatcher instanceof ext.jtester.hamcrest.core.IsNot) {
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
