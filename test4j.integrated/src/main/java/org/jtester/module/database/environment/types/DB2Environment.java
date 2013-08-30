package org.jtester.module.database.environment.types;

import org.jtester.module.database.environment.BaseEnvironment;
import org.jtester.module.database.utility.DataSourceType;

public class DB2Environment extends BaseEnvironment {

	public DB2Environment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.DB2, dataSourceName, dataSourceFrom);
		this.typeMap = null;
	}

	public String getFieldQuato() {
		return "";
	}
}
