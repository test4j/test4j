package org.jtester.hamcrest.matcher.mockito;

public class GreaterOrEqual<T extends Comparable<T>> extends CompareTo<T> {

	public GreaterOrEqual(Comparable<T> value) {
		super(value);
	}

	@Override
	protected String getName() {
		return "geq";
	}

	@Override
	protected boolean matchResult(int result) {
		return result >= 0;
	}
}
