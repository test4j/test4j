package org.jtester.hamcrest.matcher.mockito;

public class LessThan<T extends Comparable<T>> extends CompareTo<T> {

	public LessThan(Comparable<T> value) {
		super(value);
	}

	@Override
	protected String getName() {
		return "lt";
	}

	@Override
	protected boolean matchResult(int result) {
		return result < 0;
	}
}
