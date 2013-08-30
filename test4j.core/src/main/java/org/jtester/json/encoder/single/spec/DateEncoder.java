package org.jtester.json.encoder.single.spec;

import java.io.Writer;
import java.util.Date;

import org.jtester.json.encoder.single.SpecTypeEncoder;
import org.jtester.json.encoder.single.fixed.StringEncoder;
import org.jtester.tools.commons.DateHelper;

@SuppressWarnings("rawtypes")
public class DateEncoder<T extends Date> extends SpecTypeEncoder<T> {
	public static DateEncoder instance = new DateEncoder();

	private DateEncoder() {
		super(Date.class);
	}

	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		String df = dateFormat == null ? "yyyy-MM-dd HH:mm:ss" : dateFormat;
		String date = DateHelper.toDateTimeStr(target, df);

		writer.append(quote_Char);
		StringEncoder.writeEscapeString(date, writer);
		writer.append(quote_Char);
	}

	@Override
	protected void encodeOtherProperty(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub
	}

	private static String dateFormat = null;

	public static void setDateFormat(String format) {
		dateFormat = format;
	}
}
