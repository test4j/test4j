package org.test4j.hamcrest.matcher.modes;

public enum MatchMode {
	/**
	 * the items(any or all, specified by {@link ItemsMode}) should match all
	 * matchers.
	 */
	MatchAny,
	/**
	 * the items(any or all, specified by {@link ItemsMode}) should match any
	 * matchers.
	 */
	MatchAll;
}
