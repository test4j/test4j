package org.test4j.module.database.environment.typesmap;

import oracle.sql.TIMESTAMP;
import org.test4j.tools.commons.DateHelper;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "serial" })
public class OracleTypeMap extends AbstractTypeMap {

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