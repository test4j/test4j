package org.test4j.module.database.dbop;

import org.test4j.tools.datagen.IDataMap;

/**
 * 往数据库插入数据操作接口
 *
 * @author darui.wudr 2013-1-11 下午5:31:57
 */
public interface IInsertOp {
    /**
     * 插入数据操作
     *
     * @throws Exception
     */
    void insert(String table, IDataMap data) throws Exception;
}