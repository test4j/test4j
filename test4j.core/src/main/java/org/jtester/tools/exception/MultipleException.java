package org.jtester.tools.exception;

import java.util.List;

public class MultipleException extends RuntimeException {
	private static final long serialVersionUID = 7060749249322672700L;

	private StringBuffer message = new StringBuffer();

	public MultipleException() {
		super();
	}

	public MultipleException(String message, Throwable cause) {
		super(message, cause);
	}

	public MultipleException(String message) {
		super(message);
	}

	public MultipleException(Throwable cause) {
		super(cause);
	}

	public MultipleException(Throwable cause, Throwable... more) {
		super(cause);
		for (Throwable item : more) {
			this.addException(item);
		}
	}

	public MultipleException(String message, Throwable cause, Throwable... more) {
		super(message, cause);
		for (Throwable item : more) {
			this.addException(item);
		}
	}

	public void addException(Throwable e) {
		Throwable caused = e;
		while (caused != null) {
			message.append("\n").append(e.getClass().getName()).append(": ").append(e.getLocalizedMessage());
			caused = caused.getCause();
		}
	}

	public void addException(List<Throwable> list) {
		for (Throwable item : list) {
			addException(item);
		}
	}

	@Override
	public String getMessage() {
		return super.getMessage() + message.toString();
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage() + message.toString();
	}

	public static MultipleException getMultipleException(Throwable... more) {
		Throwable caused = null;
		for (Throwable item : more) {
			if (item != null) {
				caused = item;
				break;
			}
		}
		if (caused == null) {
			return null;
		}
		MultipleException exception = new MultipleException(caused);
		for (Throwable item : more) {
			exception.addException(item);
		}
		return exception;
	}
}
