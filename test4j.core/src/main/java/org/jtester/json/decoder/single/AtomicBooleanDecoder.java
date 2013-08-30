package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AtomicBooleanDecoder extends FixedTypeDecoder {
	public final static AtomicBooleanDecoder toATOMICBOOLEAN = new AtomicBooleanDecoder();

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return AtomicBoolean.class.isAssignableFrom(claz);
	}

	@Override
	protected AtomicBoolean decodeFromString(String value) {
		boolean bool = BooleanDecoder.toBoolean(value);
		return new AtomicBoolean(bool);
	}
}
