package org.jtester.spec.util;

import java.util.ArrayList;
import java.util.List;

import org.jtester.spec.util.XmlHelper.MethodNode;
import org.jtester.tools.commons.StringHelper;

public class MethodNodeParser {
	private final char[] chars;
	private final int total;
	private int currIndex;

	public MethodNodeParser(String text) {
		this.chars = (text + "\0").toCharArray();
		this.total = text.length();
		this.currIndex = 0;
	}

	public List<MethodNode> parseMethodNodes() {
		List<MethodNode> nodes = new ArrayList<MethodNode>();
		StringBuffer textBuff = new StringBuffer();
		while (currIndex < total) {
			boolean isBngParaElement = this.isBgnParaElement(this.currIndex);
			if (isBngParaElement) {
				this.addMethodText(nodes, textBuff.toString());
				textBuff = new StringBuffer();

				MethodNode paraNode = this.parseParameterName(this.currIndex);
				if (paraNode == null) {
					throw new RuntimeException(
							"there must be some wrong, the method parseParameterName should return a MethodNode instance or throw an Exception.");
				}
				this.parseParameterValue(paraNode, this.currIndex);
				nodes.add(paraNode);
			} else {
				char ch = chars[currIndex];
				textBuff.append(ch);
				currIndex++;
			}
		}
		this.addMethodText(nodes, textBuff.toString());
		return nodes;
	}

	/**
	 * 增加文本节点
	 * 
	 * @param nodes
	 * @param text
	 */
	final void addMethodText(List<MethodNode> nodes, String text) {
		if (StringHelper.isBlankOrNull(text)) {
			return;
		}
		MethodNode textNode = new MethodNode(false);
		textNode.setText(text);
		nodes.add(textNode);
	}

	final char currIndexChar() {
		return this.chars[this.currIndex];
	}

	/**
	 * 从currIndex位置开始解析参数内容<br>
	 * 并且将游标移到下一区域的开始
	 * 
	 * @return
	 */
	final void parseParameterValue(MethodNode method, int index) {
		StringBuffer value = new StringBuffer();

		index = this.skipWhitespace(index);
		boolean isStartCDATA = this.isStartCDATA(index);
		boolean isEndCDATA = false;
		if (isStartCDATA) {
			index = index + 9;
		}
		boolean isEndParaNode = false;
		while (index < total) {
			char ch = this.chars[index];
			if (ch == '<') {
				isEndParaNode = this.isEndParaElement(index);
				if (isEndParaNode) {
					break;
				}
				boolean isStart = this.isBgnParaElement(index);
				if (isStart) {
					throw new RuntimeException(
							"you haven't end with previous para element, but renew a para elemetn at index["
									+ this.currIndex + "]");
				}
			}
			if (isEndCDATA) {
				throw new RuntimeException("expected end of para element at index[" + index + "].");
			}
			if (ch == ']') {
				isEndCDATA = this.isEndCDATA(index);
				if (isEndCDATA) {
					index = this.skipWhitespace(index + 3);
					continue;
				}
			}
			value.append(ch);
			index++;
		}
		if (isStartCDATA && !isEndCDATA) {
			throw new RuntimeException("you have \"<![CDATA[\" begin of para value, but not end with \"]]>\"");
		}
		if (!isStartCDATA && isEndCDATA) {
			throw new RuntimeException("you have \"]]>\" end of para value, but not begin with \"<![CDATA[\"");
		}
		if (!isEndParaNode) {
			throw new RuntimeException("The element type 'para' must be terminated by the matching end-tag '</para>'");
		}
		method.setText(value.toString());
	}

	/**
	 * 判断是否是para元素结尾 &lt;/para&gt;<br>
	 * 如果是，则将当前游标指向下一个区域的开始
	 * 
	 * @param index
	 * @return
	 */
	final boolean isEndParaElement(int index) {
		char ch = this.chars[index];
		if (ch != '<') {
			return false;
		}

		index = this.skipWhitespace(index + 1);
		ch = this.chars[index];
		if (ch != '/') {
			return false;
		}

		index = this.skipWhitespace(index + 1);
		boolean isParaKey = this.isParaKey(index);
		if (!isParaKey) {
			return false;
		}

		index = this.skipWhitespace(index + 4);
		ch = this.chars[index];
		if (ch != '>') {
			throw new RuntimeException(String.format(EXPECTED_CHAR_ERROR, '>', index, ch));
		}
		this.currIndex = index + 1;
		return true;
	}

	/**
	 * 判断是否是para元素开头 &lt;para<br>
	 * 如果是，则将当前游标指向name属性
	 * 
	 * @param index
	 * @return
	 */
	final boolean isBgnParaElement(int index) {
		char ch = this.chars[index];
		if (ch != '<') {
			return false;
		}

		index = this.skipWhitespace(index + 1);
		boolean isParaKey = isParaKey(index);
		if (isParaKey == false) {
			return false;
		}
		index = this.skipWhitespace(index + 4);
		this.currIndex = index;
		return true;
	}

	static final String EXPECTED_CHAR_ERROR = "expected char('%s') at index[%d], but actual is '%s'.";

	/**
	 * 解析参数名称 name="???"&gt;<br>
	 * 将当前游标移到参数内容开始位置，并构造MethodNode(参数)返回
	 * 
	 * @return
	 */
	final MethodNode parseParameterName(int index) {
		index = this.skipWhitespace(index);
		boolean isParaName = this.isParaName(index);
		if (isParaName == false) {
			throw new RuntimeException("only name property allowed by para element, error at index[" + index + "]");
		}

		index = this.skipWhitespace(index + 4);
		char ch = chars[index];
		if (ch != '=') {
			throw new RuntimeException(String.format(EXPECTED_CHAR_ERROR, '=', index, ch));
		}

		index = this.skipWhitespace(index + 1);
		ch = chars[index];
		if (ch != '"') {
			throw new RuntimeException(String.format(EXPECTED_CHAR_ERROR, '"', index, ch));
		}
		StringBuffer paraName = new StringBuffer();
		ch = chars[++index];
		while (index < total && ch != '"') {
			paraName.append(ch);
			ch = chars[++index];
		}

		index = this.skipWhitespace(index + 1);
		ch = chars[index];
		if (ch != '>') {
			throw new RuntimeException(String.format(EXPECTED_CHAR_ERROR, '>', index, ch));
		}
		MethodNode paraNode = new MethodNode(true);
		paraNode.setParaName(paraName.toString());
		if (StringHelper.isBlankOrNull(paraNode.getParaName())) {
			throw new RuntimeException("the name of para can't be null! index at " + index);
		}
		this.currIndex = index + 1;
		return paraNode;
	}

	/**
	 * 是否是保留字 para
	 * 
	 * @param index
	 * @return
	 */
	private boolean isParaKey(int index) {
		boolean isParaKey = this.isKey(index, new char[] { 'p', 'a', 'r', 'a' }, true);
		return isParaKey;
	}

	/**
	 * 是否是name属性
	 * 
	 * @param index
	 * @return
	 */
	private boolean isParaName(int index) {
		boolean isParaName = this.isKey(index, new char[] { 'n', 'a', 'm', 'e' }, true);
		return isParaName;
	}

	/**
	 * 是否是 &lt;![CDATA[ 关键字
	 * 
	 * @param index
	 * @return
	 */
	private boolean isStartCDATA(int index) {
		boolean isCDATA = this.isKey(index, new char[] { '<', '!', '[', 'C', 'D', 'A', 'T', 'A', '[' }, false);
		return isCDATA;
	}

	/**
	 * 是否是 ]]&gt;关键字
	 * 
	 * @param index
	 * @return
	 */
	private boolean isEndCDATA(int index) {
		boolean isCDATA = this.isKey(index, new char[] { ']', ']', '>' }, false);
		return isCDATA;
	}

	/**
	 * 从index开始的字符串是否是关键字
	 * 
	 * @param index
	 * @param key
	 * @param ignoreCase
	 *            是否忽略关键字字符的大小写
	 * @return
	 */
	private boolean isKey(int index, char[] key, boolean ignoreCase) {
		for (char ch : key) {
			if (!ignoreCase && chars[index] == ch && index < total) {
				index++;
				continue;
			}
			if (ignoreCase && (chars[index] == toLower(ch) || chars[index] == toUpper(ch)) && index < total) {
				index++;
				continue;
			}
			return false;
		}
		return true;
	}

	char toLower(char ch) {
		return String.valueOf(ch).toLowerCase().charAt(0);
	}

	char toUpper(char ch) {
		return String.valueOf(ch).toUpperCase().charAt(0);
	}

	/**
	 * 跳过空白字符
	 */
	final int skipWhitespace(int index) {
		char ch = chars[index];
		while (StringHelper.isSpace(ch) && index < total) {
			index++;
			ch = chars[index];
		}
		return index;
	}
}
