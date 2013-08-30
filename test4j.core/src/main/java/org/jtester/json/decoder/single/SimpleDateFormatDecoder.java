package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleDateFormatDecoder extends FixedTypeDecoder {
	public final static SimpleDateFormatDecoder toSIMPLEDATEFORMAT = new SimpleDateFormatDecoder();

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return SimpleDateFormat.class.isAssignableFrom(claz);
	}

	@Override
	protected <T> T decodeFromString(String value) {
		Locale locale = Locale.getDefault();
		SimpleDateFormat target = new SimpleDateFormat(value, locale);

		return (T) target;
	}
}
