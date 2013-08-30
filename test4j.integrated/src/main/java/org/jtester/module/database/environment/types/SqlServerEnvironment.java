package org.jtester.module.database.environment.types;

import org.jtester.module.database.environment.BaseEnvironment;
import org.jtester.module.database.utility.DataSourceType;

public class SqlServerEnvironment extends BaseEnvironment {

	public SqlServerEnvironment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.SQLSERVER, dataSourceName, dataSourceFrom);
		this.typeMap = null;// TODO
	}

	public String getFieldQuato() {
		return "";
	}
}
