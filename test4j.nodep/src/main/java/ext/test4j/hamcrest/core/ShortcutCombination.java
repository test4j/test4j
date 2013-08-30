package ext.test4j.hamcrest.core;

import ext.test4j.hamcrest.BaseMatcher;
import ext.test4j.hamcrest.Description;
import ext.test4j.hamcrest.Matcher;

@SuppressWarnings("rawtypes")
abstract class ShortcutCombination<T> extends BaseMatcher<T> {

	private final Iterable<Matcher> matchers;

	public ShortcutCombination(Iterable<Matcher> matchers) {
		this.matchers = matchers;
	}

	public abstract boolean matches(Object o);

	public abstract void describeTo(Description description);

	protected boolean matches(Object o, boolean shortcut) {
		for (Matcher matcher : matchers) {
			if (matcher.matches(o) == shortcut) {
				return shortcut;
			}
		}
		return !shortcut;
	}

	public void describeTo(Description description, String operator) {
		description.appendList("(", " " + operator + " ", ")", matchers);
	}
}
