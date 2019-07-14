package org.test4j.hamcrest.iassert.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.AllOf;
import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IMapAssert;
import org.test4j.hamcrest.matcher.array.MapMatcher;
import org.test4j.hamcrest.matcher.array.MapMatcher.MapMatcherType;


@SuppressWarnings({"rawtypes"})
public class MapAssert extends Assert<Map, IMapAssert> implements IMapAssert {
    public MapAssert() {
        super(Map.class, IMapAssert.class);
    }

    public MapAssert(Map map) {
        super(map, Map.class, IMapAssert.class);
    }

    @Override
    public IMapAssert hasKeys(Object key, Object... keys) {
        return this.hasKeyOrValues(MapMatcherType.KEY, key, keys);

    }

    @Override
    public IMapAssert hasValues(Object value, Object... values) {
        return this.hasKeyOrValues(MapMatcherType.VALUE, value, values);
    }

    private IMapAssert hasKeyOrValues(MapMatcherType type, Object item, Object... items) {
        Matcher matcher1 = new MapMatcher(item, type);
        if (items == null || items.length == 0) {
            return assertThat(matcher1);
        }
        List list = new ArrayList();
        list.add(matcher1);
        for (Object _item : items) {
            list.add(new MapMatcher(_item, type));
        }
        return assertThat(AllOf.allOf(list));
    }

    public IMapAssert hasEntry(Object key, Object value, Object... objects) {
        Matcher<?> matcher = IsMapContaining.hasEntry(key, value);
        if (objects == null) {
            return assertThat(matcher);
        }
        int size = objects.length;
        List list = new ArrayList<Matcher>();
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
        List list = new ArrayList();
        list.add(matcher);
        for (Map.Entry<?, ?> item : entries) {
            Matcher<?> matcher2 = IsMapContaining.hasEntry(item.getKey(), item.getValue());
            list.add(matcher2);
        }

        return assertThat(AllOf.allOf(list));
    }
}
