package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.util.TimeZone;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class TimeZoneEncoder extends FixedTypeEncoder<TimeZone> {

	public static TimeZoneEncoder instance = new TimeZoneEncoder();

	private TimeZoneEncoder() {
		super(TimeZone.class);
	}

	@Override
	protected void encodeSingleValue(TimeZone target, Writer writer) throws Exception {
		String timezone = target.getID();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(timezone, writer);
		writer.append(quote_Char);
	}
}
