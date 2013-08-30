package org.jtester.database.table;

import org.jtester.module.ICore.DataMap;

/**
 * 数据库 tdd_address 表数据准备或验证
 * 
 * @author darui.wudr 2013-1-8 下午5:33:49
 */
@SuppressWarnings("serial")
public class TddAddressTable extends DataMap {
    public static interface IColumn {
        final String f_id            = "id";
        final String f_create_date   = "create_date";
        final String f_creator       = "creator";
        final String f_modified_date = "modified_date";
        final String f_modifior      = "modifior";
        final String f_is_deleted    = "is_deleted";
        final String f_address       = "address";
        final String f_country       = "country";
        final String f_city          = "city";
        final String f_postcode      = "postcode";
        final String f_province      = "province";
        final String f_user_id       = "user_id";
    }
}
