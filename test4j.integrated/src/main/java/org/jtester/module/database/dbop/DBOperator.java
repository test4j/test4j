package org.jtester.module.database.dbop;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.jtester.hamcrest.iassert.object.impl.CollectionAssert;
import org.jtester.hamcrest.iassert.object.impl.MapAssert;
import org.jtester.hamcrest.iassert.object.impl.ObjectAssert;
import org.jtester.hamcrest.iassert.object.intf.ICollectionAssert;
import org.jtester.hamcrest.iassert.object.intf.IMapAssert;
import org.jtester.hamcrest.iassert.object.intf.IObjectAssert;
import org.jtester.module.database.environment.DBEnvironment;
import org.jtester.module.database.environment.DBEnvironmentFactory;
import org.jtester.module.database.utility.SqlRunner;
import org.jtester.tools.commons.ExceptionWrapper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBOperator implements IDBOperator {
    /**
     * 是否在db操作
     */
    public static ThreadLocal<Boolean> IN_DB_OPERATOR = new ThreadLocal<Boolean>();
    static {
        IN_DB_OPERATOR.set(false);
    }

    @Override
    public IDBOperator useDB(String dataSource) {
        IN_DB_OPERATOR.set(true);
        try {
            DBEnvironment environment = DBEnvironmentFactory.getDBEnvironment(dataSource);
            DBEnvironmentFactory.changeDBEnvironment(environment);
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator useDefaultDB() {
        IN_DB_OPERATOR.set(true);
        try {
            DBEnvironment environment = DBEnvironmentFactory.getDefaultDBEnvironment();
            DBEnvironmentFactory.changeDBEnvironment(environment);
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator cleanTable(String table, String... more) {
        IN_DB_OPERATOR.set(true);
        try {
            SqlRunner.instance.execute("delete from " + table);
            for (String item : more) {
                SqlRunner.instance.execute("delete from " + item);
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
            SqlRunner.instance.execute(sql);
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
                SqlRunner.instance.executeFromStream(new FileInputStream(file));
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
            SqlRunner.instance.commit();
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IDBOperator rollback() {
        IN_DB_OPERATOR.set(true);
        try {
            SqlRunner.instance.rollback();
            return this;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IMapAssert queryAsMap(String query) {
        IN_DB_OPERATOR.set(true);
        try {
            Map<String, Object> map = SqlRunner.instance.queryMap(query);
            return new MapAssert(map);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public IObjectAssert queryAsPoJo(String query, Class objClazz) {
        IN_DB_OPERATOR.set(true);
        try {
            Object o = SqlRunner.instance.query(query, objClazz);
            return new ObjectAssert(o);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public ICollectionAssert query(String sql) {
        IN_DB_OPERATOR.set(true);
        try {
            List list = SqlRunner.instance.queryMapList(sql);
            return new CollectionAssert(list);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public ICollectionAssert queryList(String query, Class pojo) {
        IN_DB_OPERATOR.set(true);
        try {
            List list = SqlRunner.instance.queryList(query, pojo);
            return new CollectionAssert(list);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public ITableOp table(String table) {
        IN_DB_OPERATOR.set(true);
        try {
            ITableOp tableOperator = new TableOp(table);
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
            sqlSet.execute();
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
            List list = SqlRunner.instance.queryMapList(query);
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
            List list = SqlRunner.instance.queryList(query, pojoClazz);
            return list;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public List<Map<String, Object>> returnQuery(String query) {
        IN_DB_OPERATOR.set(true);
        try {
            List list = SqlRunner.instance.queryMapList(query);
            return list;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public List<Object> returnQuery(String query, Class pojoClazz) {
        IN_DB_OPERATOR.set(true);
        try {
            List list = SqlRunner.instance.queryList(query, pojoClazz);
            return list;
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }
}
