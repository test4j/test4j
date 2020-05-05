package cn.org.atool.fluent.mybatis.generator.demo;

import cn.org.atool.fluent.mybatis.generator.demo.mapping.*;

/**
 *
 * @author: generate code
 */
public interface ITable {

    String t_address = ${obj.mp}.Table_Name;

    String t_user = ${obj.mp}.Table_Name;

    String t_no_primary = ${obj.mp}.Table_Name;

    String t_no_auto_id = ${obj.mp}.Table_Name;
}