package org.jtester.json.encoder.single.jms;

import java.io.Writer;
import java.util.Collection;
import java.util.List;

import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;

import org.jtester.json.encoder.single.SpecTypeEncoder;

@SuppressWarnings("rawtypes")
public class TabularDataEncoder<T extends TabularData> extends SpecTypeEncoder<T> {

	public static TabularDataEncoder instance = new TabularDataEncoder();

	protected TabularDataEncoder() {
		super(TabularDataSupport.class);
	}

	@SuppressWarnings("unused")
	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		List indexNames = target.getTabularType().getIndexNames();
		writer.append(quote_Char).append("columns").append(quote_Char).append(':');
		// TODO encode list

		writer.append(',');
		writer.append(quote_Char).append("rows").append(quote_Char).append(':');
		Collection datas = target.values();
		// TODo encode datas
	}

	@Override
	protected void encodeOtherProperty(T target, Writer writer) throws Exception {
		// TODO Auto-generated method stub

	}
}
