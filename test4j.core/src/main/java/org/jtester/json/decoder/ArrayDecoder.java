package org.jtester.json.decoder;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jtester.json.JSONException;
import org.jtester.json.decoder.base.BaseDecoder;
import org.jtester.json.decoder.base.DecoderException;
import org.jtester.json.decoder.base.DecoderFactory;
import org.jtester.json.helper.JSONArray;
import org.jtester.json.helper.JSONMap;
import org.jtester.json.helper.JSONObject;
import org.jtester.tools.commons.PrimitiveHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ArrayDecoder extends BaseDecoder {
	public static final BaseDecoder toARRAY = new ArrayDecoder();

	public <T> T decode(JSONObject json, Type toType, Map<String, Object> references) {
		if (json == null) {
			return null;
		}
		if (json instanceof JSONMap) {
			Object o = this.parseFromJSONMap((JSONMap) json, toType, references);
			return (T) o;
		} else if (json instanceof JSONArray) {
			List list = this.parseFromJSONArray(((JSONArray) json), toType, references);
			Object[] a = this.getArray(toType);
			return (T) list.toArray(a);
		} else {
			throw new DecoderException(
					"illegal type for ArrayDecoder. the type can only is JSONArray or JSONMap, but actual is JSONSingle.");
		}
	}

	private <T> T parseFromJSONMap(JSONMap map, Type toType, Map<String, Object> references) {
		String referenceID = map.getReferFromJSONProp();
		if (referenceID != null) {
			Object o = references.get(referenceID);
			return (T) o;
		}

		Type type = map.getClazzFromJSONFProp(toType);
		if (this.accept(type) == false) {
			throw new JSONException("JSONMap must have property that declared the array type.");
		}

		JSONObject array = map.getValueFromJSONProp();
		if (!(array instanceof JSONArray)) {
			throw new JSONException("illegal type for ArrayDecoder. the type can only be JSONArray, but actual is "
					+ array.getClass().getName());
		}

		List list = this.parseFromJSONArray(((JSONArray) array), toType, references);
		Object[] a = this.getArray(toType);
		Object oa = list.toArray(a);
		String newID = map.getReferenceID();
		if (newID != null) {
			references.put(newID, oa);
		}
		return (T) oa;
	}

	private final List parseFromJSONArray(JSONArray jsonArray, Type toType, Map<String, Object> references) {
		List list = new ArrayList();
		for (Iterator<JSONObject> it = jsonArray.iterator(); it.hasNext();) {
			JSONObject jsonObject = it.next();
			Type componentType = this.getComponent(jsonObject, toType);
			IDecoder decoder = DecoderFactory.getDecoder(componentType);
			Object o = decoder.decode(jsonObject, componentType, references);
			list.add(o);
		}
		return list;
	}

	public boolean accept(Type type) {
		if (type instanceof Class) {
			return ((Class) type).isArray();
		} else if (type instanceof GenericArrayType) {
			return true;
		} else {
			return false;
		}
	}

	private Object[] getArray(Type type) {
		while (type instanceof GenericArrayType) {
			type = ((GenericArrayType) type).getGenericComponentType();
		}

		if (!(type instanceof Class)) {
			return new String[0];
		}
		int length = 0;
		Class argClaz = (Class) type;
		while (argClaz.getComponentType() != null) {
			argClaz = argClaz.getComponentType();
			length++;
		}
		int[] dimensions = new int[length];
		for (int index = 0; index < length; index++) {
			dimensions[index] = 0;
		}
		argClaz = PrimitiveHelper.getPrimitiveBoxType(argClaz);
		return (Object[]) Array.newInstance(argClaz, dimensions);
	}

	private Type getComponent(Type toType) {
		if (toType instanceof Class) {
			Class claz = ((Class) toType).getComponentType();
			return Object.class.equals(claz) ? HashMap.class : claz;
		} else if (toType instanceof GenericArrayType) {
			return ((GenericArrayType) toType).getGenericComponentType();
		} else {
			throw new DecoderException("the ArrayDecoder only accpt array type, but actual is:" + toType);
		}
	}

	private Type getComponent(JSONObject jo, Type toType) {
		Type argType = this.getComponent(toType);
		if (!(jo instanceof JSONMap)) {
			return argType;
		}
		Type type = ((JSONMap) jo).getClazzFromJSONFProp(argType);
		return type;
	}
}
