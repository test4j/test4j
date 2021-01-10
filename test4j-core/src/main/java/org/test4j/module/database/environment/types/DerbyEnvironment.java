package org.test4j.module.database.environment.types;

import org.test4j.module.database.environment.BaseEnvironment;

/**
 * Encapsulates support for the Derby database (also known as JavaDB). Operates
 * in Client mode.
 *
 */
public class DerbyEnvironment extends BaseEnvironment {
	public DerbyEnvironment(String dataSourceName) {
		super(dataSourceName);
		this.typeMap = null;// TODO
	}

	@Override
	public String getFieldQuota() {
		return "";
	}
}