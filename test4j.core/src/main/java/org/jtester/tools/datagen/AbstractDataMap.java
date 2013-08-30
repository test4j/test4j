package org.jtester.tools.datagen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.jtester.json.ITypeConverter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AbstractDataMap extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	/**
	 * put(String,Object[])的快捷写法
	 * 
	 * @param key
	 * @param value1
	 * @param value2
	 * @param more
	 */
	public void put(String key, Object value1, Object value2, Object... more) {
		List<Object> list = new ArrayList<Object>();
		list.add(value1);
		list.add(value2);
		for (Object item : more) {
			list.add(item);
		}
		super.put(key, list.toArray(new Object[0]));
	}

	public AbstractDataMap convert(String key, ITypeConverter converter) {
		Object value = this.get(key);
		if (value instanceof String && converter.accept(value)) {
			value = converter.convert((String) value);
			this.put(key, value);
		} else if (value instanceof List) {
			value = this.conveterList((List) value, converter);
			this.put(key, value);
		}
		return this;
	}

	private List conveterList(List list, ITypeConverter converter) {
		List value = new ArrayList();
		for (Object item : list) {
			if (item instanceof String) {
				Object o = converter.convert((String) item);
				value.add(o);
			}
		}
		return value;
	}

	/**
	 * 批量转换DataMap列表
	 * 
	 * @param list
	 * @param key
	 * @param converter
	 */
	public static void convert(Collection list, String key, ITypeConverter converter) {
		for (Object dm : list) {
			if (dm instanceof AbstractDataMap) {
				((AbstractDataMap) dm).convert(key, converter);
			}
		}
	}

	public static void convert(Object[] array, String key, ITypeConverter converter) {
		for (Object dm : array) {
			if (dm instanceof AbstractDataMap) {
				((AbstractDataMap) dm).convert(key, converter);
			}
		}
	}
}
