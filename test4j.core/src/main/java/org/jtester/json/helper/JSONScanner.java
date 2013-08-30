package org.jtester.json.helper;

import org.jtester.json.JSONException;
import org.jtester.tools.commons.StringHelper;

public class JSONScanner {

	public static JSONObject scnJSON(String json) {
		if (json == null) {
			return null;
		}
		if (StringHelper.isBlank(json)) {
			return new JSONSingle(json);
		}
		char[] input = json.toCharArray();
		JSONScanner scanner = new JSONScanner(input);
		JSONObject o = scanner.scan();
		return o;
	}

	private final char[] input;

	private final int length;

	private int index;

	JSONScanner(char[] input) {
		this.index = 0;
		this.length = input.length;

		this.input = new char[this.length + 1];
		System.arraycopy(input, 0, this.input, 0, this.length);
		this.input[length] = '\0';
	}

	public JSONObject scan() {
		JSONObject json = null;
		char ch = this.nextToken();
		switch (ch) {
		case '{':
			json = this.scanJSONMap();
			break;
		case '[':
			json = this.scanJSONArray();
			break;
		default:
			this.index--;
			json = this.scanJSONValue();
		}
		ch = this.nextToken();
		if (ch != '\0' || this.index != this.length + 1) {
			throw this.syntaxError("syntax error.");
		}
		return json;
	}

	JSONMap scanJSONMap() {
		JSONMap map = new JSONMap();

		char ch = this.nextToken();
		this.index--;
		while (ch != '}') {
			JSONObject key = scanMapKey();
			ch = this.nextToken();
			if (ch != ':') {
				throw this.syntaxError("syntax error, expected char ':'.");
			}
			JSONObject value = scanMapValue();
			map.put(key, value);

			ch = this.nextToken();
			if (ch != ',' && ch != '}') {
				throw this.syntaxError("syntax error, expected char '}' or ','.");
			}
		}

		return map;
	}

	JSONArray scanJSONArray() {
		JSONArray array = new JSONArray();

		char ch = this.nextToken();
		while (ch != ']') {
			JSONObject json = null;
			switch (ch) {
			case '{':
				json = this.scanJSONMap();
				break;
			case '[':
				json = this.scanJSONArray();
				break;
			case '\'':
				json = this.scanString('\'');
				break;
			case '"':
				json = this.scanString('"');
				break;
			default:
				this.index--;
				json = this.scanString(',', ']', '\0');
				this.index--;
			}
			array.add(json);
			ch = this.nextToken();
			if (ch != ',' && ch != ']') {
				throw this.syntaxError("syntax error, expectd char ',' or ']'.");
			}
			if (ch == ',') {
				ch = this.nextToken();
			}
		}
		return array;
	}

	JSONSingle scanJSONValue() {
		JSONSingle json = null;
		char ch = this.nextToken();
		switch (ch) {
		case '\'':
			json = this.scanString('\'');
			break;
		case '"':
			json = this.scanString('"');
			break;
		default:
			this.index--;
			json = this.scanString('\0');
			this.index--;
		}
		return json;
	}

	/**
	 * 返回json值对的key
	 * 
	 * @return
	 */
	JSONObject scanMapKey() {
		char ch = this.nextToken();
		JSONObject json = null;
		switch (ch) {
		case '\0':
			throw this.syntaxError("syntax error.");
		case '{':
			json = this.scanJSONMap();
			break;
		case '[':
			json = this.scanJSONArray();
			break;
		case '"':
			json = this.scanString('"');
			break;
		case '\'':
			json = this.scanString('\'');
			break;
		default:
			this.index--;
			json = this.scanString(':');
			this.index--;
		}

		return json;
	}

	/**
	 * 返回json值对的value
	 * 
	 * @return
	 */
	JSONObject scanMapValue() {
		char ch = this.nextToken();
		JSONObject json = null;
		switch (ch) {
		case '\0':
			throw this.syntaxError("syntax error");
		case '{':
			json = this.scanJSONMap();
			break;
		case '[':
			json = this.scanJSONArray();
			break;
		case '"':
			json = this.scanString('"');
			break;
		case '\'':
			json = this.scanString('\'');
			break;
		default:
			this.index--;
			json = this.scanString(',', '}', ']');
			this.index--;
		}

		return json;
	}

	private SymbolBuff symbolBuff = new SymbolBuff();

	/**
	 * 扫描json的单值对象<br>
	 * String对象 或其它非 [] 和 {} 括起来的对象
	 * 
	 * @param endChar
	 *            扫描终止符
	 * @param endChars
	 *            扫描终止符
	 * @return
	 */
	private final JSONSingle scanString(final char endChar, char... endChars) {
		JSONSingle json = new JSONSingle(endChar == '\'' || endChar == '"');
		json.setBeginIndex(this.index);

		char ch = this.input[this.index++];
		while (ch != endChar && !contain(endChars, ch)) {
			if (ch == '\0') {
				throw this.syntaxError("syntax error.");
			} else if (ch == '\\') {
				ch = this.getEscapedChar();
				symbolBuff.append(ch);
			} else {
				symbolBuff.append(ch);
			}
			ch = this.input[this.index++];
		}

		String symbol = symbolBuff.getSymbol();
		json.setValue(symbol);
		json.setEndIndex(this.index);
		return json;
	}

	/**
	 * 字符数组中是否包含某字符
	 * 
	 * @param chars
	 * @param ch
	 * @return
	 */
	private static boolean contain(char[] chars, char ch) {
		if (chars.length == 0 || chars == null) {
			return false;
		}
		for (char c : chars) {
			if (c == ch) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回反义字符
	 * 
	 * @return
	 */
	private char getEscapedChar() {
		char ch = this.input[this.index++];

		switch (ch) {
		case '"':
			return '"';
		case '\'':
			return '\'';
		case '\\':
			return '\\';
		case '/':
			return '/';
		case 'b':
			return 'b';
		case 'f':
		case 'F':
			return '\f';
		case 'n':
			return '\n';
		case 'r':
			return '\r';
		case 't':
			return '\t';
		case 'u':
			char u1 = this.input[this.index++];
			char u2 = this.input[this.index++];
			char u3 = this.input[this.index++];
			char u4 = this.input[this.index++];
			int val = Integer.parseInt(new String(new char[] { u1, u2, u3, u4 }), 16);
			return (char) val;
		default:
			return ch;
			// throw this.syntaxError("syntax error.");
		}
	}

	char nextToken() {
		if (this.index > this.length) {
			throw this.syntaxError("syntax error end.");
		}
		this.skipWhitespace();
		char ch = input[index];
		index++;

		return ch;
	}

	/**
	 * 跳过空白字符
	 */
	final int skipWhitespace() {
		char ch = input[index];
		while (StringHelper.isSpace(ch)) {
			index++;
			ch = input[index];
		}
		return index;
	}

	private static boolean[] singleValueFlags = new boolean[256];
	static {
		for (char ch = '0'; ch <= '9'; ch++) {
			singleValueFlags[ch] = true;
		}
		for (char ch = 'a'; ch <= 'z'; ch++) {
			singleValueFlags[ch] = true;
		}
		for (char ch = 'A'; ch <= 'Z'; ch++) {
			singleValueFlags[ch] = true;
		}
		singleValueFlags['-'] = true;
		singleValueFlags['+'] = true;
		singleValueFlags['.'] = true;
		singleValueFlags[':'] = true;
	}

	private JSONException syntaxError(String message) {
		StringBuffer error = new StringBuffer(message);
		error.append("\n syntax error at position:" + this.index);
		error.append("\n string parsed is:\n");
		error.append(new String(this.input, 0, this.index));
		return new JSONException(error.toString());
	}
}
