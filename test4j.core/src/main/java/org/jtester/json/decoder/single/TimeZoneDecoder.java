package org.jtester.json.decoder.single;

import java.lang.reflect.Type;
import java.util.TimeZone;

import org.jtester.json.decoder.base.FixedTypeDecoder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TimeZoneDecoder extends FixedTypeDecoder {

	public final static TimeZoneDecoder toTimeZone = new TimeZoneDecoder();

	@Override
	protected TimeZone decodeFromString(String value) {
		TimeZone timezone = TimeZone.getTimeZone(value.trim());
		return timezone;
	}

	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return TimeZone.class.isAssignableFrom(claz);
	}
}
