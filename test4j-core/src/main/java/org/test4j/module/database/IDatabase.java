package org.test4j.module.database;

import org.test4j.module.core.internal.ICoreInitial;
import org.test4j.module.database.dbop.DBOperator;
import org.test4j.module.database.dbop.IDBOperator;

/**
 * IDatabase
 *
 * @author wudarui
 */
public interface IDatabase {
    IDBOperator db = ICoreInitial.initDBOperator();

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
