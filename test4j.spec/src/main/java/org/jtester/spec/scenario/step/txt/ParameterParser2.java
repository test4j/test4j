package org.jtester.spec.scenario.step.txt;

import static org.jtester.spec.scenario.step.xml.MethodDescription.VAR_START;

import java.util.Map;

public class ParameterParser2 {
	/**
	 * 根据传入的测试描述文件，解析相应的变量名称和变量值
	 * 
	 * @param text
	 * @param paras
	 *            存放解析出来的参数
	 * @return 返回描述内容模板
	 */
	public static String parserParameter(String text, Map<String, String> paras) {
		StringBuilder buff = new StringBuilder();
		char[] words = (text + "\0").toCharArray();
		int count = 1;
		for (int index = 0; index < words.length;) {
			char ch = words[index];
			index++;
			if (ch == '\\') {
				continue;
			}
			if (ch == '【') {
				buff.append(VAR_START).append(count).append("}");
				index = parseSinglePara(paras, words, index, String.valueOf(count++));
			} else {
				buff.append(ch);
			}
		}
		String textTemplate = buff.toString();
		count = 1;
		for (String key : paras.keySet()) {
			textTemplate = textTemplate.replace(VAR_START + (count++) + "}", VAR_START + key + "}");
		}
		return textTemplate.trim();
	}

	/**
	 * 解析单个变量名称和值，返回结束符所在位置
	 * 
	 * @param paras
	 *            变量列表
	 * @param words
	 *            被解析的字符串
	 * @param start
	 *            解析开始位置
	 * @param defaultKey
	 *            默认的key name
	 * @return
	 */
	static int parseSinglePara(Map<String, String> paras, char[] words, final int start, String defaultKey) {
		StringBuilder buff = new StringBuilder();
		int wordsIndex = start;
		int equalsIndex = 0;

		boolean hasQuato = false;
		boolean ignoreRested = false;
		for (int pos = start; pos < words.length; pos++) {
			char ch = words[pos];
			// 反义符
			if (ch == '\\') {
				pos++;
				ch = words[pos];
				buff.append(ch);
				continue;
			} else if (ch == '】' || ch == '\0') {
				wordsIndex = pos;
				break;
			}
			if (ignoreRested) {
				continue;
			}
			// 当碰到'"'时，还没有'='，表示没有显式指定变量名称
			if (ch == '"' && equalsIndex == 0) {
				equalsIndex = -1;
				if (pos == start) {
					hasQuato = true;
				}
			} else if (ch == '=' && equalsIndex == 0) {
				equalsIndex = pos - start;
				if (words[pos + 1] == '"') {
					hasQuato = true;
				}
			} else if (ch == '|') {
				ignoreRested = true;
				continue;
			}
			buff.append(ch);
		}
		String para = buff.toString();
		String key = defaultKey;
		if (equalsIndex > 0) {
			key = para.substring(0, equalsIndex);
			para = para.substring(equalsIndex + 1);
		}
		String value = para;
		if (hasQuato) {
			value = para.substring(1, para.length() - 1);
		}
		paras.put(key, value);
		return wordsIndex == start ? words.length : wordsIndex + 1;
	}
}
