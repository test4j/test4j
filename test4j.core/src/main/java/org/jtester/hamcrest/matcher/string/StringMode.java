package org.jtester.hamcrest.matcher.string;

import org.jtester.tools.commons.StringHelper;

/**
 * String Matcher配置模式
 * 
 * @author darui.wudr
 * 
 */
public enum StringMode {
	/**
	 * 忽略大小写
	 */
	IgnoreCase {
		@Override
		public String process(String input) {
			if (input == null) {
				return null;
			}
			return input.toLowerCase();
		}
	},
	/**
	 * 忽略空格
	 */
	IgnoreSpace {
		@Override
		public String process(String input) {
			if (input == null) {
				return null;
			}
			StringBuilder buff = new StringBuilder();
			char[] chars = input.toCharArray();
			for (char ch : chars) {
				if (StringHelper.isSpace(ch) == false) {
					buff.append(ch);
				}
			}
			return buff.toString();
		}
	},
	/**
	 * 忽略引号(单引号和双引号)
	 */
	IgnoreQuato {
		@Override
		public String process(String input) {
			if (input == null) {
				return null;
			}
			int length = input.length();
			StringBuilder buff = new StringBuilder();
			char[] chars = input.toCharArray();
			for (int index = 0; index < length; index++) {
				char ch = chars[index];
				if (ch == '"' || ch == '\'') {
					continue;
				} else {
					buff.append(ch);
				}
			}
			return buff.toString();
		}
	},

	/**
	 * 把所有空格当做一样处理<br>
	 * 即多个空格,换行,换页,TAB等等都当作一个空格来对待<br>
	 * 如果 IgnoreSpace = true ，则不存在这种情况
	 */
	SameAsSpace {
		@Override
		public String process(String input) {
			if (input == null) {
				return null;
			}
			int length = input.length();
			StringBuilder buff = new StringBuilder();
			char[] chars = input.toCharArray();
			for (int index = 0; index < length; index++) {
				char ch = chars[index];
				if (StringHelper.isSpace(ch)) {
					buff.append(' ');
					index = this.skipSpaceChar(chars, index + 1, length);
				} else {
					buff.append(ch);
				}
			}
			return buff.toString();
		}

		/**
		 * 跳过空白字符
		 */
		private int skipSpaceChar(char[] chars, int start, int length) {
			for (int index = start; index < length; index++) {
				char ch = chars[index];
				if (StringHelper.isSpace(ch) == false) {
					return index - 1;
				}
			}
			return length;
		}
	},
	/**
	 * 把单引号,双引号当作一样处理<br>
	 * 如果 IgnoreQuato = true，则不存在这种情况
	 */
	SameAsQuato {
		@Override
		public String process(String input) {
			if (input == null) {
				return null;
			}
			int length = input.length();
			StringBuilder buff = new StringBuilder();
			char[] chars = input.toCharArray();
			for (int index = 0; index < length; index++) {
				char ch = chars[index];
				if (ch == '"') {
					buff.append('\'');
				} else {
					buff.append(ch);
				}
			}
			return buff.toString();
		}
	},
	/**
	 * 将斜杠 '\' 和反斜杠 '/' 都当作斜杠处理
	 */
	SameAsSlash {

		@Override
		public String process(String input) {
			if (input == null) {
				return null;
			}
			int length = input.length();
			StringBuilder buff = new StringBuilder();
			char[] chars = input.toCharArray();
			for (int index = 0; index < length; index++) {
				char ch = chars[index];
				if (ch == '/') {
					buff.append('\\');
				} else {
					buff.append(ch);
				}
			}
			return buff.toString();
		}
	};

	/**
	 * 把字符串按模式处理完返回
	 * 
	 * @param input
	 * @param modes
	 * @return
	 */
	public abstract String process(String input);

	/**
	 * 返回经过StringMode处理过的字符串
	 * 
	 * @param input
	 * @param modes
	 * @return
	 */
	public static String getStringByMode(String input, StringMode... modes) {
		if (modes == null || modes.length == 0) {
			return input;
		}
		String output = input;
		for (StringMode mode : modes) {
			output = mode.process(output);
		}
		return output;
	}

	public static String toString(StringMode... modes) {
		if (modes == null || modes.length == 0) {
			return "[]";
		}
		StringBuilder buff = new StringBuilder("[");
		for (StringMode mode : modes) {
			buff.append(mode.name()).append(" ");
		}
		buff.append("]");
		return buff.toString();
	}
}
