package org.test4j.module.database;

import javax.sql.DataSource;

public interface IDataSourceCreator {
    /**
     * 构建数据源
     *
     * @param dataSourceName 数据源名称
     * @return
     */
    DataSource createDataSource(String dataSourceName);
}