package org.jtester.spec.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.jtester.tools.commons.ResourceHelper;

public class StreamLinesReader implements LinesReader {
	private final BufferedReader reader;

	public StreamLinesReader(InputStream is, String encoding) {
		try {
			if (encoding == null) {
				encoding = ResourceHelper.getFileEncodingCharset(is);
			}
			this.reader = new BufferedReader(new InputStreamReader(is, encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String readLine() throws IOException {
		String line = reader.readLine();
		return line;
	}

	public void close() {
		if (reader == null) {
			return;
		}
		try {
			reader.close();
		} catch (IOException e) {
		}
	}
}
