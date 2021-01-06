package org.test4j.module.database.utility;

import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.tools.commons.ResourceHelper;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * sql 执行器
 *
 * @author darui.wudr
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class SqlRunner {
    /**
     * 执行sql语句
     *
     * @param sql
     * @throws SQLException
     */
    public static void execute(DBEnvironment env, String sql) {
        env.execute(sql, PreparedStatement::execute);
    }

    /**
     * 执行sql文件<br>
     * 默认从classpath中读取<br>
     * classpath:前缀开头，表示从classpath中读取<br>
     * file:前缀开头，表示从文件系统中读取<br>
     *
     * @param fileName
     * @throws Exception
     */
    public static void executeFromFile(DBEnvironment env, String fileName) throws Exception {
        String[] statements = DBHelper.parseSQL(ResourceHelper.readFromFile(fileName));
        for (String statement : statements) {
            execute(env, statement);
        }
    }

    public static void executeFromFile(DBEnvironment env, Class klass, String fileName) {
        try {
            String[] statements = DBHelper.parseSQL(ResourceHelper.readFromFile(klass, fileName));
            for (String statement : statements) {
                execute(env, statement);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行sql文件流
     *
     * @param is
     * @throws Exception
     */
    public static void executeFromStream(DBEnvironment env, InputStream is) throws Exception {
        String[] statements = DBHelper.parseSQL(ResourceHelper.readFromStream(is));
        for (String statement : statements) {
            execute(env, statement);
        }
    }

    /**
     * 根据sql查询数据，如果result是Map.class则返回Map类型<br>
     * 如果是PoJo，则根据camel name命名方式初始化result
     *
     * @param <T>
     * @param sql
     * @param claz
     * @return
     * @throws SQLException
     */
    public static <T> T query(DBEnvironment env, String sql, Class<T> claz) {
        return env.query(sql, resultSet -> {
            if (Map.class.isAssignableFrom(claz)) {
                return (T) DBHelper.getMapFromResult(resultSet, false);
            } else {
                return DBHelper.getPoJoFromResult(resultSet, claz);
            }
        });
    }

    /**
     * 执行sql，返回查询数据列表，如果result是Map.class则返回Map列表<br>
     * 如果是PoJo，则根据camel name命名方式初始化result，返回PoJo列表
     *
     * @param <T>
     * @param sql
     * @param clazz
     * @return
     */
    public static <T> List<T> queryList(DBEnvironment env, String sql, Class<T> clazz) {
        return env.query(sql, resultSet -> {
            if (Map.class.isAssignableFrom(clazz)) {
                return (List<T>) DBHelper.getListMapFromResult(resultSet, false);
            } else {
                return DBHelper.getListPoJoFromResult(resultSet, clazz);
            }
        });
    }

    public static <T> List<T> queryMapList(DBEnvironment env, String sql) {
        return env.query(sql, resultSet ->
                (List<T>) DBHelper.getListMapFromResult(resultSet, false)
        );
    }

    public static <T> List<T> queryMapList(DBEnvironment env, String sql, Map<String,Object> where) {
        return env.query(connection -> {
            PreparedStatement st = connection.prepareStatement(sql);
            return setParameterByMap(st, where);
        }, resultSet -> (List<T>) DBHelper.getListMapFromResult(resultSet, false));
    }


    public static <T> Map<String, Object> queryMap(DBEnvironment env, String sql) {
        List<Map> list = queryMapList(env, sql);
        if (list.size() == 0) {
            return null;
        } else if (list.size() > 1) {
            throw new RuntimeException("to many result, u want to query one RowSet.");
        } else {
            return list.get(0);
        }
    }

    /**
     * 这里map取值顺序必须和 DBHelper.getWhereCondition 保持一致
     * @param st
     * @param where
     * @return
     */
    private static PreparedStatement setParameterByMap(PreparedStatement st, Map<String, Object> where) {
        int index = 1;
        for (Map.Entry<String,Object> entry : where.entrySet()) {
            try {
                Object value = entry.getValue();
                if (value instanceof InputStream) {
                    InputStream is = (InputStream) value;
                    st.setBinaryStream(index, is, is.available());
                } else {
                    st.setObject(index, value);
                }
                index++;
            } catch (Throwable e) {
                throw new RuntimeException("set column[" + entry.getKey() + "] value error:" + e.getMessage(), e);
            }
        }
        return st;
    }
}
