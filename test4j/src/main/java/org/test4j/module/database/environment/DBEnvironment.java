package org.test4j.module.database.environment;

import org.test4j.function.EConsumer;
import org.test4j.function.EFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface DBEnvironment {
    /**
     * 执行sql语句
     *
     * @param sql
     * @param executor
     */
    void execute(String sql, EConsumer<PreparedStatement> executor);

    /**
     * 执行sql语句  bb
     *
     * @param statementEFunction
     * @param executor
     */
    void execute(EFunction<Connection, PreparedStatement> statementEFunction,
                 EConsumer<PreparedStatement> executor);

    /**
     * 查询
     *
     * @param statementEFunction
     * @param resultFunction
     * @param <R>
     * @return
     */
    <R> R query(EFunction<Connection, PreparedStatement> statementEFunction,
                EFunction<ResultSet, R> resultFunction);

    /**
     * 查询
     *
     * @param sql
     * @param resultFunction
     * @param <R>
     * @return
     */
    <R> R query(String sql, EFunction<ResultSet, R> resultFunction);

    /**
     * 获得数据表的元信息
     *
     * @param table
     * @return
     * @throws Exception
     */
    TableMeta getTableMetaData(String table);

    /**
     * 返回指定类型的默认值
     *
     * @param javaType
     * @return
     */
    Object getDefaultValue(String javaType);

    /**
     * 将字符串类型转换为java对象
     *
     * @param input
     * @param javaType
     * @return
     */
    Object toObjectValue(String input, String javaType);

    void commit();

    void rollback();

    String getFieldQuato();

    Object convertToSqlValue(Object value);
}
