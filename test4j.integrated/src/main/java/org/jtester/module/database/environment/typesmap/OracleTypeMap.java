package org.jtester.module.database.environment.typesmap;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import oracle.jdbc.driver.OracleTypes;
import oracle.sql.BLOB;
import oracle.sql.TIMESTAMP;

import org.jtester.module.database.environment.typesmap.TypeMap.JavaSQLType;
import org.jtester.tools.commons.DateHelper;

@SuppressWarnings({ "rawtypes", "serial" })
public class OracleTypeMap extends AbstractTypeMap {
	private static TypeMap maps = new TypeMap() {
		private static final long serialVersionUID = 4142728734422012716L;

		{
			this.put("VARCHAR", String.class, Types.VARCHAR);
			this.put("VARCHAR2", String.class, Types.VARCHAR);
			this.put("NVARCHAR2", String.class, Types.VARCHAR);
			this.put("CHAR", String.class, Types.VARCHAR);
			this.put("NCHAR", String.class, Types.VARCHAR);
			// this.put("CLOB", CLOB.class, Types.CLOB);
			this.put("CLOB", String.class, Types.CLOB);
			this.put("BLOB", BLOB.class, Types.BLOB);
			this.put("RAW", byte[].class, Types.BINARY);

			this.put("ROWID", String.class, Types.VARCHAR);
			this.put("BINARY_INTEGER", BigDecimal.class, Types.NUMERIC);
			this.put("NUMBER", BigDecimal.class, Types.NUMERIC);
			this.put("FLOAT", BigDecimal.class, Types.NUMERIC);
			this.put("DATE", Timestamp.class, Types.TIMESTAMP);
			this.put("TIMESTAMP", Timestamp.class, Types.TIMESTAMP);
			this.put("REF", ResultSet.class, OracleTypes.CURSOR);

			//
			this.put("LONGVARCHAR", String.class, Types.VARCHAR);
			this.put("NUMERIC", BigDecimal.class, Types.DECIMAL);
			this.put("DECIMAL", BigDecimal.class, Types.DECIMAL);
			this.put("BIT", Boolean.class, Types.BOOLEAN);
			this.put("TINYINT", Byte.class, Types.BIT);
			this.put("SMALLINT", Short.class, Types.SMALLINT);
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

	public static Map<String, Class> types = new HashMap<String, Class>() {
		{
			// this.put("oracle.sql.CLOB", oracle.sql.CLOB.class);
			this.put("oracle.sql.CLOB", String.class);
			this.put("oracle.sql.BLOB", oracle.sql.BLOB.class);
			this.put("oracle.sql.TIMESTAMP", oracle.sql.TIMESTAMP.class);
		}
	};

	/**
	 * 将string对象转换为java对象
	 * 
	 * @param input
	 * @param javaType
	 * @return
	 */
	public Object toObjectByType(String input, Class javaType) {

		if (javaType == TIMESTAMP.class) {
			long time = DateHelper.parse(input).getTime();
			Timestamp stamp = new Timestamp(time);
			return new TIMESTAMP(stamp);
		}
		if (javaType == oracle.sql.CLOB.class || javaType == oracle.sql.BLOB.class) {
			InputStream is = getStream(input);
			return is;
		}

		return input;
	}

	@Override
	protected Class getJavaTypeByName(String typeName) {
		Class type = types.get(typeName);
		return type == null ? String.class : type;
	}

	@Override
	protected Object getDefaultValue(Class javaType) {
		if (javaType == TIMESTAMP.class) {
			java.util.Date now = new java.util.Date();
			Timestamp stamp = new Timestamp(now.getTime());
			return new TIMESTAMP(stamp);
		}
		if (javaType == oracle.sql.CLOB.class || javaType == oracle.sql.BLOB.class) {
			InputStream is = getStream("stream");
			return is;
		}
		return null;
	}
}
