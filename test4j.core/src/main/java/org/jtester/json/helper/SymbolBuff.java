package org.jtester.json.helper;

/**
 * 变量扫描存储buffer
 * 
 * @author darui.wudr
 * 
 */
public class SymbolBuff {
	private char[] buffString = new char[1024 * 4];

	private int buffIndex = 0;

	public SymbolBuff() {
	}

	public void append(char ch) {
		if (buffIndex == buffString.length) {
			char[] newsbuf = new char[buffString.length * 2];
			System.arraycopy(buffString, 0, newsbuf, 0, buffString.length);
			buffString = newsbuf;
		}
		buffString[buffIndex++] = ch;
	}

	public String getSymbol() {
		String value = new String(buffString, 0, buffIndex);
		buffIndex = 0;
		return value;
	}

	@Override
	public String toString() {
		return new String(buffString, 0, buffIndex);
	}
}
