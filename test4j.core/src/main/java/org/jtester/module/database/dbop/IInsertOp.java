package org.jtester.module.database.dbop;

import org.jtester.module.ICore.DataMap;

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
    void insert(String table, DataMap data) throws Exception;
}
