package org.test4j.exception;

public class Test4JException extends RuntimeException {
	private static final long serialVersionUID = -5975690696261330680L;

	public Test4JException(String message) {
		super(message);
	}

	public Test4JException(Throwable throwable) {
		super(throwable);
	}

	public Test4JException(String error, Throwable throwable) {
		super(error, throwable);
	}
}
