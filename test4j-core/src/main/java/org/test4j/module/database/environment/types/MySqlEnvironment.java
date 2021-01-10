package org.test4j.module.database.environment.types;

import org.test4j.module.database.environment.BaseEnvironment;
import org.test4j.module.database.environment.typesmap.MySQLTypeMap;

public class MySqlEnvironment extends BaseEnvironment {
	public MySqlEnvironment(String dataSourceName) {
		super(dataSourceName);
		typeMap = new MySQLTypeMap();
	}

	public String getFieldQuota() {
		return "`";
	}
}