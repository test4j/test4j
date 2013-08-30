package org.test4j.spec.reader;

import java.io.IOException;

public interface LinesReader {
	String readLine() throws IOException;

	void close();
}
