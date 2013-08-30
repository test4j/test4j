package org.jtester.json.decoder.single;

import java.lang.reflect.Type;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DoubleDecoder extends FixedTypeDecoder {

	public final static DoubleDecoder toDOUBLE = new DoubleDecoder();

	@Override
	protected Double decodeFromString(String value) {
		value = value.trim();
		try {
			value = value.trim();
			if (value.endsWith("d") || value.endsWith("D")) {
				value = value.substring(0, value.length() - 1);
			}
			return Double.parseDouble(value);
		} catch (Exception e) {
			String message = String.format("the value{%s} can't be casted to double number.", value);
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Double.class.equals(claz) || double.class.equals(claz);
	}
}
