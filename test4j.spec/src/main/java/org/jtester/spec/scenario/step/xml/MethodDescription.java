package org.jtester.spec.scenario.step.xml;

import java.util.Map;

/**
 * 用例场景方法描述+具体参数构造器
 * 
 * @author darui.wudr 2012-6-29 下午4:32:51
 */
public class MethodDescription {
	private char[] chars;
	private int length;
	private Map<String, String> paras;

	public MethodDescription(String template, Map<String, String> paras) {
		this.chars = template.toCharArray();
		this.length = this.chars.length;
		this.paras = paras;
	}

	public String getMethodDisplayText() {
		StringBuilder buff = new StringBuilder();
		for (int index = 0; index < length; index++) {
			char ch = chars[index];
			switch (ch) {
			case '$':
				String var = this.getVariable(index);
				if (var == null) {
					buff.append(ch);
				} else {
					buff.append("\n").append(var).append("=").append(this.paras.get(var));
					index = index + var.length() + VAR_START.length();
				}
				break;
			default:
				buff.append(ch);
			}
		}
		return buff.toString();
	}

	String getVariable(int start) {
		if (start + VAR_START.length() + 1 >= length) {
			return null;
		}
		boolean isStart = this.isVarStart(start);
		if (isStart == false) {
			return null;
		}
		StringBuilder variable = new StringBuilder();
		for (int index = start + VAR_START.length(); index < length; index++) {
			char ch = chars[index];
			switch (ch) {
			case '}':// 碰到}，是个合法变量
				return variable.toString();
			case '{':// 不是一个合法的变量
				return null;
			default:
				variable.append(ch);
			}
		}
		return null;
	}

	/**
	 * 是否是变量开始提示符
	 * 
	 * @param start
	 * @return
	 */
	private boolean isVarStart(int start) {
		for (int index = 0; index < VAR_START.length(); index++) {
			if (chars[start + index] != VAR_START.charAt(index)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 变量开始提示符
	 */
	public final static String VAR_START = "$_#_@_&{";
}
