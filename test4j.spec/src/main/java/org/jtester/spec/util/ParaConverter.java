package org.jtester.spec.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jtester.json.ITypeConverter;
import org.jtester.json.JSON;

public abstract class ParaConverter {

	static final String DEFAULT_COMMA = ",";
	static final Locale DEFAULT_NUMBER_FORMAT_LOCAL = Locale.ENGLISH;

	static List<ITypeConverter> converters = new ArrayList<ITypeConverter>();

	public static void addConverters(ITypeConverter... converters) {
		for (ITypeConverter converter : converters) {
			ParaConverter.converters.add(converter);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(String value, Type type) {
		for (ITypeConverter converter : converters) {
			if (converter.accept(value)) {
				Object o = converter.convert(value);
				return (T) o;
			}
		}
		if (type == String.class) {
			return (T) getStringValue(value);
		} else {
			Object o = JSON.toObject(value, type);
			return (T) o;
		}
	}

	static String getStringValue(String value) {
		if (value == null) {
			return null;
		}
		if (value.startsWith("\"") && value.endsWith("\"")) {
			return value.substring(1, value.length() - 1);
		}
		if (value.startsWith("'") && value.endsWith("'")) {
			return value.substring(1, value.length() - 1);
		} else {
			return value;
		}
	}
}
