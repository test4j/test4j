package org.test4j.generator.mybatis.query;

import org.test4j.generator.mybatis.rule.DbType;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 表数据查询接口
 *
 * @author darui.wu
 */
public interface IDbQuery {
    /**
     * 数据库类型
     */
    DbType dbType();

    /**
     * 表信息查询 SQL
     */
    String tablesSql();

    /**
     * 表字段信息查询 SQL
     */
    String tableFieldsSql();

    /**
     * 表名称
     */
    String tableName();

    /**
     * 表注释
     */
    String tableComment();

    /**
     * 字段名称
     */
    String fieldName();

    /**
     * 字段类型
     */
    String fieldType();

    /**
     * 字段注释
     */
    String fieldComment();

    /**
     * 主键字段
     */
    String fieldKey();

    /**
     * 判断主键是否为identity，目前仅对mysql进行检查
     *
     * @param results ResultSet
     * @return 主键是否为identity
     * @throws SQLException
     */
    boolean isKeyIdentity(ResultSet results) throws SQLException;
}
