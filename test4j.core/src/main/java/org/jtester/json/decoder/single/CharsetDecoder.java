package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CharsetDecoder extends FixedTypeDecoder {

	public final static CharsetDecoder toCHARSET = new CharsetDecoder();

	@Override
	protected Charset decodeFromString(String value) {
		Charset charset = Charset.forName(value);
		return charset;
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Charset.class.isAssignableFrom(claz);
	}
}
