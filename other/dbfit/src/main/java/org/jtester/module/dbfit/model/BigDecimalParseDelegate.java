package org.jtester.module.dbfit.model;

import java.math.BigDecimal;

public class BigDecimalParseDelegate {
	public static Object parse(String s) {
		return new BigDecimal(s);
	}
}
