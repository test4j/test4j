package org.test4j.module.database.environment.types;

import org.test4j.module.database.environment.BaseEnvironment;
import org.test4j.module.database.utility.DataSourceType;

public class SqlServerEnvironment extends BaseEnvironment {

	public SqlServerEnvironment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.SqlServer, dataSourceName, dataSourceFrom);
		this.typeMap = null;// TODO
	}

	public String getFieldQuota() {
		return "";
	}
}
