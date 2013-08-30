package org.jtester.spec.scenario.step.txt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jtester.spec.annotations.Named;

public class ParameterParser {
	/**
	 * 根据传入的测试描述文件，解析相应的变量名称和变量值
	 * 
	 * @param stepWithoutStartingWord
	 * @return
	 */
	public static LinkedHashMap<String, String> parserParameter(String stepWithoutStartingWord) {
		LinkedHashMap<String, String> paras = new LinkedHashMap<String, String>();
		char[] words = (stepWithoutStartingWord + "\0").toCharArray();
		int count = 1;
		for (int index = 0; index < words.length;) {
			char ch = words[index];
			if (ch == '$' && words[index + 1] == '{') {
				index = parseSinglePara(paras, words, index + 2, String.valueOf(count++));
			}
			index++;
		}
		return paras;
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
	static int parseSinglePara(Map<String, String> paras, char[] words, int start, String defaultKey) {
		StringBuilder buff = new StringBuilder();
		int wordsIndex = start;
		int equalsIndex = 0;

		boolean hasQuato = false;
		boolean ignoreRested = false;
		for (int pos = start; pos < words.length; pos++) {
			char ch = words[pos];
			if (ch == '\\') {
				pos++;
				ch = words[pos];
				buff.append(ch);
				continue;
			} else if (ch == '}' || ch == '\0') {
				break;
			}
			wordsIndex = pos;
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
		return wordsIndex + 1;
	}
	
	
	/**
	 * 从Spec的方法构造"methodname $paraname $paraname"字符串
	 * 
	 * @param method
	 * @return
	 */
	public static String getPatternAsStringFromMethod(Method method) {
		StringBuilder builder = new StringBuilder();
		builder.append(method.getName());
		Annotation[][] paraAnnotations = method.getParameterAnnotations();
		for (int index = 0; index < paraAnnotations.length; index++) {
			Annotation[] annotations = paraAnnotations[index];
			String name = annotationName(annotations, index + 1);
			builder.append(" ").append(name);
		}
		return builder.toString();
	}

	private static String annotationName(Annotation[] annotations, int index) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAssignableFrom(Named.class)) {
				return "$" + ((Named) annotation).value();
			}
		}
		return "$" + index;
	}
}
