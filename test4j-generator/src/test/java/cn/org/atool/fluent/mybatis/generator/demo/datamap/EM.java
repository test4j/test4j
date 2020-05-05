package cn.org.atool.fluent.mybatis.generator.demo.datamap;

import cn.org.atool.fluent.mybatis.generator.demo.datamap.entity.*;

/**
 * Entity Data Map
 */
public interface EM {

    AddressEntityMap.Factory address = new AddressEntityMap.Factory();

    TUserEntityMap.Factory tUser = new TUserEntityMap.Factory();

    NoPrimaryEntityMap.Factory noPrimary = new NoPrimaryEntityMap.Factory();

    NoAutoIdEntityMap.Factory noAutoId = new NoAutoIdEntityMap.Factory();
}