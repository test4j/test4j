package org.test4j.module.database.dbop;

import org.test4j.hamcrest.iassert.impl.CollectionAssert;
import org.test4j.hamcrest.iassert.impl.IntegerAssert;
import org.test4j.hamcrest.iassert.impl.ObjectAssert;
import org.test4j.hamcrest.iassert.intf.ICollectionAssert;
import org.test4j.hamcrest.iassert.intf.INumberAssert;
import org.test4j.hamcrest.iassert.intf.IObjectAssert;
import org.test4j.json.JSON;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.sql.Test4JSqlContext;
import org.test4j.module.database.utility.DBHelper;
import org.test4j.module.database.utility.SqlRunner;
import org.test4j.tools.datagen.IDataMap;

import java.util.List;
import java.util.Map;

import static org.test4j.module.database.dbop.DBOperator.IN_DB_OPERATOR;
import static org.test4j.tools.commons.StringHelper.isBlankOrNull;

/**
 * TableOp
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class TableOp implements ITableOp {
    private final DBEnvironment env;

    private String table;

    public TableOp(String table) {
        this(DBEnvironmentFactory.getDefaultDBEnvironment(), table);
    }

    public TableOp(DBEnvironment env, String table) {
        this.env = env;
        this.table = table;
        if (isBlankOrNull(this.table)) {
            throw new RuntimeException("the table name can't be null.");
        }
    }

    @Override
    public ITableOp clean() {
        setDbOperator(true);
        try {
            String sql = "delete from " + table;
            SqlRunner.execute(env, sql);
            return this;
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public ITableOp insert(IDataMap... datas) {
        if (datas == null || datas.length == 0) {
            return this;
        }
        setDbOperator(true);
        try {
            for (IDataMap map : datas) {
                InsertOp.insertNoException(env, table, map);
            }
            return this;
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert query() {
        setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(null);
            return new CollectionAssert(list);
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert queryList(Class pojo) {
        setDbOperator(true);
        try {
            String query = "select * from " + table;
            List list = SqlRunner.queryList(env, query, pojo);
            return new CollectionAssert(list);
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public INumberAssert count() {
        setDbOperator(true);
        try {
            String query = "select count(*) from " + table;
            Number number = (Number) SqlRunner.query(env, query, Object.class);
            return new IntegerAssert(number.intValue());
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public ITableOp deleteWhere(String where) {
        setDbOperator(true);
        try {
            String sql = String.format("delete from %s where %s", table, where);
            SqlRunner.execute(env, sql);
            return this;
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public IObjectAssert queryAs(Class pojo) {
        setDbOperator(true);
        try {
            String query = "select * from " + table;
            Object o = SqlRunner.query(env, query, pojo);
            return new ObjectAssert(o);
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert queryWhere(String where) {
        setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(where);
            return new CollectionAssert(list);
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert printAndAssert(String where) {
        setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(where);
            MessageHelper.info(JSON.toJSON(list, true));
            return new CollectionAssert(list);
        } finally {
            setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert queryWhere(IDataMap dataMap) {
        setDbOperator(true);
        try {
            Map<String, Object> param = dataMap.row(0);
            StringBuilder query = new StringBuilder("select * from ");
            query.append(table).append(" ");
            String where = DBHelper.getWhereCondition(param);
            query.append(where);
            List list = SqlRunner.queryMapList(env, query.toString(), param);
            return new CollectionAssert(list);
        } finally {
            setDbOperator(false);
        }
    }

    private static void setDbOperator(boolean bool) {
        IN_DB_OPERATOR.set(bool);
        Test4JSqlContext.setTestOpStatus(bool);
    }

    private List<Map<String, Object>> selectData(String where) {
        String _where = isBlankOrNull(where) ? "" : " where " + where.trim();
        String query = "select * from " + table + _where;
        return queryData(env, query);
    }

    public static List<Map<String, Object>> queryData(DBEnvironment env, String tableAndWhere) {
        setDbOperator(true);
        try {
            if (tableAndWhere.trim().toLowerCase().startsWith("select")) {
                return SqlRunner.queryMapList(env, tableAndWhere);
            } else {
                String query = "select * from " + tableAndWhere;
                return SqlRunner.queryMapList(env, query);
            }
        } finally {
            setDbOperator(false);
        }
    }
}
