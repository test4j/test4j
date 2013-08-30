package org.test4j.json.decoder.single;

import java.lang.reflect.Type;

import org.test4j.json.decoder.base.DecoderException;
import org.test4j.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IntegerDecoder extends FixedTypeDecoder {
	public final static IntegerDecoder toINTEGER = new IntegerDecoder();

	@Override
	protected Integer decodeFromString(String value) {
		try {
			value = value.trim();
			return Integer.parseInt(value);
		} catch (Exception e) {
			String message = String.format("the value{%s} can't be casted to integer number.", value);
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Integer.class.equals(claz) || int.class.equals(claz);
	}
}
