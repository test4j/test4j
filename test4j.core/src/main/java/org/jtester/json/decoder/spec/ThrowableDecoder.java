package org.jtester.json.decoder.spec;

import java.lang.reflect.Type;
import java.util.Map;

import org.jtester.json.helper.JSONMap;

public class ThrowableDecoder extends SpecDecoder {

	public static final StackTraceElement castToStackTraceElement(Map<String, Object> map) {
		String declaringClass = (String) map.get("className");
		String methodName = (String) map.get("methodName");
		String fileName = (String) map.get("fileName");
		int lineNumber;
		{
			Number value = (Number) map.get("lineNumber");
			if (value == null) {
				lineNumber = 0;
			} else {
				lineNumber = value.intValue();
			}
		}

		return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
	}

	public boolean accept(Type type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T decodeFrom(JSONMap map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
