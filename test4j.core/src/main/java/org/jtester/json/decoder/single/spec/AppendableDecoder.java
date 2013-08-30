package org.jtester.json.decoder.single.spec;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;

import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.SpecTypeDecoder;

@SuppressWarnings({ "rawtypes" })
public class AppendableDecoder extends SpecTypeDecoder {
	public final static AppendableDecoder toAPPENDABLE = new AppendableDecoder();

	@Override
	protected Object decodeFromString(String value, Type type) {
		Class claz = this.getRawType(type, StringWriter.class);
		Appendable target = new StringWriter();
		if (StringBuffer.class.isAssignableFrom(claz)) {
			target = new StringBuffer();
		} else if (StringBuilder.class.isAssignableFrom(claz)) {
			target = new StringBuilder();
		}
		try {
			target.append(value);
			return target;
		} catch (IOException e) {
			String message = "can't cast value[" + value + "] to " + claz.getName();
			throw new DecoderException(message, e);
		}
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Appendable.class.isAssignableFrom(claz);
	}
}
