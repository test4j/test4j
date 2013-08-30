package org.jtester.module.dbfit.exception;

public class DbFitException extends RuntimeException {
	private static final long serialVersionUID = 2467680894577167851L;

	public DbFitException() {
		super();
	}

	public DbFitException(String message, Throwable cause) {
		super(message, cause);
	}

	public DbFitException(String message) {
		super(message);
	}

	public DbFitException(Throwable cause) {
		super(cause);
	}

}
