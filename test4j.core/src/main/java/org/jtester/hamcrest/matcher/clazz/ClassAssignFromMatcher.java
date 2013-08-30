package org.jtester.hamcrest.matcher.clazz;

import ext.jtester.hamcrest.BaseMatcher;
import ext.jtester.hamcrest.Description;

@SuppressWarnings("rawtypes")
public class ClassAssignFromMatcher extends BaseMatcher {
	private Class wanted;

	public ClassAssignFromMatcher(Class wanted) {
		this.wanted = wanted;
	}

	private Class actual = null;

	@SuppressWarnings("unchecked")
	public boolean matches(Object item) {
		if( item == null) {
			return true;
		}
		if (item instanceof Class) {
			actual = (Class) item;
		} else {
			actual = item.getClass();
		}
		if (actual == null || wanted == null) {
			return false;
		}
		return wanted.isAssignableFrom(actual);
	}

	public void describeTo(Description description) {
		description.appendText(String.format("the class[%s] isn't assignable from class[%s]", actual == null ? "<null>"
				: actual.getName(), wanted == null ? "<null>" : wanted.getName()));
	}
}
