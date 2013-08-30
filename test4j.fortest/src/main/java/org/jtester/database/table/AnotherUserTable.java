package org.jtester.database.table;

import org.jtester.module.ICore.DataMap;

/**
 * 数据库 another_user 表数据准备或验证
 * 
 * @author darui.wudr 2013-1-8 下午5:33:49
 */
@SuppressWarnings("serial")
public class AnotherUserTable extends DataMap {
    public static interface IColumn {
        final String f_id   = "id";
        final String f_name = "name";
        final String f_age  = "age";
    }
}
