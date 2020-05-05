package cn.org.atool.fluent.mybatis.generator.demo;

import cn.org.atool.fluent.mybatis.generator.demo.mapping.*;

/**
 *
 * @author: generate code
 */
public interface ITable {

    String t_address = AddressMP.Table_Name;

    String t_t_user = UserMP.Table_Name;

    String t_no_primary = NoPrimaryMP.Table_Name;

    String t_no_auto_id = NoAutoIdMP.Table_Name;
}