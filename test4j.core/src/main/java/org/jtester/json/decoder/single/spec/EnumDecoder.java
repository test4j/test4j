package org.jtester.json.decoder.single.spec;

import java.lang.reflect.Type;

import org.jtester.json.decoder.base.SpecTypeDecoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EnumDecoder extends SpecTypeDecoder {
	public final static EnumDecoder toENUM = new EnumDecoder();

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return claz.isEnum();
	}

	@Override
	protected Object decodeFromString(String value, Type type) {
		Class claz = this.getRawType(type, null);
		Enum enumValue = Enum.valueOf(claz, value);
		return enumValue;
	}
}
