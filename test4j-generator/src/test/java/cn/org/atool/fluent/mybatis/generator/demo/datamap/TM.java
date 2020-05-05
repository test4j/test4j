package cn.org.atool.fluent.mybatis.generator.demo.datamap;

import cn.org.atool.fluent.mybatis.generator.demo.datamap.table.*;

/**
 * Table Data Map
 */
public interface TM {

    AddressTableMap.Factory address = new AddressTableMap.Factory();

    TUserTableMap.Factory t_user = new TUserTableMap.Factory();

    NoPrimaryTableMap.Factory no_primary = new NoPrimaryTableMap.Factory();

    NoAutoIdTableMap.Factory no_auto_id = new NoAutoIdTableMap.Factory();
}