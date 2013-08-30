package org.jtester.tools.cpdetector;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UnsupportedCharset extends Charset {
	private static Map<String, Charset> singletons = new HashMap<String, Charset>();

	private UnsupportedCharset(String name) {
		super(name, null);
	}

	public static Charset forName(String name) {
		Charset ret = (Charset) singletons.get(name);
		if (ret == null) {
			ret = new UnsupportedCharset(name);
			singletons.put(name, ret);
		}
		return ret;
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

	public String displayName() {
		return super.displayName();
	}

	public String displayName(Locale locale) {
		return super.displayName(locale);
	}
}
