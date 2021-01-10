package org.test4j;

import org.test4j.module.database.dbop.DBOperator;
import org.test4j.module.database.dbop.IDBOperator;
import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.datagen.AbstractDataMap;

import javax.sql.DataSource;
import java.util.List;

/**
 * 一些测试中常用到的方法快捷入口
 */
public interface IDatabase {
    /**
     * 创建test4j数据源
     *
     * @param dataSource
     * @return
     */
    default DataSource createDataSource(String dataSource) {
        return DataSourceCreatorFactory.createDataSource(dataSource);
    }

    IDBOperator db = new DBOperator();

    /**
     * 指定数据库执行
     *
     * @param dataSourceName
     * @return
     */
    default IDBOperator db(String dataSourceName) {
        return new DBOperator(dataSourceName);
    }
}