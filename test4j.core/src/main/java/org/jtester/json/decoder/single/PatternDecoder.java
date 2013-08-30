package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PatternDecoder extends FixedTypeDecoder {
	public final static PatternDecoder toPATTERN = new PatternDecoder();

	@Override
	protected Pattern decodeFromString(String value) {
		Pattern pattern = Pattern.compile(value);
		return pattern;
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Pattern.class.isAssignableFrom(claz);
	}
}
