package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicLong;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AtomicLongDecoder extends FixedTypeDecoder {
	public final static AtomicLongDecoder toATOMICLONG = new AtomicLongDecoder();

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return AtomicLong.class.isAssignableFrom(claz);
	}

	@Override
	protected AtomicLong decodeFromString(String value) {
		long newValue = Long.parseLong(value);
		return new AtomicLong(newValue);
	}
}
