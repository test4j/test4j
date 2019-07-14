package org.test4j.module.database.environment.types;

import org.test4j.module.database.environment.BaseEnvironment;
import org.test4j.module.database.environment.typesmap.MySQLTypeMap;
import org.test4j.module.database.utility.DataSourceType;

public class MySqlEnvironment extends BaseEnvironment {
	public MySqlEnvironment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.MYSQL, dataSourceName, dataSourceFrom);
		typeMap = new MySQLTypeMap();
	}

	public String getFieldQuato() {
		return "`";
	}
}
