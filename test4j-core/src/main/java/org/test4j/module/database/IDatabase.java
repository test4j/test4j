package org.test4j.module.database;

import org.test4j.module.database.dbop.DBOperator;
import org.test4j.module.database.dbop.IDBOperator;

/**
 * IDatabase
 *
 * @author wudarui
 */
public interface IDatabase {
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