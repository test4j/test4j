package org.test4j.module.database.environment.typesmap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "serial" })
public class MySQLTypeMap extends AbstractTypeMap {

	private static Map<String, Class> types = new HashMap<String, Class>() {
		{
			// TODO
		}
	};

	@Override
	protected Class getJavaTypeByName(String typeName) {
		Class type = types.get(typeName);
		return type == null ? String.class : type;
	}

	@Override
	protected Object toObjectByType(String input, Class javaType) {
		return input;
	}

	@Override
	protected Object getDefaultValue(Class javaType) {
		return null;
	}
}