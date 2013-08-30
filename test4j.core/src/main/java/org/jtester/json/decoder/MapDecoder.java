package org.jtester.json.decoder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.jtester.json.JSON;
import org.jtester.json.decoder.base.MapPoJoBaseDecoder;
import org.jtester.json.helper.JSONMap;
import org.jtester.json.helper.JSONObject;
import org.jtester.tools.commons.ClazzHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapDecoder extends MapPoJoBaseDecoder<Map> {
	public static final MapDecoder toMAP = new MapDecoder();

	@Override
	protected void parseFromJSONMap(Map target, JSONMap jsonMap, Map<String, Object> references) {

		for (Map.Entry<JSONObject, JSONObject> entry : jsonMap.entrySet()) {
			JSONObject jsonkey = entry.getKey();
			if (jsonkey.equals(JSONMap.JSON_ClazzFlag)) {
				continue;
			}
			JSONObject jsonvalue = entry.getValue();

			Object key = JSON.toObject(jsonkey, references);
			Object value = JSON.toObject(jsonvalue, references);
			target.put(key, value);
		}
	}

	@Override
	protected Map getTarget(JSONMap map, Type toType) {
		Class claz = this.getTargetType(toType, map);
		if (Object.class.equals(claz)) {
			return new HashMap();
		}
		try {
			Constructor constructor = claz.getConstructor();
			if (constructor != null) {
				Object value = ClazzHelper.newInstance(claz);
				return (Map) value;
			} else {
				return new HashMap();
			}
		} catch (Exception e) {
			return new HashMap();
		}
	}

	/**
	 * Map类型或者没有指定PoJo类型的Object
	 */
	public boolean accept(Type type) {
		Class claz = this.getRawType(type, null);
		return Map.class.isAssignableFrom(claz) || Object.class.equals(type);
	}
}
