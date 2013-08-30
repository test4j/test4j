package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.util.Locale;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LocaleDecoder extends FixedTypeDecoder {
	public final static LocaleDecoder toLOCAL = new LocaleDecoder();

	@Override
	protected Locale decodeFromString(String value) {
		String[] parts = value.split("_");

		if (parts.length == 1) {
			return new Locale(parts[0]);
		}

		if (parts.length == 2) {
			return new Locale(parts[0], parts[1]);
		}

		return new Locale(parts[0], parts[1], parts[2]);
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Locale.class.isAssignableFrom(claz);
	}
}
