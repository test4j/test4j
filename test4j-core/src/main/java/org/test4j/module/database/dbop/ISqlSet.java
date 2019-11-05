package org.test4j.module.database.dbop;

import org.test4j.module.database.environment.DBEnvironment;

/**
 * 可执行的sql语句集合
 *
 * @author darui.wudr 2013-1-11 下午5:29:05
 */
public interface ISqlSet {

    /**
     * 执行sql语句
     */
    void execute(DBEnvironment env);
}
