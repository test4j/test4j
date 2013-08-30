package org.jtester.tools.cpdetector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class CodepageDetectorProxy extends AbstractCodepageDetector {
	private static CodepageDetectorProxy instance = null;

	private Set<ICodepageDetector> detectors = new LinkedHashSet<ICodepageDetector>();

	public static CodepageDetectorProxy getInstance() {
		if (instance == null) {
			instance = new CodepageDetectorProxy();
		}
		return instance;
	}

	public boolean add(ICodepageDetector detector) {
		return this.detectors.add(detector);
	}

	public Charset detectCodepage(URL url) throws IOException {
		Charset ret = null;
		Iterator<ICodepageDetector> detectorIt = this.detectors.iterator();
		while (detectorIt.hasNext()) {
			ret = ((ICodepageDetector) detectorIt.next()).detectCodepage(url);
			if ((ret != null) && (ret != UnknownCharset.getInstance()) && (!(ret instanceof UnsupportedCharset))) {
				break;
			}

		}

		return ret;
	}

	public Charset detectCodepage(InputStream in, int length) throws IOException, IllegalArgumentException {
		if (!(in.markSupported())) {
			throw new IllegalArgumentException("The given input stream (" + in.getClass().getName()
					+ ") has to support marking.");
		}
		Charset ret = null;
		int markLimit = length;
		Iterator<ICodepageDetector> detectorIt = this.detectors.iterator();
		while (detectorIt.hasNext()) {
			in.mark(markLimit);
			ret = ((ICodepageDetector) detectorIt.next()).detectCodepage(in, length);
			try {
				in.reset();
			} catch (IOException ioex) {
				throw new IllegalArgumentException(
						"More than the given length had to be read and the given stream could not be reset. Undetermined state for this detection.");
			}

			if ((ret != null) && (ret != UnknownCharset.getInstance()) && (!(ret instanceof UnsupportedCharset))) {
				break;
			}

		}

		return ret;
	}

	public String toString() {
		StringBuffer ret = new StringBuffer();
		Iterator<ICodepageDetector> it = this.detectors.iterator();
		int i = 1;
		while (it.hasNext()) {
			ret.append(i);
			ret.append(") ");
			ret.append(it.next().getClass().getName());
			ret.append("\n");
			++i;
		}
		return ret.toString();
	}
}
