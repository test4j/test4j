package org.test4j.module.database.dbop;

import org.test4j.hamcrest.iassert.intf.ICollectionAssert;
import org.test4j.hamcrest.iassert.intf.IMapAssert;
import org.test4j.hamcrest.iassert.intf.IObjectAssert;
import org.test4j.module.database.sql.SqlList;
import org.test4j.tools.datagen.TableData;

import java.io.File;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface IDBOperator {
    /**
     * 提交数据
     *
     * @return
     */
    IDBOperator commit();

    /**
     * 回滚未提交的数据
     *
     * @return
     */
    IDBOperator rollback();

    /**
     * 清空指定的若干张表的数据
     *
     * @param table
     * @param mores
     * @return
     */
    IDBOperator cleanTable(String table, String... mores);

    /**
     * 针对表进行操作
     *
     * @param table
     * @return
     */
    ITableOp table(String table);

    /**
     * 查询数据列表，并进行断言
     *
     * @param sql
     * @return
     */
    ICollectionAssert query(String sql);

    /**
     * 执行sql语句集合
     *
     * @param sqlSet
     * @return
     */
    IDBOperator execute(ISqlSet sqlSet);

    /**
     * 执行单条语句，执行多条请使用 execute(SqlSet)
     *
     * @param sql
     * @return
     */
    IDBOperator execute(String sql);

    /**
     * 执行文件中的sql语句
     *
     * @param sqlFile
     * @return
     */
    IDBOperator execute(File sqlFile);

    /**
     * 返回表中所有的数据
     *
     * @param query
     * @param clazz
     * @return
     */
    <T> List<T> returnList(String query, Class<T> clazz);

    /**
     * 返回测试线程中收集到的sql语句列表
     *
     * @return
     */
    SqlList sqlList();

    /**
     * 插入数据
     *
     * @param data  具体数据
     * @param clean 是否清空插入
     * @return
     */
    String insert(TableData data, boolean clean);

    /**
     * 断言数据
     *
     * @param data
     */
    String queryEq(TableData data);
}
