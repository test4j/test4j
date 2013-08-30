package org.jtester.json.decoder.base;

public class DecoderException extends RuntimeException {

	private static final long serialVersionUID = 5079112932522963488L;

	public DecoderException(Throwable t) {
		super(t);
	}

	public DecoderException(String string) {
		super(string);
	}

	public DecoderException(String string, Throwable e) {
		super(string, e);
	}
}
