package org.jtester.tools.cpdetector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import ext.jtester.mozilla.intl.chardet.NsDetector;
import ext.jtester.mozilla.intl.chardet.NsICharsetDetectionObserver;
import ext.jtester.mozilla.intl.chardet.NsPSMDetector;

public class JChardetFacade extends AbstractCodepageDetector implements NsICharsetDetectionObserver {
	private static JChardetFacade instance = null;
	private static NsDetector det;
	private byte[] buf = new byte[4096];

	private Charset codpage = null;

	private int amountOfVerifiers = 0;

	private JChardetFacade() {
		det = new NsDetector(NsPSMDetector.SIMPLIFIED_CHINESE);
		det.init(this);
		this.amountOfVerifiers = det.getProbableCharsets().length;
	}

	public static JChardetFacade getInstance() {
		if (instance == null) {
			instance = new JChardetFacade();
		}
		return instance;
	}

	public synchronized Charset detectCodepage(InputStream in, int length) throws IOException {
		int len;
		reset();

		int read = 0;
		boolean done = false;
		Charset ret = null;
		do {
			len = in.read(this.buf, 0, Math.min(this.buf.length, length - read));
			if (len > 0) {
				read += len;
			}
			if (!(done))
				done = det.doIt(this.buf, len, false);
		} while ((len > 0) && (!(done)));
		det.dataEnd();
		if (this.codpage == null) {
			ret = guess();
		} else {
			ret = this.codpage;
		}
		return ret;
	}

	private Charset guess() {
		Charset ret = null;
		String[] possibilities = det.getProbableCharsets();

		if (possibilities.length == this.amountOfVerifiers) {
			ret = Charset.forName("US-ASCII");
		} else {
			String check = possibilities[0];
			if (check.equalsIgnoreCase("nomatch"))
				ret = UnknownCharset.getInstance();
			else {
				for (int i = 0; (ret == null) && (i < possibilities.length); ++i) {
					try {
						ret = Charset.forName(possibilities[i]);
					} catch (UnsupportedCharsetException uce) {
						ret = UnsupportedCharset.forName(possibilities[i]);
					}
				}
			}
		}
		return ret;
	}

	public void notify(String charset) {
		this.codpage = Charset.forName(charset);
	}

	public void reset() {
		det.reset();
		this.codpage = null;
	}
}
