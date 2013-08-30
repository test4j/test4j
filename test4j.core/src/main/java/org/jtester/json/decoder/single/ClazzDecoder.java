package org.jtester.json.decoder.single;

import java.lang.reflect.Type;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ClazzDecoder extends FixedTypeDecoder {
	public static final ClazzDecoder toCLASS = new ClazzDecoder();

	@Override
	protected Class decodeFromString(String value) {
		value = value.trim();
		try {
			Class clazz = Class.forName(value);
			return clazz;
		} catch (ClassNotFoundException e) {
			throw new DecoderException(e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Class.class.equals(claz);
	}
}
