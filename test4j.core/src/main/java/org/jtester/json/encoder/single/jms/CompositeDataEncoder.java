package org.jtester.json.encoder.single.jms;

import java.io.Writer;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;

import org.jtester.json.encoder.single.FixedTypeEncoder;

public class CompositeDataEncoder<T extends CompositeData> extends FixedTypeEncoder<T> {

	protected CompositeDataEncoder() {
		super(CompositeDataSupport.class);
	}

	@Override
	protected void encodeSingleValue(T target, Writer writer) throws Exception {
		CompositeType type = target.getCompositeType();

		for (Object key : type.keySet()) {
			Object value = target.get((String) key);

			if (value == null) {
				continue;
			}

			// encode key

			// encode value
		}
	}
}
