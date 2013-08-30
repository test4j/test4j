package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BigDecimalDecoder extends FixedTypeDecoder {

	public final static BigDecimalDecoder toBIGDECIMAL = new BigDecimalDecoder();

	@Override
	protected BigDecimal decodeFromString(String value) {
		try {
			value = value.trim();
			return new BigDecimal(value);
		} catch (Exception e) {
			String message = String.format("the value{%s} can't be casted to BigDecimal.", value);
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return BigDecimal.class.isAssignableFrom(claz);
	}
}
