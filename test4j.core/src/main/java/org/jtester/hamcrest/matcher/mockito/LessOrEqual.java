package org.jtester.hamcrest.matcher.mockito;

public class LessOrEqual<T extends Comparable<T>> extends CompareTo<T> {

	public LessOrEqual(Comparable<T> value) {
		super(value);
	}

	@Override
	protected String getName() {
		return "leq";
	}

	@Override
	protected boolean matchResult(int result) {
		return result <= 0;
	}
}
