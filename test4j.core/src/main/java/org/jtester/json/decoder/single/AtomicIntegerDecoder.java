package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AtomicIntegerDecoder extends FixedTypeDecoder {
	public final static AtomicIntegerDecoder toATOMICINTEGER = new AtomicIntegerDecoder();

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return AtomicInteger.class.isAssignableFrom(claz);
	}

	@Override
	protected AtomicInteger decodeFromString(String value) {
		int newValue = Integer.parseInt(value);
		return new AtomicInteger(newValue);
	}
}
