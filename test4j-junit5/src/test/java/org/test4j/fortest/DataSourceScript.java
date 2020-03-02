package org.test4j.fortest;

import org.test4j.db.datamap.table.AddressTableMap;
import org.test4j.db.datamap.table.UserTableMap;
import org.test4j.module.database.IDataSourceScript;

/**
 * 生成内存数据库（h2)脚本
 *
 * @author darui.wu
 * @create 2019-09-02 18:03
 */
public class DataSourceScript implements IDataSourceScript {
    @Override
    public Class[] getTableKlass() {
        return new Class[]{
                AddressTableMap.class,
                UserTableMap.class
        };
    }

    @Override
    public IndexList getIndexList() {
        return new IndexList();
    }

    static {
        SPEC_TYPES.put("json", "text");
    }
}