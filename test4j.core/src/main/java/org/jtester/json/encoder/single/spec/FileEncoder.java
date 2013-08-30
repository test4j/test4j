package org.jtester.json.encoder.single.spec;

import java.io.File;
import java.io.Writer;

import org.jtester.json.encoder.single.SpecTypeEncoder;
import org.jtester.json.encoder.single.fixed.StringEncoder;

@SuppressWarnings("rawtypes")
public class FileEncoder<T extends File> extends SpecTypeEncoder<T> {

	public static FileEncoder instance = new FileEncoder();

	protected FileEncoder() {
		super(File.class);
	}

	@Override
	protected void encodeSingleValue(File target, Writer writer) throws Exception {
		String file = target.getPath();
		writer.append(quote_Char);
		StringEncoder.writeEscapeString(file, writer);
		writer.append(quote_Char);
	}

	@Override
	protected void encodeOtherProperty(File target, Writer writer) throws Exception {
		// TODO Auto-generated method stub

	}
}
