package org.jtester.module.dbfit.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jtester.tools.commons.DateHelper;
import org.jtester.tools.commons.FieldHelper;
import org.jtester.tools.commons.StringHelper;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;

import fit.Fixture;

/**
 * ugly workaround for fit change in release 200807, which internally converts
 * NULL into a string value "null"; for db access, we need to make a difference
 * between NULL and "null" so this class provides a centralised place for the
 * change; for dbfit fixtures use this class to access symbols rather than
 * directly fit.fixture
 */
@SuppressWarnings("rawtypes")
public class SymbolUtil {
	private static final Object dbNull = new Object();

	public static void setSymbol(String name, Object value) {
		validateSymbol(name, true);
		fit.Fixture.setSymbol(name, value == null ? dbNull : value);
	}

	/**
	 * 获取symbol变量值<br>
	 * <br>
	 * o 单值类型 name=symbol, symbol由 [a-zA-Z_-]+ 组成 <br>
	 * o Map类型 name=symbol[key], symbol为Map变量的名称，key为map中的key值<br>
	 * o PoJo类型 name=symbol[property], symbol为pojo的变量名称, property为pojo中的属性值
	 * 
	 * @param name
	 * @return
	 */
	public static Object getSymbol(String name) {
		validateSymbol(name, true);

		int index = name.indexOf("[");
		if (index > 0) {
			String symbolName = StringHelper.trim(name.substring(0, index));
			String keyName = StringHelper.trim(name.substring(index + 1, name.length() - 1));
			Object symbol = fit.Fixture.getSymbol(symbolName);
			if (symbol instanceof Map) {
				Object o = ((Map) symbol).get(keyName);
				return o;
			} else {
				try {
					Object o = FieldHelper.getFieldValue(symbol, keyName);
					return o;
				} catch (NoSuchFieldRuntimeException e) {
					return null;
				}
			}
		} else {
			if (system_symbols.containsKey(name)) {
				return system_symbols.get(name);
			} else {
				Object value = fit.Fixture.getSymbol(name);
				if (value == dbNull) {
					return null;
				} else {
					return value;
				}
			}
		}
	}

	/**
	 * 是否存在var这个变量
	 * 
	 * @param var
	 * @return
	 */
	public static boolean hasSymbol(String var) {
		if (Fixture.hasSymbol(var)) {
			return true;
		} else if (system_symbols.containsKey(var)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 清空wiki中已设置的变量
	 */
	public static void cleanSymbols() {
		fit.Fixture.ClearSymbols();
	}

	public static void init() {
		// do nothing;
	}

	/**
	 * 批量设置wiki中用到的变量
	 * 
	 * @param symbols
	 */
	public static void setSymbol(Map<String, ?> symbols) {
		for (Map.Entry<String, ?> symbol : symbols.entrySet()) {
			setSymbol(symbol.getKey(), symbol.getValue());
		}
	}

	/**
	 * 系统预设的变量
	 * 
	 * @return
	 */
	private final static Map<String, Object> system_symbols = new HashMap<String, Object>() {
		private static final long serialVersionUID = 2824018273733392296L;
		{
			put("date", DateHelper.currDateStr());
			put("datetime", DateHelper.currDateTimeStr());
			put("space", " ");
			put("at", "@");
		}
	};

	private final static String VARIABLE_REGEX = "[a-zA-Z0-9_\\-]+";

	/**
	 * 验证变量名称的有效性
	 * 
	 * @param name
	 *            变量名称
	 * @param isThrowable
	 *            如果无效，是否抛出异常
	 * @return
	 */
	private static final boolean validateSymbol(String name, boolean isThrowable) {
		if (StringHelper.isBlankOrNull(name)) {
			if (isThrowable) {
				throw new RuntimeException("the symbol name can't be null.");
			} else {
				return false;
			}
		}
		int index = name.indexOf('[');
		String _name = name;
		String _key = "nokey";
		if (index >= 0 && name.endsWith("]")) {
			_name = name.substring(0, index);
			_key = name.substring(index + 1, name.length() - 1);
		} else if (index >= 0 && name.endsWith("]") == false) {
			if (isThrowable) {
				throw new RuntimeException("the symbol's format must be 'variable[key]', but actual value is " + name);
			} else {
				return false;
			}
		}
		boolean matchedName = _name.matches(VARIABLE_REGEX);
		boolean matchedKey = _key.matches(VARIABLE_REGEX);

		if (isThrowable) {
			if (matchedName == false || matchedKey == false) {
				String error = String.format(
						"illegal symbol format, the symbol's format must be 'variable[key]', but actual value is '%s'",
						name);
				throw new RuntimeException(String.format(error, "symbol key", _key));
			} else {
				return true;
			}
		} else {
			return matchedName && matchedKey;
		}
	}

	private final static Pattern symbolPattern = Pattern.compile("@\\{([a-zA-Z0-9_\\-\\[\\]]+)\\}");

	/**
	 * 替换text文本中的@{var}变量
	 * 
	 * @param text
	 * @return 替换过后的文本
	 */
	public static final String replacedBySymbols(String text) {
		ArrayList<String> vars = new ArrayList<String>();
		Matcher mc = symbolPattern.matcher(text);
		while (mc.find()) {
			String name = mc.group(1);
			validateSymbol(name, true);
			vars.add(name);
		}

		String replaced = text;
		for (String var : vars) {
			Object o = SymbolUtil.getSymbol(var);
			String value = String.valueOf(o);

			if (value != null) {
				replaced = replaced.replace("@{" + var + "}", value);
			}
		}
		return replaced;
	}
}
