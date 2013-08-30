package org.jtester.tools.exception;

public class NoSuchMethodRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1046092406929023981L;

	public NoSuchMethodRuntimeException() {
		super();
	}

	public NoSuchMethodRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchMethodRuntimeException(String message) {
		super(message);
	}

	public NoSuchMethodRuntimeException(Throwable cause) {
		super(cause);
	}
}
