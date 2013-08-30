package org.jtester.datafilling.exceptions;

@SuppressWarnings("serial")
public class PoJoFillException extends RuntimeException {
	/**
	 * Full constructor
	 * 
	 * @param message
	 *            The exception message
	 * @param cause
	 *            The error which caused this exception to be thrown
	 */
	public PoJoFillException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with message
	 * 
	 * @param message
	 *            The exception message
	 */
	public PoJoFillException(String message) {
		super(message);
	}
}
