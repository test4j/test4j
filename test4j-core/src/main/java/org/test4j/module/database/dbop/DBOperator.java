package org.test4j.module.database.dbop;

import org.test4j.hamcrest.iassert.impl.CollectionAssert;
import org.test4j.hamcrest.iassert.intf.ICollectionAssert;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.ICore;
import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.sql.SqlList;
import org.test4j.module.database.sql.Test4JSqlContext;
import org.test4j.module.database.utility.SqlRunner;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.ExceptionWrapper;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.datagen.TableData;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

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
    public List<Object> returnList(String query, Class pojoClazz) {
        IN_DB_OPERATOR.set(true);
        try {
            String _query = query.trim();
            if (!_query.toLowerCase().startsWith("select")) {
                _query = "select * from " + query;
            }
            if (pojoClazz == null) {
                return SqlRunner.queryMapList(env(), query);
            } else {
                return SqlRunner.queryList(env(), _query, pojoClazz);
            }
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    private void deleteWhere(String table, String where) {
        IN_DB_OPERATOR.set(true);
        try {
            String sql = "delete from " + table;
            if (!StringHelper.isBlankOrNull(where)) {
                sql += " where " + where;
            }
            SqlRunner.execute(env(), sql);
        } finally {
            IN_DB_OPERATOR.set(false);
        }
    }

    @Override
    public SqlList sqlList() {
        return Test4JSqlContext.getSqlContext();
    }

    @Override
    public IDBOperator insert(TableData data, boolean clean) {
        for (Map.Entry<String, List<Map<String, String>>> entry : data.entrySet()) {
            String table = entry.getKey();
            List<Map<String, String>> records = entry.getValue();
            if (clean) {
                this.deleteWhere(table, null);
            }
            for (Map<String, String> record : records) {
                this.table(table).insert(new ICore.DataMap(record));
            }
        }
        return this;
    }

    @Override
    public void queryEq(TableData data) {
        for (Map.Entry<String, List<Map<String, String>>> entry : data.entrySet()) {
            String query = entry.getKey();
            if (!query.trim().toLowerCase().startsWith("select")) {
                query = "select * from " + query;
            }
            List<Map<String, String>> records = entry.getValue();
            this.query(query).eqReflect(records, EqMode.IGNORE_DEFAULTS, EqMode.IGNORE_ORDER, EqMode.EQ_STRING);
        }
    }
}
