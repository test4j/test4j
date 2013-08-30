package org.jtester.fortest.reflector;

public class TestException extends Exception {
	private static final long serialVersionUID = -3003266231421877135L;

	public TestException() {
	}

	public TestException(String message) {
		super(message);
	}

	public TestException(Throwable cause) {
		super(cause);
	}

	public TestException(String message, Throwable cause) {
		super(message, cause);
	}
}
