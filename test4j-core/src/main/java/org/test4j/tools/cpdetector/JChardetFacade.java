package org.test4j.tools.cpdetector;

import cicada.chardet.nsDetector;
import cicada.chardet.nsICharsetDetectionObserver;
import cicada.chardet.nsPSMDetector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class JChardetFacade extends AbstractCodePageDetector implements nsICharsetDetectionObserver {
    private static JChardetFacade instance = null;
    private static nsDetector det;
    private byte[] buf = new byte[4096];

    private Charset codpage = null;

    private int amountOfVerifiers = 0;

    private JChardetFacade() {
        det = new nsDetector(nsPSMDetector.SIMPLIFIED_CHINESE);
        det.Init(this);
        this.amountOfVerifiers = det.getProbableCharsets().length;
    }

    public static JChardetFacade getInstance() {
        if (instance == null) {
            instance = new JChardetFacade();
        }
        return instance;
    }

    public synchronized Charset detectCodePage(InputStream in, int length) throws IOException {
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
                done = det.DoIt(this.buf, len, false);
        } while ((len > 0) && (!(done)));
        det.DataEnd();
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

    @Override
    public void Notify(String charset) {
        this.codpage = Charset.forName(charset);
    }

    public void reset() {
        det.Reset();
        this.codpage = null;
    }
}
