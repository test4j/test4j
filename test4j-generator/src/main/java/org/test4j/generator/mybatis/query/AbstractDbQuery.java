package org.test4j.generator.mybatis.query;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表数据查询抽象类
 */
public abstract class AbstractDbQuery implements IDbQuery {

    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return false;
    }
}
