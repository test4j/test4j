package org.jtester.tools.cpdetector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public abstract class AbstractCodepageDetector implements ICodepageDetector {

	public Charset detectCodepage(URL url) throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		Charset result = detectCodepage(in, 2147483647);
		in.close();
		return result;
	}

	public final Reader open(URL url) throws IOException {
		Reader ret = null;
		Charset cs = detectCodepage(url);
		if (cs != null) {
			ret = new InputStreamReader(new BufferedInputStream(url.openStream()), cs);
		}
		return ret;
	}

	public int compareTo(ICodepageDetector o) {
		String other = o.getClass().getName();
		String mine = super.getClass().getName();
		return mine.compareTo(other);
	}
}