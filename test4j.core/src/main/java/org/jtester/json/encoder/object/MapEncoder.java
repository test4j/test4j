package org.jtester.json.encoder.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jtester.json.encoder.PropertyEncoder;
import org.jtester.json.encoder.ObjectEncoder;
import org.jtester.json.helper.JSONFeature;


@SuppressWarnings("rawtypes")
public class MapEncoder<M extends Map> extends ObjectEncoder<M> {
	public MapEncoder(Class clazz) {
		super(clazz);
	}

	@Override
	protected Collection<PropertyEncoder> getPropertyEncoders(M map) {
		List<PropertyEncoder> list = new ArrayList<PropertyEncoder>();
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			boolean skipNull = JSONFeature.isEnabled(features, JSONFeature.SkipNullValue);
			if (value == null && skipNull) {
				continue;
			}
			PropertyEncoder encoder = PropertyEncoder.newInstance(key, value, features);
			list.add(encoder);
		}
		return list;
	}
}
