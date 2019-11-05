package org.test4j.module.database.dbop;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.test4j.hamcrest.iassert.intf.ICollectionAssert;
import org.test4j.hamcrest.iassert.intf.IMapAssert;
import org.test4j.hamcrest.iassert.intf.IObjectAssert;
import org.test4j.module.database.sql.SqlList;

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
     * 查询数据列表，并进行断言
     *
     * @param sql
     * @param clazz
     * @return
     */
    ICollectionAssert queryList(String sql, Class clazz);

    /**
     * 查询数据，并且返回的数据只有一行<br>
     * 将数据自动填充到Map<String,Object>中,并对数据进行断言
     *
     * @param sql
     * @return
     */
    IMapAssert queryAsMap(String sql);

    /**
     * 查询数据，并且返回的数据只有一行<br>
     * 将数据自动填充到 pojo 类型的对象中,并且返回对象的断言器
     *
     * @param sql
     * @param pojo
     * @return
     */
    IObjectAssert queryAsPoJo(String sql, Class pojo);

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
     * @param table
     * @return
     */
    List<Map<String, Object>> returnList(String table);

    /**
     * 返回表中所有的数据
     *
     * @param table
     * @param clazz
     * @return
     */
    <T> List<T> returnList(String table, Class<T> clazz);

    /**
     * 根据查询返回数据
     *
     * @param query
     * @return
     */
    List<Map<String, Object>> returnQuery(String query);

    /**
     * 根据查询返回映射数据
     *
     * @param query
     * @param clazz
     * @return
     */
    <T> List<T> returnQuery(String query, Class<T> clazz);

    /**
     * 根据条件删除数据
     *
     * @param table
     * @param where
     * @return
     */
    IDBOperator deleteWhere(String table, String where);

    /**
     * 清除线程中收集的sql语句
     *
     * @return
     */
    IDBOperator cleanSqlContext();

    /**
     * 返回测试线程中收集到的sql语句列表
     *
     * @return
     */
    SqlList sqlList();
}
