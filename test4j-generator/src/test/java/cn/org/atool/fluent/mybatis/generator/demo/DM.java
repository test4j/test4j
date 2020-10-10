package cn.org.atool.fluent.mybatis.generator.demo;

import cn.org.atool.fluent.mybatis.generator.demo.dm.*;

/**
 * Table Data Map
 */
public interface DM {

    AddressDataMap.Factory address = new AddressDataMap.Factory();

    NoPrimaryDataMap.Factory noPrimary = new NoPrimaryDataMap.Factory();

    UserDataMap.Factory user = new UserDataMap.Factory();

    NoAutoIdDataMap.Factory noAutoId = new NoAutoIdDataMap.Factory();
}