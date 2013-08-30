package org.test4j.json;

public class JSONException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JSONException() {
		super();
	}

	public JSONException(Throwable e) {
		super(e);
	}

	public JSONException(String message) {
		super(message);
	}

	public JSONException(String message, Throwable cause) {
		super(message, cause);
	}
}
