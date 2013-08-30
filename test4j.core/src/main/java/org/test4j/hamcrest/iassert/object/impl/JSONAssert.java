package org.test4j.hamcrest.iassert.object.impl;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.test4j.hamcrest.iassert.common.impl.AllAssert;
import org.test4j.hamcrest.iassert.common.intf.IAssert;
import org.test4j.hamcrest.iassert.object.intf.ICollectionAssert;
import org.test4j.hamcrest.iassert.object.intf.IJSONAssert;
import org.test4j.hamcrest.iassert.object.intf.IMapAssert;
import org.test4j.hamcrest.iassert.object.intf.IStringAssert;
import org.test4j.json.helper.JSONArray;
import org.test4j.json.helper.JSONMap;
import org.test4j.json.helper.JSONObject;
import org.test4j.json.helper.JSONScanner;
import org.test4j.json.helper.JSONSingle;

import ext.test4j.hamcrest.Matcher;
import ext.test4j.hamcrest.MatcherAssert;
import ext.test4j.hamcrest.core.Is;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JSONAssert extends AllAssert<Object, JSONAssert> implements IJSONAssert {

	public JSONAssert(String json, Class<? extends IAssert<?, ?>> clazE) {
		super(clazE);
		JSONObject temp = JSONScanner.scnJSON(json);
		this.value = convert(temp);
		this.type = AssertType.AssertStyle;
	}

	public ICollectionAssert isJSONArray() {
		CollectionAssert collectionAssert = null;
		Matcher matcher = Is.isA(LinkedList.class);
		if (this.type == AssertType.AssertStyle) {
			if (this.value instanceof LinkedList) {
				collectionAssert = new CollectionAssert((LinkedList) this.value);
			} else {
				MatcherAssert.assertThat(this.value, matcher);
			}
		} else {
			collectionAssert = new CollectionAssert();
		}
		return collectionAssert.assertThat(matcher);
	}

	public IMapAssert isJSONMap() {
		MapAssert mapAssert = null;
		Matcher matcher = Is.isA(LinkedHashMap.class);
		if (this.type == AssertType.AssertStyle) {
			if (this.value instanceof Map) {
				mapAssert = new MapAssert((Map) this.value);
			} else {
				MatcherAssert.assertThat(this.value, matcher);
			}
		} else {
			mapAssert = new MapAssert();
		}
		return mapAssert.assertThat(matcher);
	}

	public IStringAssert isSimple() {
		StringAssert stringAssert = null;
		Matcher matcher = Is.isA(String.class);
		if (this.type == AssertType.AssertStyle) {
			if (this.value instanceof String) {
				stringAssert = new StringAssert((String) this.value);
			} else {
				MatcherAssert.assertThat(this.value, matcher);
			}
		} else {
			stringAssert = new StringAssert();
		}
		return stringAssert.assertThat(matcher);
	}

	private static Object convert(JSONObject obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof JSONSingle) {
			return ((JSONSingle) obj).toStringValue();
		}
		if (obj instanceof JSONMap) {
			return convert((JSONMap) obj);
		}
		if (obj instanceof JSONArray) {
			return convert((JSONArray) obj);
		}
		throw new RuntimeException("unknown json type:" + obj.getClass().getName());
	}

	private static LinkedHashMap convert(JSONMap map) {
		LinkedHashMap linkedMap = new LinkedHashMap();
		for (Object key : map.keySet()) {
			Object key_obj = convert((JSONObject) key);
			Object value = map.get(key);
			Object value_obj = convert((JSONObject) value);
			linkedMap.put(key_obj, value_obj);
		}
		return linkedMap;
	}

	private static LinkedList convert(JSONArray array) {
		LinkedList list = new LinkedList();
		for (JSONObject o : array) {
			Object item = convert(o);
			list.add(item);
		}
		return list;
	}

}
