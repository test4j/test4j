package org.jtester.json.helper;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jtester.json.JSONException;
import org.jtester.tools.commons.StringHelper;

@SuppressWarnings({ "rawtypes" })
public class JSONMap extends LinkedHashMap<JSONObject, JSONObject> implements JSONObject {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	public JSONMap() {
		super(DEFAULT_INITIAL_CAPACITY);
	}

	public JSONMap(int size) {
		super(size);
	}

	public JSONMap(Map<JSONObject, JSONObject> map) {
		super(map);
	}

	@Override
	public boolean containsKey(Object key) {
		JSONObject newkey = JSONSingle.convertJSON(key);
		return super.containsKey(newkey);
	}

	@Override
	public JSONObject get(Object key) {
		JSONObject newkey = JSONSingle.convertJSON(key);
		return super.get(newkey);
	}

	/**
	 * same as put(key,value)
	 * 
	 * @param key
	 * @param value
	 */
	public JSONObject putJSON(Object key, Object value) {
		JSONObject newkey = JSONSingle.convertJSON(key);
		JSONObject newValue = JSONSingle.convertJSON(value);
		return super.put(newkey, newValue);
	}

	private boolean isInit = false;

	private String referenceID = null;

	private Class refClazz = null;

	/**
	 * 返回对象的hashcode值
	 * 
	 * @return
	 */
	public String getReferenceID() {
		if (!this.isInit) {
			this.initClazAndRefID();
		}
		return this.referenceID;
	}

	/**
	 * 返回json对象类型名称<br>
	 * 同时记录对象的hashcode值
	 * 
	 * @param defaultClazz
	 *            默认值
	 * @return
	 */
	public Type getClazzFromJSONFProp(Type defaultClazz) {
		if (!this.isInit) {
			this.initClazAndRefID();
		}
		return this.refClazz == null ? defaultClazz : this.refClazz;
	}

	private void initClazAndRefID() {
		this.isInit = true;
		JSONObject object = this.get(JSON_ClazzFlag);
		if (object == null || !(object instanceof JSONSingle)) {
			return;
		}
		JSONSingle value = (JSONSingle) object;
		String clazzName = value.toClazzName();
		this.referenceID = value.toReferenceID();
		if (StringHelper.isBlankOrNull(clazzName)) {
			this.refClazz = null;
		} else {
			this.refClazz = ClazzMap.getClazzType(clazzName.trim());
		}
	}

	/**
	 * 返回json字符串中value:xxx中xxx表示JSON对象
	 * 
	 * @return
	 */
	public JSONObject getValueFromJSONProp() {
		JSONObject value = this.get(JSON_ValueFlag);
		return value;
	}

	/**
	 * 返回对象的引用地址hascode<br>
	 * 如果json没有记录则返回null
	 * 
	 * @return
	 */
	public String getReferFromJSONProp() {
		JSONObject object = (JSONSingle) this.get(JSON_ReferFlag);
		if (object == null) {
			return null;
		} else if (!(object instanceof JSONSingle)) {
			throw new JSONException("the object reference value can only be JSONSingle type.");
		}

		JSONSingle value = (JSONSingle) object;
		String referenceID = value.toStringValue();
		return referenceID;
	}

	public String description() {
		return toString();
	}
}
