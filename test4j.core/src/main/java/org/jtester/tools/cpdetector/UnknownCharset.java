package org.jtester.tools.cpdetector;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class UnknownCharset extends Charset {
	private static Charset instance;

	private UnknownCharset() {
		super("void", null);
	}

	public static Charset getInstance() {
		if (instance == null) {
			instance = new UnknownCharset();
		}
		return instance;
	}

	public boolean contains(Charset cs) {
		return false;
	}

	public CharsetDecoder newDecoder() {
		throw new UnsupportedOperationException("This is no real Charset but a flag you should test for!");
	}

	public CharsetEncoder newEncoder() {
		throw new UnsupportedOperationException("This is no real Charset but a flag you should test for!");
	}
}
