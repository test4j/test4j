package org.test4j.module.database.environment.types;

import org.test4j.module.database.environment.BaseEnvironment;
import org.test4j.module.database.utility.DataSourceType;

public class DB2Environment extends BaseEnvironment {

	public DB2Environment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.DB2, dataSourceName, dataSourceFrom);
		this.typeMap = null;
	}

	@Override
	public String getFieldQuota() {
		return "";
	}
}
