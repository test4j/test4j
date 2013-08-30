package org.jtester.json.decoder.spec;

import java.lang.reflect.Type;
import java.util.Map;

import org.jtester.json.JSONException;
import org.jtester.json.decoder.IDecoder;
import org.jtester.json.helper.JSONMap;
import org.jtester.json.helper.JSONObject;
import org.jtester.json.helper.JSONSingle;

@SuppressWarnings("unchecked")
public abstract class SpecDecoder implements IDecoder {

	public <T> T decode(JSONObject json, Type toType, Map<String, Object> references) {
		if (json == null) {
			return null;
		}
		if (json instanceof JSONSingle) {
			String value = ((JSONSingle) json).toStringValue();
			if (value == null) {
				return null;
			} else {
				throw new JSONException("illegal type for SpecDecoder, the json[" + value + "] isn't a JSONMap.");
			}
		}
		try {
			Object o = this.decodeFrom((JSONMap) json);
			return (T) o;
		} catch (Exception e) {
			throw new JSONException(e);
		}
	}

	public abstract <T> T decodeFrom(JSONMap map) throws Exception;
}
