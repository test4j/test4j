package org.jtester.module.dbfit.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jtester.json.JSON;

import fit.Fixture;
import fit.Parse;

/**
 * 参数解析工具类
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("unchecked")
public class ParseArg {
	/**
	 * 解析json对象
	 * 
	 * @param <T>
	 * @param clazz
	 * @param json
	 * @return
	 */
	public static <T> T paserJson(Class<T> clazz, String json) {
		return (T) JSON.toObject(json, clazz);
	}

	/**
	 * 将字符串解析为map，map entry用entrySplit分割，key-value以keySplit分割
	 * 
	 * @param value
	 * @param entrySplit
	 * @param keySplit
	 * @return
	 */
	public static Map<String, String> parseMap(String value, String entrySplit, String keySplit) {
		Map<String, String> map = new HashMap<String, String>();
		if (value == null || value.equalsIgnoreCase("null")) {
			return null;
		} else if (value.trim().equalsIgnoreCase("")) {
			return map;
		} else {
			String[] pairs = value.split(entrySplit);
			for (String pair : pairs) {
				String[] entry = pair.split(keySplit);
				map.put(entry[0], entry[1]);
			}
			return map;
		}
	}

	/**
	 * 解析默认的方式组装map数据的字符串,map entry用";"分割，key-value以":"分割<br>
	 * 例子: "key1:value1;key2:value2"
	 * 
	 * @param value
	 * @return
	 */
	public static Map<String, String> parseMap(String value) {
		return parseMap(value, ";", ":");
	}

	/**
	 * 解析以split分割的字符串为list对象
	 * 
	 * @param value
	 * @param split
	 * @return
	 */
	public static List<String> parseList(String value, String split) {
		List<String> list = new ArrayList<String>();
		if (value == null || value.equalsIgnoreCase("null")) {
			return null;
		} else if (value.trim().equalsIgnoreCase("")) {
			return list;
		} else {
			String[] items = value.split(split);
			for (String item : items) {
				list.add(item);
			}
			return list;
		}
	}

	/**
	 * 解析以";"分割的字符串为list对象
	 * 
	 * @param value
	 * @return
	 */
	public static List<String> parseList(String value) {
		return parseList(value, ";");
	}

	/**
	 * 变量的正则表达式<br>
	 * 因为${var}是fitnesse wiki的表达式，所以这里改用@{var}
	 * 
	 */
	public static String SYMBOL_PATTERN_EXPRESSION = "@\\{([\\w\\-\\.]+)\\}";

	public static boolean containSymbols(String text) {
		return text.matches(".*" + SYMBOL_PATTERN_EXPRESSION + ".*");
	}

	/**
	 * 根据预存的Symbol变量替换text字符串
	 * 
	 * @param text
	 * @return
	 */
	public static String exactCellSymbolText(final String text) {
		String _text = text;

		String[] symbols = extractParamNames(text);
		for (String symbol : symbols) {
			String value = (String) SymbolUtil.getSymbol(symbol);
			if (value == null) {
				continue;
			}
			_text = _text.replaceFirst("@\\{" + symbol + "\\}", value);
		}
		return _text;
	}

	/**
	 * 变量表达式
	 */
	private static Pattern symbolPattern = Pattern.compile(SYMBOL_PATTERN_EXPRESSION);

	/**
	 * 提取字符串中的变量列表 <br>
	 * MUST RETURN PARAMETER NAMES IN EXACT ORDER AS IN STATEMENT. <br>
	 * IF SINGLE PARAMETER APPEARS MULTIPLE TIMES,<br>
	 * MUST BE LISTED MULTIPLE TIMES IN THE ARRAY ALSO
	 */
	public static String[] extractParamNames(String commandText) {
		ArrayList<String> hs = new ArrayList<String>();
		Matcher mc = symbolPattern.matcher(commandText);
		while (mc.find()) {
			hs.add(mc.group(1));
		}
		String[] array = new String[hs.size()];
		return hs.toArray(array);
	}

	/**
	 * 返回cell中的文本内容
	 * 
	 * @param cell
	 * @return
	 */
	public static String parseCellValue(Parse cell) {
		String content = cell.text();

		if (content.startsWith("<<")) {
			String value = (String) SymbolUtil.getSymbol(content.substring(2));
			cell.addToBody(Fixture.gray("= " + value));
			return value;
		} else if (containSymbols(content)) {
			String value = ParseArg.exactCellSymbolText(content);
			cell.addToBody(Fixture.gray("= " + String.valueOf(value)));
			return value;
		} else {
			return content;
		}
	}

	public static String parseCellValue(String text) {
		if (text == null) {
			return "<null>";
		}
		if (text.startsWith("<<")) {
			String value = (String) SymbolUtil.getSymbol(text.substring(2));
			return value;
		} else if (containSymbols(text)) {
			String value = ParseArg.exactCellSymbolText(text);
			return value;
		} else {
			return text;
		}
	}
}
