package org.test4j.module.database.environment.types;

import org.test4j.module.database.environment.BaseEnvironment;
import org.test4j.module.database.environment.typesmap.OracleTypeMap;
import org.test4j.module.database.utility.DataSourceType;

public class OracleEnvironment extends BaseEnvironment {
	public OracleEnvironment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.ORACLE, dataSourceName, dataSourceFrom);
		typeMap = new OracleTypeMap();
	}

	public String getFieldQuato() {
		return "\"";
	}

	/**
	 * {@inheritDoc} <br>
	 * <br>
	 * 在oracle中将java.util.Date转为java.sql.Date对象
	 */
	@Override
	public Object convertToSqlValue(Object value) {
		if (value != null && "java.util.Date".equals(value.getClass().getName())) {
			return new java.sql.Date(((java.util.Date) value).getTime());
		} else {
			return super.convertToSqlValue(value);
		}
	}
}
