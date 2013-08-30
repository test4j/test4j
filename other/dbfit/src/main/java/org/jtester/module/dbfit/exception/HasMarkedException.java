package org.jtester.module.dbfit.exception;

/**
 * 用来标记这个异常在dbfit的反馈html中已经标记了
 * 
 * @author darui.wudr
 * 
 */
public class HasMarkedException extends RuntimeException {
	private static final long serialVersionUID = 7576205074486075042L;

	public HasMarkedException() {
		super();
	}

	public HasMarkedException(String message, Throwable cause) {
		super(message, cause);
	}

	public HasMarkedException(String message) {
		super(message);
	}

	public HasMarkedException(Throwable cause) {
		super(cause);
	}
}
