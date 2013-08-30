package org.jtester.module.database.environment.typesmap;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.jtester.module.database.environment.typesmap.TypeMap.JavaSQLType;

@SuppressWarnings({ "rawtypes", "serial" })
public class MySQLTypeMap extends AbstractTypeMap {
	private static TypeMap maps = new TypeMap() {
		private static final long serialVersionUID = -2101070752077610108L;

		{
			this.put("VARCHAR", String.class, Types.VARCHAR);
			this.put("CHAR", String.class, Types.VARCHAR);

			this.put("TEXT", String.class, Types.VARCHAR);
			this.put("TINYTEXT", String.class, Types.BLOB);
			this.put("MEDIUMTEXT", String.class, Types.BLOB);
			this.put("LONGTEXT", String.class, Types.BLOB);

			this.put("TINYINT", Integer.class, Types.INTEGER);
			this.put("TINYINT UNSIGNED", Integer.class, Types.INTEGER);

			this.put("SMALLINT", Integer.class, Types.INTEGER);
			this.put("SMALLINT UNSIGNED", Integer.class, Types.INTEGER);

			this.put("MEDIUMINT", Integer.class, Types.INTEGER);
			this.put("MEDIUMINT UNSIGNED", Integer.class, Types.INTEGER);

			this.put("INT", Integer.class, Types.INTEGER);
			this.put("INT UNSIGNED", Integer.class, Types.INTEGER);

			this.put("INTEGER", Integer.class, Types.INTEGER);
			this.put("INTEGER UNSIGNED", Integer.class, Types.INTEGER);

			this.put("BIGINT", Long.class, Types.BIGINT);
			this.put("BIGINT UNSIGNED", Long.class, Types.BIGINT);

			this.put("FLOAT", Float.class, Types.FLOAT);
			this.put("FLOAT UNSIGNED", Float.class, Types.FLOAT);

			this.put("DOUBLE", Double.class, Types.DOUBLE);
			this.put("DOUBLE UNSIGNED", Double.class, Types.DOUBLE);

			this.put("DECIMAL", BigDecimal.class, Types.NUMERIC);
			this.put("DECIMAL UNSIGNED", BigDecimal.class, Types.NUMERIC);

			this.put("DEC", BigDecimal.class, Types.NUMERIC);
			this.put("DEC UNSIGNED", BigDecimal.class, Types.NUMERIC);

			this.put("DATE", java.sql.Date.class, Types.DATE);
			this.put("TIMESTAMP", java.sql.Timestamp.class, Types.TIMESTAMP);
			this.put("DATETIME", java.sql.Timestamp.class, Types.TIMESTAMP);
			this.put("YEAR", Integer.class, Types.INTEGER);

			this.put("BIT", Boolean.class, Types.BOOLEAN);
			// cobar extended
			this.put("CHARACTER VARYING", String.class, Types.VARCHAR);
			this.put("NUMERIC", Double.class, Types.NUMERIC);
			this.put("DOUBLE PRECISION", Double.class, Types.DOUBLE);
			this.put("CHARACTER", String.class, Types.CHAR);
			this.put("TIME", java.sql.Time.class, Types.TIME);
			this.put("BOOLEAN", Boolean.class, Types.BIT);
			this.put("TIMESTAMP WITH TIME ZONE", java.sql.Date.class, Types.DATE);
			this.put("TIMESTAMP WITHOUT TIME ZONE", java.sql.Date.class, Types.DATE);
			this.put("TIME WITH TIME ZONE", java.sql.Time.class, Types.TIME);
			this.put("TIME WITHOUT TIME ZONE", java.sql.Time.class, Types.TIME);

			this.put("ENUM", String.class, Types.VARCHAR);
			this.put("SET", String.class, Types.VARCHAR);

			this.put("BLOB", String.class, Types.BLOB);
			this.put("LONGBLOB", String.class, Types.BLOB);
			this.put("MEDIUMBLOB", String.class, Types.BLOB);
			this.put("TINYBLOB", String.class, Types.BLOB);

			this.put("BINARY", InputStream.class, Types.BINARY);
			this.put("VARBINARY", InputStream.class, Types.BINARY);
		}
	};

	public static Class getJavaType(String type) {
		JavaSQLType map = maps.get(type);
		return map == null ? null : map.getJavaType();
	}

	public static Integer getSQLType(String type) {
		JavaSQLType map = maps.get(type);
		return map == null ? null : map.getSqlType();
	}

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
