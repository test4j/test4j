package org.jtester.json.decoder.single;

import java.lang.reflect.Type;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LongDecoder extends FixedTypeDecoder {

	public final static LongDecoder toLONG = new LongDecoder();

	@Override
	protected Long decodeFromString(String value) {
		try {
			value = value.trim();
			if (value.endsWith("l") || value.endsWith("L")) {
				value = value.substring(0, value.length() - 1);
			}
			return Long.parseLong(value);
		} catch (Exception e) {
			String message = String.format("the value{%s} can't be casted to long number.", value);
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Long.class.equals(claz) || long.class.equals(claz);
	}
}
