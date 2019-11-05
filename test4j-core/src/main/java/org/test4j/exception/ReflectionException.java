package org.test4j.exception;

public class ReflectionException extends RuntimeException {
	private static final long serialVersionUID = 1421565945736042586L;

	public ReflectionException() {
		super();
	}

	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectionException(String message) {
		super(message);
	}

	public ReflectionException(Throwable cause) {
		super(cause);
	}
}
