package cn.org.atool.fluent.mybatis.generator.demo.datamap;

import cn.org.atool.fluent.mybatis.generator.demo.datamap.table.*;

/**
 * Table Data Map
 */
public interface TM {

    AddressTableMap.Factory address = new AddressTableMap.Factory();

    UserTableMap.Factory user = new UserTableMap.Factory();

    NoAutoIdTableMap.Factory no_auto_id = new NoAutoIdTableMap.Factory();

    NoPrimaryTableMap.Factory no_primary = new NoPrimaryTableMap.Factory();
}
