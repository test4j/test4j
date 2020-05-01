package org.test4j.generator.mybatis.query;


import org.test4j.generator.mybatis.rule.DbType;

/**
 * DB2 表数据查询
 *
 * @author zhanyao
 * @since 2018-05-16
 */
public class DB2Query extends AbstractDbQuery {
    @Override
    public DbType dbType() {
        return DbType.DB2;
    }

    @Override
    public String tablesSql() {
        return "SELECT * FROM SYSCAT.TABLES where tabschema=%s";
    }

    @Override
    public String tableFieldsSql() {
        return "SELECT * FROM syscat.columns WHERE tabschema=%s AND tabname='%s'";
    }

    @Override
    public String tableName() {
        return "TABNAME";
    }

    @Override
    public String tableComment() {
        return "REMARKS";
    }

    @Override
    public String fieldName() {
        return "COLNAME";
    }

    @Override
    public String fieldType() {
        return "TYPENAME";
    }

    @Override
    public String fieldComment() {
        return "REMARKS";
    }

    @Override
    public String fieldKey() {
        return "IDENTITY";
    }
}
