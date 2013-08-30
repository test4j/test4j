package org.test4j.json.encoder.single.fixed;

import java.io.Writer;
import java.net.URI;

import org.test4j.json.encoder.single.FixedTypeEncoder;

public class URIEncoder extends FixedTypeEncoder<URI> {
	public static URIEncoder instance = new URIEncoder();

	private URIEncoder() {
		super(URI.class);
	}

	@Override
	protected void encodeSingleValue(URI target, Writer writer) throws Exception {
		String uri = target.toString();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(uri, writer);
		writer.append(quote_Char);
	}
}
