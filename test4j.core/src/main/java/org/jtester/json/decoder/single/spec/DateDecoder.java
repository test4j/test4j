package org.jtester.json.decoder.single.spec;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jtester.json.decoder.base.SpecTypeDecoder;
import org.jtester.tools.commons.DateHelper;

@SuppressWarnings({ "rawtypes" })
public class DateDecoder extends SpecTypeDecoder {
	public final static DateDecoder toDATE = new DateDecoder();

	private static long getTime(String value) {
		if (value.matches("\\d+[Ll]?")) {
			long time = Long.valueOf(value.replaceAll("[Ll]", ""));
			return time;
		}
		if (dateFormat == null) {
			Date date = DateHelper.parse(value);
			return date.getTime();
		}
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			Date date = df.parse(value);
			return date.getTime();
		} catch (ParseException e) {
			String message = "";
			throw new RuntimeException(message, e);
		}
	}

	private static String dateFormat = null;

	public static void setDateFormat(String format) {
		dateFormat = format;
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Date.class.isAssignableFrom(claz);
	}

	@Override
	protected Object decodeFromString(String value, Type type) {
		long time = getTime(value);
		Class claz = this.getRawType(type, null);
		if (java.sql.Date.class.isAssignableFrom(claz)) {
			return new java.sql.Date(time);
		}
		if (java.sql.Time.class.isAssignableFrom(claz)) {
			return new java.sql.Time(time);
		}
		if (java.sql.Timestamp.class.isAssignableFrom(claz)) {
			return new java.sql.Timestamp(time);
		}
		return new Date(time);
	}
}
