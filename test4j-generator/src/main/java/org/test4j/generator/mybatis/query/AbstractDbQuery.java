package org.test4j.generator.mybatis.query;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.test4j.generator.mybatis.config.IDbQuery;

/**
 * 表数据查询抽象类
 *
 * @author hubin
 * @since 2018-01-16
 */
public abstract class AbstractDbQuery implements IDbQuery {

    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return false;
    }
}
