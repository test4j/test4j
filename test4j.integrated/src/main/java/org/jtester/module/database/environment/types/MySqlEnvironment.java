package org.jtester.module.database.environment.types;

import org.jtester.module.database.environment.BaseEnvironment;
import org.jtester.module.database.environment.typesmap.MySQLTypeMap;
import org.jtester.module.database.utility.DataSourceType;

public class MySqlEnvironment extends BaseEnvironment {
	public MySqlEnvironment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.MYSQL, dataSourceName, dataSourceFrom);
		typeMap = new MySQLTypeMap();
	}

	public String getFieldQuato() {
		return "`";
	}
}
