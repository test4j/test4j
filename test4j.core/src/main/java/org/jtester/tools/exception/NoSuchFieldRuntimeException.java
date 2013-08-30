package org.jtester.tools.exception;

public class NoSuchFieldRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -4445906138048916133L;

	public NoSuchFieldRuntimeException() {
		super();
	}

	public NoSuchFieldRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchFieldRuntimeException(String message) {
		super(message);
	}

	public NoSuchFieldRuntimeException(Throwable cause) {
		super(cause);
	}
}
