package org.jtester.spec.reader;

public class StringLinesReader implements LinesReader {
	private final String[] lines;
	private int index;
	private final int total;

	public StringLinesReader(String[] lines) {
		this.lines = lines == null ? new String[0] : lines;
		this.index = 0;
		this.total = this.lines.length;
	}

	public String readLine() {
		if (index >= total) {
			return null;
		}
		String line = lines[index];
		index++;
		return line;
	}

	public void close() {
	}
}
