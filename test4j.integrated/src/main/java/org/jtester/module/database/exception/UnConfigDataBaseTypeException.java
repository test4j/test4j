package org.jtester.module.database.exception;

public class UnConfigDataBaseTypeException extends RuntimeException {

	private static final long serialVersionUID = -3029001538945051694L;

	public UnConfigDataBaseTypeException() {
		super();
	}

	public UnConfigDataBaseTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnConfigDataBaseTypeException(String message) {
		super(message);
	}

	public UnConfigDataBaseTypeException(Throwable cause) {
		super(cause);
	}
}
