package org.jtester.module.dbfit.model;

import java.util.Date;

import org.jtester.tools.commons.DateHelper;

public class SqlDateParseDelegate {

	public static Object parse(String s) throws Exception {
		Date date = DateHelper.parse(s);
		return new java.sql.Date(date.getTime());
	}
}
