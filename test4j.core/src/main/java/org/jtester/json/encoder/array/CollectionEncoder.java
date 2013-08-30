package org.jtester.json.encoder.array;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jtester.json.encoder.ArrayEncoder;
import org.jtester.json.encoder.JSONEncoder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollectionEncoder extends ArrayEncoder<Collection> {

	public CollectionEncoder(Class clazz) {
		super(clazz);
	}

	public CollectionEncoder() {
		super(ArrayList.class);
	}

	@Override
	protected void encodeIterator(Collection target, Writer writer, List<String> references) throws Exception {
		boolean isFirst = true;
		for (Iterator it = target.iterator(); it.hasNext();) {
			if (isFirst) {
				isFirst = false;
			} else {
				writer.append(',');
			}
			Object item = it.next();
			boolean isNullOrRef = this.writerNullOrReference(item, writer, references, false);
			if (isNullOrRef == false) {
				JSONEncoder baseEncoder = JSONEncoder.get(item.getClass());
				baseEncoder.setFeatures(this.features);
				baseEncoder.encode(item, writer, references);
			}
		}
	}
}
