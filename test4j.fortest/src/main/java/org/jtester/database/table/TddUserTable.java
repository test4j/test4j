package org.jtester.database.table;

import org.jtester.module.ICore.DataMap;

/**
 * 数据库 tdd_users 表数据准备或验证
 * 
 * @author darui.wudr 2013-1-8 下午5:33:49
 */
@SuppressWarnings("serial")
public class TddUserTable extends DataMap {
    public static interface IColumn {
        final String f_id           = "id";
        final String f_first_name   = "first_name";
        final String f_last_name    = "last_name";
        final String f_nick_name    = "nick_name";
        final String f_post_code    = "post_code";
        final String f_sarary       = "sarary";
        final String f_address_id   = "address_id";
        final String f_email        = "email";
        final String f_gmt_created  = "gmt_created";
        final String f_creator      = "creator";
        final String f_gmt_modified = "gmt_modified";
        final String f_modifior     = "modifior";
        final String f_is_deleted   = "is_deleted";
    }
}
