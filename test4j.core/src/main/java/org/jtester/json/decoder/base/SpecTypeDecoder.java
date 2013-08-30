package org.jtester.json.decoder.base;

import java.lang.reflect.Type;
import java.util.Map;

import org.jtester.json.helper.JSONMap;
import org.jtester.json.helper.JSONObject;
import org.jtester.json.helper.JSONSingle;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class SpecTypeDecoder extends BaseDecoder {

	public <E> E decode(JSONObject json, Type toType, Map<String, Object> references) {
		if (json == null) {
			return null;
		}

		if (json instanceof JSONMap) {
			Object value = this.decodeFromJSONMap((JSONMap) json, toType, references);
			return (E) value;
		} else if (json instanceof JSONSingle) {
			Object value = this.decodeFromJSONSingle((JSONSingle) json, toType);
			return (E) value;
		} else {
			throw new RuntimeException("syntax error, JSONObject of Single Type can't be JSONArray.");
		}
	}

	private <E> E decodeFromJSONSingle(JSONSingle single, Type toType) {
		String value = single.toStringValue();
		if (value == null) {
			return null;
		}

		Object target = this.decodeFromString(value, toType);
		return (E) target;
	}

	private <E> E decodeFromJSONMap(JSONMap map, Type toType, Map<String, Object> references) {
		Type type = map.getClazzFromJSONFProp(toType);
		Class clazz = this.getRawType(type, null);

		JSONObject json = map.getValueFromJSONProp();
		if (!(json instanceof JSONSingle)) {
			throw new DecoderException(
					"illegal syntax, the JSONObject value of Single Type Object can only be JSONSingle.");
		}
		String value = ((JSONSingle) json).toStringValue();
		Object target = this.decodeFromString(value, clazz);
		this.fillTargetOtherProp(target, map);
		return (E) target;
	}

	protected abstract Object decodeFromString(String value, Type type);

	/**
	 * 填充非标准字段,比如用户自己扩展的属性
	 * 
	 * @param target
	 * @param map
	 */
	protected void fillTargetOtherProp(Object target, JSONMap map) {
		// do nothing TODO
	}
}
