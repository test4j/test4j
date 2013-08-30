package org.jtester.module.database.environment.types;

import org.jtester.module.database.environment.BaseEnvironment;
import org.jtester.module.database.utility.DataSourceType;

/**
 * Encapsulates support for the Derby database (also known as JavaDB). Operates
 * in Client mode.
 * 
 * @see EmbeddedDerbyEnvironment
 */
public class DerbyEnvironment extends BaseEnvironment {
	public DerbyEnvironment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.DERBYDB, dataSourceName, dataSourceFrom);
		this.typeMap = null;// TODO
	}

	public String getFieldQuato() {
		return "";
	}
}
