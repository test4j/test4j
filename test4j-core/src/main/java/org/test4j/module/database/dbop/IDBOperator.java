package org.test4j.module.database.dbop;

import org.test4j.hamcrest.iassert.intf.ICollectionAssert;
import org.test4j.module.database.sql.SqlList;
import org.test4j.tools.datagen.TableMap;

import java.io.File;
import java.util.List;

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
     * 打印成dataMap形式
     *
     * @param tableAndWhere "tableName" or "tableName where ..."
     * @param mapName       DataMap名称
     * @return
     */
    String printAsDataMap(String tableAndWhere, String mapName);

    /**
     * 打印成JSON格式
     *
     * @param tables
     */
    void printAsJson(String... tables);

    /**
     * 打印成JSON格式
     *
     * @param tables
     * @param orderColumns
     * @param excludes
     */
    String printAsJson(String[] tables, String[] orderColumns, String... excludes);

    /**
     * 插入数据
     *
     * @param data  具体数据
     * @param clean 是否清空插入
     * @return
     */
    String insert(TableMap data, boolean clean);

    /**
     * 断言数据
     *
     * @param data
     * @return
     */
    String queryEq(TableMap data);
}
