package org.jtester.json.encoder.single.fixed;

import java.io.Writer;
import java.net.URL;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class URLEncoder extends FixedTypeEncoder<URL> {

	public static URLEncoder instance = new URLEncoder();

	private URLEncoder() {
		super(URL.class);
	}

	@Override
	protected void encodeSingleValue(URL target, Writer writer) throws Exception {
		String url = target.toString();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(url, writer);
		writer.append(quote_Char);
	}
}
