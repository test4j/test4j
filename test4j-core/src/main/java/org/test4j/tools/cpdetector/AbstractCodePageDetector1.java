package org.test4j.tools.cpdetector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public abstract class AbstractCodePageDetector1 implements ICodepageDetector {

	@Override
	public Charset detectCodePage(URL url) throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		Charset result = detectCodePage(in, 2147483647);
		in.close();
		return result;
	}

	@Override
	public final Reader open(URL url) throws IOException {
		Reader ret = null;
		Charset cs = detectCodePage(url);
		if (cs != null) {
			ret = new InputStreamReader(new BufferedInputStream(url.openStream()), cs);
		}
		return ret;
	}

	@Override
	public int compareTo(ICodepageDetector o) {
		String other = o.getClass().getName();
		String mine = super.getClass().getName();
		return mine.compareTo(other);
	}
}