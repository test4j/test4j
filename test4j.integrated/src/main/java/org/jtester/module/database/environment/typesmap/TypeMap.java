package org.jtester.module.database.environment.typesmap;

import java.util.HashMap;

import org.jtester.module.database.environment.typesmap.TypeMap.JavaSQLType;

@SuppressWarnings("rawtypes")
public class TypeMap extends HashMap<String, JavaSQLType> {
	private static final long serialVersionUID = -8446876368817445261L;

	public void put(String type, Class javaType, int sqlType) {
		this.put(type, new JavaSQLType(javaType, sqlType));
	}

	public Class getJavaType(String type) {
		JavaSQLType javaSql = this.get(type);
		if (javaSql == null) {
			return null;
		} else {
			return javaSql.javaType;
		}
	}

	public int getSQLType(String type) {
		JavaSQLType javaSql = this.get(type);
		if (javaSql == null) {
			return -1;
		} else {
			return javaSql.sqlType;
		}
	}

	public static class JavaSQLType {
		Class javaType;

		int sqlType;

		public JavaSQLType(Class javaType, int sqlType) {
			this.javaType = javaType;
			this.sqlType = sqlType;
		}

		public Class getJavaType() {
			return javaType;
		}

		public int getSqlType() {
			return sqlType;
		}
	}
}
