package org.jtester.json;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.json.decoder.IDecoder;
import org.jtester.json.decoder.base.DecoderFactory;
import org.jtester.json.encoder.JSONEncoder;
import org.jtester.json.helper.JSONArray;
import org.jtester.json.helper.JSONFeature;
import org.jtester.json.helper.JSONMap;
import org.jtester.json.helper.JSONObject;
import org.jtester.json.helper.JSONScanner;
import org.jtester.json.helper.JSONSingle;

/**
 * json解码，编码工具类
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class JSON {

	/**
	 * 将json字符串反序列为对象
	 * 
	 * @param json
	 * @return
	 */
	public static final <T> T toObject(String json) {
		if (json == null) {
			return null;
		}
		JSONObject jsonObject = JSONScanner.scnJSON(json);

		Object o = toObject(jsonObject, new HashMap<String, Object>());
		return (T) o;
	}

	public static final <T> T toObject(String json, Type clazz) {
		if (json == null) {
			return null;
		}
		JSONObject jsonObject = JSONScanner.scnJSON(json);

		Object o = toObject(jsonObject, clazz, new HashMap<String, Object>());
		return (T) o;
	}

	/**
	 * 将json字符串反序列为对象
	 * 
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @param decodingFeatures
	 * @return
	 */
	public static final <T> T toObject(JSONObject json, Map<String, Object> references) {
		if (json instanceof JSONArray) {
			Object o = toObject(json, Object[].class, references);
			return (T) o;
		} else if (json instanceof JSONMap) {
			Type toType = ((JSONMap) json).getClazzFromJSONFProp(HashMap.class);
			Object o = toObject(json, toType, references);
			return (T) o;
		} else {
			Object value = ((JSONSingle) json).toPrimitiveValue();
			return (T) value;
		}
	}

	public static final <T> T toObject(JSONObject json, Type type, Map<String, Object> references) {
		if (type == null) {
			throw new RuntimeException("the decode class can't be null.");
		}
		IDecoder decoder = DecoderFactory.getDecoder(type);
		Object obj = decoder.decode(json, type, references);
		return (T) obj;
	}

	/**
	 * 将对象编码为json串
	 * 
	 * @param object
	 * @return
	 */
	public static final String toJSON(Object object, JSONFeature... features) {
		if (object == null) {
			return "null";
		}
		int value = JSONFeature.getFeaturesMask(features);
		String json = toJSON(object, value);
		return json;
	}

	/**
	 * 将对象编码为json串
	 * 
	 * @param object
	 * @param features
	 * @return
	 */
	public static final String toJSON(Object object, int features) {
		if (object == null) {
			return "null";
		}

		StringWriter writer = new StringWriter();
		JSONEncoder encoder = JSONEncoder.get(object.getClass());
		encoder.setFeatures(features);

		List<String> references = new ArrayList<String>();
		encoder.encode(object, writer, references);
		String json = writer.toString();
		return json;
	}
}
