package org.jtester.json.decoder.single;

import java.lang.reflect.Type;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FloatDecoder extends FixedTypeDecoder {
	public final static FloatDecoder toFLOAT = new FloatDecoder();

	@Override
	protected Float decodeFromString(String value) {
		try {
			value = value.trim();
			if (value.endsWith("f") || value.endsWith("F")) {
				value = value.substring(0, value.length() - 1);
			}
			return Float.parseFloat(value);
		} catch (Exception e) {
			String message = String.format("the value{%s} can't be casted to float number.", value);
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Float.class.equals(claz) || float.class.equals(claz);
	}
}
