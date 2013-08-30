package org.jtester.module.dbfit.model;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jtester.module.dbfit.utility.ParseArg;
import org.jtester.module.dbfit.utility.SymbolUtil;
import org.jtester.tools.commons.StringHelper;

import fit.Binding;
import fit.Fixture;
import fit.Parse;

public class SymbolAccessQueryBinding extends Binding.QueryBinding {
	boolean symbol_has_set = false;

	public void doCell(Fixture fixture, Parse cell, String symbolName, int rowNum) {
		symbol_has_set = false;
		String content = cell.text();
		if (content.startsWith(">>")) {
			try {
				Object value = this.adapter.get();
				setSymbols(content, symbolName, value, rowNum);
				symbol_has_set = true;
			} catch (Throwable t) {
				fixture.exception(cell, t);
				return;
			}
		}
		doCell(fixture, cell);
	}

	public void doCell(Fixture fixture, Parse cell) {
		String content = cell.text();
		try {
			if (content.startsWith(">>")) {
				Object value = this.adapter.get();
				if (symbol_has_set == false) {
					SymbolUtil.setSymbol(content.substring(2).trim(), value);
				}
				cell.addToBody(Fixture.gray("= " + String.valueOf(value)));
				return;
			}
			String value = ParseArg.parseCellValue(cell);

			Object actual = this.adapter.get();
			if (content.startsWith("fail[") || content.endsWith("]")) {
				String expectedVal = value.substring(5, value.length() - 1);
				Object expected = adapter.parse(expectedVal);
				if (adapter.equals(actual, expected)) {
					fixture.wrong(cell, String.valueOf(value));
				} else {
					fixture.right(cell);
				}
			} else {
				Object expected = this.adapter.parse(value);
				if (expected instanceof InputStream) {
					expected = value;
				}
				if (expected instanceof Date && actual instanceof Date) {
					long time_expected = ((Date) expected).getTime();
					long time_actual = ((Date) actual).getTime();
					if (time_expected == time_actual) {
						fixture.right(cell);
					} else {
						fixture.wrong(cell, String.valueOf(value));
					}
				} else if (adapter.equals(actual, expected)) {
					fixture.right(cell);
				} else {
					fixture.wrong(cell, String.valueOf(value));
				}
			}
		} catch (Throwable t) {
			fixture.exception(cell, t);
			return;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setSymbols(String content, String idName, Object value, int rowNum) throws IllegalAccessException,
			InvocationTargetException {

		content = content.substring(2);
		String keyName = String.valueOf(rowNum);
		int index = content.indexOf("[");
		String symbolName = idName;
		if (index > -1) {
			symbolName = StringHelper.trim(content.substring(0, index));
			keyName = StringHelper.trim(content.substring(index + 1, content.length() - 1));
		} else if (content.length() > 1) {
			symbolName = StringHelper.trim(content.substring(1));
		}

		if (StringHelper.isBlankOrNull(symbolName)) {
			symbolName = idName;
		}
		Object symbol = SymbolUtil.getSymbol(symbolName);
		if (symbol instanceof Map) {
			Map map = (Map) symbol;
			map.put(keyName, value);
		} else {
			Map map = new HashMap<String, Object>();
			map.put(keyName, value);
			SymbolUtil.setSymbol(symbolName, map);
		}
	}
}
