package org.jtester.json.decoder.single;

import java.lang.reflect.Type;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ByteDecoder extends FixedTypeDecoder {
	public final static ByteDecoder toBYTE = new ByteDecoder();

	@Override
	protected Byte decodeFromString(String value) {
		try {
			return Byte.parseByte(value);
		} catch (Exception e) {
			String message = String.format("the value{%s} can't be casted to byte.", value);
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Byte.class == claz || byte.class == claz;
	}
}
