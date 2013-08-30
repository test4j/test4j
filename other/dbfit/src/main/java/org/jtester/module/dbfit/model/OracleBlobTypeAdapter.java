package org.jtester.module.dbfit.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class OracleBlobTypeAdapter {
	public static Object parse(String input) throws Exception {
		try {
			byte[] bs = input.getBytes("UTF-8");
			InputStream is = new ByteArrayInputStream(bs);
			return is;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
