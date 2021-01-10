package org.test4j.exception;

public class MultipleException extends RuntimeException {
	private static final long serialVersionUID = 7060749249322672700L;

	private StringBuffer message = new StringBuffer();

	public MultipleException(Throwable cause) {
		super(cause);
	}

	public void addException(Throwable e) {
		Throwable caused = e;
		while (caused != null) {
			message.append("\n").append(e.getClass().getName()).append(": ").append(e.getLocalizedMessage());
			caused = caused.getCause();
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