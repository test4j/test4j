package org.test4j.tools.cpdetector;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public interface ICodepageDetector extends Comparable<ICodepageDetector> {
	Reader open(URL paramURL) throws IOException;

	Charset detectCodePage(URL paramURL) throws IOException;

	Charset detectCodePage(InputStream paramInputStream, int paramInt) throws IOException;
}
