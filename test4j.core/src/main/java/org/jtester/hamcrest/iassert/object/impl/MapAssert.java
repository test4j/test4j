package org.jtester.hamcrest.iassert.object.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jtester.hamcrest.iassert.common.impl.AllAssert;
import org.jtester.hamcrest.iassert.object.intf.IMapAssert;
import org.jtester.hamcrest.matcher.array.MapMatcher;
import org.jtester.hamcrest.matcher.array.MapMatcher.MapMatcherType;

import ext.jtester.hamcrest.Matcher;
import ext.jtester.hamcrest.collection.IsMapContaining;
import ext.jtester.hamcrest.core.AllOf;

@SuppressWarnings({ "rawtypes" })
public class MapAssert extends AllAssert<Map<?, ?>, IMapAssert> implements IMapAssert {
	public MapAssert() {
		super(IMapAssert.class);
		this.valueClaz = Map.class;
	}

	public MapAssert(Map<?, ?> map) {
		super(map, IMapAssert.class);
		this.valueClaz = Map.class;
	}

	public IMapAssert hasKeys(Object key, Object... keys) {
		MapMatcher matcher1 = new MapMatcher(key, MapMatcherType.KEY);
		if (keys == null || keys.length == 0) {
			return assertThat(matcher1);
		}
		List<Matcher> list = new ArrayList<Matcher>();
		list.add(matcher1);
		for (Object item : keys) {
			list.add(new MapMatcher(item, MapMatcherType.KEY));
		}
		return assertThat(AllOf.allOf(list));
	}

	public IMapAssert hasValues(Object value, Object... values) {
		MapMatcher matcher1 = new MapMatcher(value, MapMatcherType.VALUE);
		if (values == null || values.length == 0) {
			return assertThat(matcher1);
		}
		List<Matcher> list = new ArrayList<Matcher>();
		list.add(matcher1);
		for (Object item : values) {
			list.add(new MapMatcher(item, MapMatcherType.VALUE));
		}
		return assertThat(AllOf.allOf(list));
	}

	public IMapAssert hasEntry(Object key, Object value, Object... objects) {
		Matcher<?> matcher = IsMapContaining.hasEntry(key, value);
		if (objects == null) {
			return assertThat(matcher);
		}
		int size = objects.length;
		List<Matcher> list = new ArrayList<Matcher>();
		list.add(matcher);
		for (int index = 0; index < size / 2; index++) {
			Matcher<?> matcher2 = IsMapContaining.hasEntry(objects[index * 2], objects[index * 2 + 1]);
			list.add(matcher2);
		}

		return assertThat(AllOf.allOf(list));
	}

	public IMapAssert hasEntry(Entry<?, ?> entry, Entry<?, ?>... entries) {
		Matcher<?> matcher = IsMapContaining.hasEntry(entry.getKey(), entry.getValue());
		if (entries == null) {
			return assertThat(matcher);
		}
		List<Matcher> list = new ArrayList<Matcher>();
		list.add(matcher);
		for (Map.Entry<?, ?> item : entries) {
			Matcher<?> matcher2 = IsMapContaining.hasEntry(item.getKey(), item.getValue());
			list.add(matcher2);
		}

		return assertThat(AllOf.allOf(list));
	}
}
