package org.test4j.module.database.dbop;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.test4j.hamcrest.iassert.impl.CollectionAssert;
import org.test4j.hamcrest.iassert.impl.MapAssert;
import org.test4j.hamcrest.iassert.impl.ObjectAssert;
import org.test4j.hamcrest.iassert.intf.ICollectionAssert;
import org.test4j.hamcrest.iassert.intf.IMapAssert;
import org.test4j.hamcrest.iassert.intf.IObjectAssert;
import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.sql.SqlList;
import org.test4j.module.database.sql.Test4JSqlContext;
import org.test4j.module.database.utility.SqlRunner;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.ExceptionWrapper;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DBOperator implements IDBOperator {
    /**
     * 是否在db操作
     */
    public static ThreadLocal<Boolean> IN_DB_OPERATOR = new ThreadLocal<Boolean>();

    static {
        IN_DB_OPERATOR.set(false);
    }

    private final String dataSourceName;

    public DBOperator() {
        this.dataSourceName = ConfigHelper.getDefaultDataSource();
    }

    public DBOperator(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * 懒加载
     *
     * @return
     */
    private DBEnvironment env() {
        return DBEnvironmentFactory.getDBEnvironment(this.dataSourceName);
    }

    @Override
    public IDBOperator cleanTable(String table, String... more) {
        IN_DB_OPERATOR.set(true);
        try {
            SqlRunner.execute(env(), "delete from " + table);
            for (String item : more) {
                SqlRunner.execute(env(), "delete from " + item);
            }
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator execute(String sql) {
        IN_DB_OPERATOR.set(true);
        try {
            SqlRunner.execute(env(), sql);
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator execute(File file) {
        IN_DB_OPERATOR.set(true);
        try {
            try {
                SqlRunner.executeFromStream(env(), new FileInputStream(file));
            } catch (Exception e) {
                throw ExceptionWrapper.wrapWithRuntimeException(e);
            }
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator commit() {
        IN_DB_OPERATOR.set(true);
        try {
            env().commit();
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator rollback() {
        IN_DB_OPERATOR.set(true);
        try {
            env().rollback();
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IMapAssert queryAsMap(String query) {
        IN_DB_OPERATOR.set(true);
        try {
            Map<String, Object> map = SqlRunner.queryMap(env(), query);
            return new MapAssert(map);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IObjectAssert queryAsPoJo(String query, Class objClazz) {
        IN_DB_OPERATOR.set(true);
        try {
            Object o = SqlRunner.query(env(), query, objClazz);
            return new ObjectAssert(o);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public ICollectionAssert query(String sql) {
        IN_DB_OPERATOR.set(true);
        try {
            List list = SqlRunner.queryMapList(env(), sql);
            return new CollectionAssert(list);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public ICollectionAssert queryList(String query, Class pojo) {
        IN_DB_OPERATOR.set(true);
        try {
            List list = SqlRunner.queryList(env(), query, pojo);
            return new CollectionAssert(list);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public ITableOp table(String table) {
        IN_DB_OPERATOR.set(true);
        try {
            ITableOp tableOperator = new TableOp(env(), table);
            return tableOperator;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator execute(ISqlSet sqlSet) {
        IN_DB_OPERATOR.set(true);
        try {
            if (sqlSet == null) {
                throw new RuntimeException("the insert sqlSet can't be null.");
            }
            sqlSet.execute(env());
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public List<Map<String, Object>> returnList(String table) {
        IN_DB_OPERATOR.set(true);
        try {
            String query = "select * from " + table;
            List list = SqlRunner.queryMapList(env(), query);
            return list;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public List<Object> returnList(String table, Class pojoClazz) {
        IN_DB_OPERATOR.set(true);
        try {
            String query = "select * from " + table;
            List list = SqlRunner.queryList(env(), query, pojoClazz);
            return list;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public List<Map<String, Object>> returnQuery(String query) {
        IN_DB_OPERATOR.set(true);
        try {
            List list = SqlRunner.queryMapList(env(), query);
            return list;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator deleteWhere(String table, String where) {
        IN_DB_OPERATOR.set(true);
        try {
            String sql = String.format("delete from %s where %s", table, where);
            SqlRunner.execute(env(), sql);
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public List<Object> returnQuery(String query, Class pojoClazz) {
        IN_DB_OPERATOR.set(true);
        try {
            List list = SqlRunner.queryList(env(), query, pojoClazz);
            return list;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator cleanSqlContext() {
        Test4JSqlContext.clean();
        return this;
    }

    @Override
    public SqlList sqlList() {
        return Test4JSqlContext.getSqlContext();
    }
}
