package org.test4j.module.database.environment.types;

import org.test4j.module.database.environment.BaseEnvironment;

public class SqlServerEnvironment extends BaseEnvironment {

    public SqlServerEnvironment(String dataSourceName) {
        super(dataSourceName);
    }

    public String getFieldQuota() {
        return "";
    }
}