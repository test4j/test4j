package org.test4j.json.decoder.single;

import java.lang.reflect.Type;

import org.test4j.json.decoder.base.DecoderException;
import org.test4j.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CharDecoder extends FixedTypeDecoder {
	public final static CharDecoder toCHARACTER = new CharDecoder();

	@Override
	protected Character decodeFromString(String value) {
		if (value.length() != 1) {
			String message = String.format("the value{%s} can't be casted to byte.", value);
			throw new DecoderException(message);
		} else {
			return value.charAt(0);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return char.class == claz || Character.class.equals(claz);
	}
}
