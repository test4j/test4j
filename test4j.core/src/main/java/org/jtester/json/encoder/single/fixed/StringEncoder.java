package org.jtester.json.encoder.single.fixed;

import java.io.IOException;
import java.io.Writer;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class StringEncoder extends FixedTypeEncoder<String> {
	public static StringEncoder instance = new StringEncoder();

	private StringEncoder() {
		super(String.class);
	}

	@Override
	public void encodeSingleValue(String target, Writer writer) throws Exception {
		writer.append(quote_Char);
		writeEscapeString(target, writer);
		writer.append(quote_Char);
	}

	public static void writeEscapeString(String value, Writer writer) throws IOException {
		char[] chars = value.toCharArray();
		for (char ch : chars) {
			writerChar(ch, writer);
		}
	}

	/**
	 * 处理特殊字符
	 * 
	 * @return
	 * @throws IOException
	 */
	public static void writerChar(char ch, Writer writer) throws IOException {
		switch (ch) {
		case '\"':
			writer.append(Anti_Slash).append('"');
			return;
		case '\'':
			writer.append(Anti_Slash).append('\'');
			return;
		case '\\':
			writer.append(Anti_Slash).append('\\');
			return;
		case '\b':
			writer.append(Anti_Slash).append('b');
			return;
		case '\f':
			writer.append(Anti_Slash).append('f');
			return;
		case '\n':
			writer.append(Anti_Slash).append('n');
			return;
		case '\r':
			writer.append(Anti_Slash).append('r');
			return;
		case '\t':
			writer.append(Anti_Slash).append('t');
			return;
			// case '/':
			// writer.append(Anti_Slash).append('/');
			// return;
		default:
			writer.append(ch);
		}
	}

	final static char Anti_Slash = '\\';
}
