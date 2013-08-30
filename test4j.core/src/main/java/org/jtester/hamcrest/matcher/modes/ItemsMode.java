package org.jtester.hamcrest.matcher.modes;

public enum ItemsMode {
	/**
	 * the all items should match matchers( any of all, specified by
	 * {@link MatchMode}).
	 */
	AllItems,
	/**
	 * the any items should match matchers( any of all, specified by
	 * {@link MatchMode}).
	 */
	AnyItems;
}
