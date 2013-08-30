package org.jtester.module.dbfit.fixture;

import java.util.Date;

import org.jtester.module.dbfit.exception.HasMarkedException;
import org.jtester.tools.commons.DateHelper;

import fit.Fixture;
import fit.Parse;
import fit.exception.CouldNotParseFitFailureException;

@SuppressWarnings("rawtypes")
public class JTesterFixture extends Fixture {
	/**
	 * 过滤掉已经标记过的异常<br>
	 * 这样做是为了异常定位更加正确
	 */
	@Override
	public void exception(Parse cell, Throwable exception) {
		if (exception instanceof HasMarkedException) {
			return;
		}
		if (cell != null) {
			super.exception(cell, exception);
			return;
		}
		if (exception instanceof RuntimeException) {
			throw (RuntimeException) exception;
		} else {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public Object parse(String s, Class type) throws Exception {
		if (type.equals(String.class)) {
			if (s.toLowerCase().equals("null"))
				return null;
			else if (s.toLowerCase().equals("blank"))
				return "";
			else
				return s;
		} else if (type.equals(Date.class)) {
			Date date = DateHelper.parse(s);
			return date;
		} else if (hasParseMethod(type)) {
			return callParseMethod(type, s);
		} else {
			throw new CouldNotParseFitFailureException(s, type.getName());
		}
	}
}
