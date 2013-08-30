package org.jtester.spec.exceptions;

public class SkipStepException extends RuntimeException {
	private static final long serialVersionUID = 1901320510294924470L;

	public static final SkipStepException instance = new SkipStepException();

	private SkipStepException() {
	}
}
