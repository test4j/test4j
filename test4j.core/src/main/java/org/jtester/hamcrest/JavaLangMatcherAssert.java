package org.jtester.hamcrest;

import ext.jtester.hamcrest.Matcher;

/**
 * 兼容Java " assert true/false "语法<br>
 * Integration method for use with Java's <code>assert</code> keyword. Example:
 * 
 * <pre>
 * assert that(&quot;Foo&quot;, startsWith(&quot;f&quot;));
 * </pre>
 */
public class JavaLangMatcherAssert {
	private JavaLangMatcherAssert() {
	};

	@SuppressWarnings("rawtypes")
	public static <T> boolean that(T argument, Matcher matcher) {
		return matcher.matches(argument);
	}
}
