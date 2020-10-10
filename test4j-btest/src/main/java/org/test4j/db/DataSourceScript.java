package org.test4j.db;

import org.test4j.db.dm.AddressDataMap;
import org.test4j.db.dm.UserDataMap;
import org.test4j.module.database.IDataSourceScript;

import java.util.List;

/**
 * 生成内存数据库（h2)脚本
 *
 * @author darui.wu
 * @create 2019-09-02 18:03
 */
public class DataSourceScript implements IDataSourceScript {
    @Override
    public List<Class> getTableKlass() {
        return list(
                AddressDataMap.class,
                UserDataMap.class
        );
    }

    @Override
    public IndexList getIndexList() {
        return new IndexList();
    }
}