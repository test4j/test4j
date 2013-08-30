package org.jtester.module.dbfit.exception;

/**
 * 
 * @author darui.wudr
 * 
 */
public class FitRunException extends RuntimeException {
	private static final long serialVersionUID = 1825615922992281543L;

	public FitRunException() {
		super();
	}

	public FitRunException(String message, Throwable cause) {
		super(message, cause);
	}

	public FitRunException(String message) {
		super(message);
	}

	public FitRunException(Throwable cause) {
		super(cause);
	}
}
