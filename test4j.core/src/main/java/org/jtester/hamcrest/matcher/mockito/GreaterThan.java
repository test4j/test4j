package org.jtester.hamcrest.matcher.mockito;

public class GreaterThan<T extends Comparable<T>> extends CompareTo<T> {

	public GreaterThan(Comparable<T> value) {
		super(value);
	}

	@Override
	protected String getName() {
		return "gt";
	}

	@Override
	protected boolean matchResult(int result) {
		return result > 0;
	}
}
