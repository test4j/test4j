package org.test4j.tools.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	/**
	 * 返回当前日期的默认格式("yyyy-MM-dd")字符串
	 * 
	 * @return
	 */
	public static final String currDateStr() {
		return toDateTimeStr(now(), "yyyy-MM-dd");
	}

	/**
	 * 返回当前日期时间的默认格式("yyyy-MM-dd mm:hh:SS")字符串
	 * 
	 * @return
	 */
	public static final String currDateTimeStr() {
		return toDateTimeStr(now(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回当前时间的格式化字符串
	 * 
	 * @param format
	 * @return
	 */
	public static final String currDateTimeStr(String format) {
		return toDateTimeStr(now(), format);
	}

	/**
	 * 返回指定时间的格式化字符串
	 * 
	 * @param format
	 * @return
	 */
	public static final String toDateTimeStr(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static final Date now() {
		return new Date();
	}

	private static SimpleDateFormat df_default = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static SimpleDateFormat df_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat df_time = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");

	// private static SimpleDateFormat df_year = new SimpleDateFormat("yyyy");

	public static final SimpleDateFormat getDateFormat(String input) {
		if (input == null) {
			return df_default;
		}
		// if (input.matches("\\s*\\d{4}\\s*")) {
		// return df_year;
		// } else
		if (input.matches("\\s*\\d{4}-\\d{1,2}-\\d{1,2}\\s*")) {
			return df_date;
		} else if (input.matches("\\s*\\d{2,4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\s*")) {
			return df_datetime;
		} else if (input.matches("\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}\\s*")) {
			return df_time;
		} else {
			return df_default;
		}
	}

	public static final Date parse(String str) {
		if (StringHelper.isBlankOrNull(str)) {
			throw new RuntimeException("parse date string can't be empty.");
		}
		Date date = null;
		try {
			SimpleDateFormat df = getDateFormat(str);
			date = df.parse(str);
		} catch (Throwable e) {
			String error = "can't parse datetime from string[" + str + "].";
			throw new RuntimeException(error, e);
		}
		if (date == null) {
			throw new RuntimeException("can't parse datetime from string[" + str + "].");
		}
		return date;
	}
}
