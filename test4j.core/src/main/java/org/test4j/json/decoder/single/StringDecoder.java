package org.test4j.json.decoder.single;

import java.lang.reflect.Type;

import org.test4j.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class StringDecoder extends FixedTypeDecoder {
	public static StringDecoder toSTRING = new StringDecoder();

	@Override
	protected String decodeFromString(String value) {
		return value;
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return String.class.isAssignableFrom(claz);
	}
}
