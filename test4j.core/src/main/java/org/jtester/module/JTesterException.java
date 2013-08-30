package org.jtester.module;

public class JTesterException extends RuntimeException {
	private static final long serialVersionUID = -5975690696261330680L;

	public JTesterException(String message) {
		super(message);
	}

	public JTesterException(Throwable throwable) {
		super(throwable);
	}

	public JTesterException(String error, Throwable throwable) {
		super(error, throwable);
	}
}
