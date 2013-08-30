package org.jtester.json.decoder.base;

import java.lang.reflect.Type;
import java.util.Map;

import org.jtester.json.helper.JSONMap;
import org.jtester.json.helper.JSONObject;
import org.jtester.json.helper.JSONSingle;

@SuppressWarnings("unchecked")
public abstract class FixedTypeDecoder extends BaseDecoder {

	public <T> T decode(JSONObject json, Type type, Map<String, Object> references) {
		if (json == null) {
			return null;
		}

		if (json instanceof JSONMap) {
			Object value = this.decodeFromJSONMap((JSONMap) json);
			return (T) value;
		} else if (json instanceof JSONSingle) {
			Object value = this.decodeFromJSONSingle((JSONSingle) json);
			return (T) value;
		} else {
			throw new DecoderException("syntax error, JSONObject of Single Type can't be JSONArray.");
		}
	}

	protected final <T> T decodeFromJSONSingle(JSONSingle single) {
		String value = single.toStringValue();
		if (value == null) {
			return null;
		}
		Object object = this.decodeFromString(value);
		return (T) object;
	}

	protected <T> T decodeFromJSONMap(JSONMap map) {
		JSONObject jsonObject = map.getValueFromJSONProp();
		if (jsonObject instanceof JSONSingle) {
			Object o = this.decodeFromJSONSingle((JSONSingle) jsonObject);
			return (T) o;
		} else {
			throw new DecoderException("illegal syntax.");
		}
	}

	protected abstract <T> T decodeFromString(String value);
}
