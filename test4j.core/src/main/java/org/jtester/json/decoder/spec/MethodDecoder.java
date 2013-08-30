package org.jtester.json.decoder.spec;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jtester.json.JSONException;
import org.jtester.json.encoder.object.spec.MethodEncoder;
import org.jtester.json.helper.JSONArray;
import org.jtester.json.helper.JSONMap;
import org.jtester.json.helper.JSONObject;
import org.jtester.json.helper.JSONSingle;
import org.jtester.tools.commons.ClazzHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MethodDecoder extends SpecDecoder {

	public static MethodDecoder toMethod = new MethodDecoder();

	@Override
	public Method decodeFrom(JSONMap map) throws Exception {
		String methodName = this.getMethodName(map);
		Class clazz = this.getDeclareClazz(map);
		Class[] parameterTypes = this.getParaTyps(map);
		Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
		return method;
	}

	private Class[] getParaTyps(JSONMap map) {
		JSONObject types = map.get(MethodEncoder.JSON_METHOD_PARATYPE);

		if (types == null) {
			return new Class[0];
		}
		if (!(types instanceof JSONArray)) {
			throw new RuntimeException("todo");
		}
		List<Class> list = new ArrayList<Class>();
		JSONArray array = (JSONArray) types;
		for (Iterator<JSONObject> it = array.iterator(); it.hasNext();) {
			JSONObject type = it.next();
			if (type == null || !(type instanceof JSONSingle)) {
				throw new JSONException("the type of method must be a String,but actual is:" + String.valueOf(type));
			}
			String typename = ((JSONSingle) type).toStringValue();
			list.add(ClazzHelper.getClazz(typename));
		}
		return list.toArray(new Class[0]);
	}

	private String getMethodName(JSONMap map) {
		JSONObject name = map.get(MethodEncoder.JSON_METHOD_NAME);
		if (name == null || !(name instanceof JSONSingle)) {
			throw new JSONException("the methodname of json must be string, but actual is " + String.valueOf(name));
		}
		String methodName = ((JSONSingle) name).toStringValue();
		return methodName;
	}

	private Class getDeclareClazz(JSONMap map) {
		JSONObject declared = map.get(MethodEncoder.JSON_METHOD_DECLAREDBY);
		if (declared == null || !(declared instanceof JSONSingle)) {
			throw new JSONException("the method declared type of json must be string, but actual is "
					+ String.valueOf(declared));
		}
		String className = ((JSONSingle) declared).toStringValue();
		return ClazzHelper.getClazz(className);
	}

	public boolean accept(Type type) {
		return Method.class.equals(type);
	}
}
