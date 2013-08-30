package org.jtester.json.decoder.single;

import java.lang.reflect.Type;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ShortDecoder extends FixedTypeDecoder {

	public final static ShortDecoder toSHORT = new ShortDecoder();

	@Override
	protected Short decodeFromString(String value) {
		try {
			value = value.trim();
			return Short.parseShort(value);
		} catch (Exception e) {
			String message = String.format("the value{%s} can't be casted to short number.", value);
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Short.class.equals(claz) || short.class.equals(claz);
	}
}
