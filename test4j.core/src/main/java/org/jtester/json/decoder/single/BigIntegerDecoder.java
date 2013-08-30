package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.math.BigInteger;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BigIntegerDecoder extends FixedTypeDecoder {

	public final static BigIntegerDecoder toBIGINTEGER = new BigIntegerDecoder();

	@Override
	protected BigInteger decodeFromString(String value) {
		try {
			value = value.trim();
			return new BigInteger(value);
		} catch (Exception e) {
			String message = String.format("the value{%s} can't be casted to BigInteger.", value);
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return BigInteger.class.isAssignableFrom(claz);
	}
}
