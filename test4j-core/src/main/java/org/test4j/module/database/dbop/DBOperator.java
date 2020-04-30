package org.test4j.module.database.dbop;

import org.test4j.exception.ExtraMessageError;
import org.test4j.hamcrest.iassert.impl.CollectionAssert;
import org.test4j.hamcrest.iassert.intf.ICollectionAssert;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.json.JSON;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.sql.SqlList;
import org.test4j.module.database.sql.Test4JSqlContext;
import org.test4j.module.database.utility.SqlRunner;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.ExceptionWrapper;
import org.test4j.tools.commons.ListHelper;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.datagen.TableMap;
import org.test4j.tools.datagen.TableData;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.test4j.module.database.utility.SqlKeyWord.COLUMN_ID;
import static org.test4j.tools.commons.StringHelper.DOUBLE_QUOTATION;
import static org.test4j.tools.commons.StringHelper.isBlank;

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
            if (!isBlank(where)) {
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
    public String printAsDataMap(String tableAndWhere, String mapName) {
        List<Map<String, Object>> list = TableOp.queryData(env(), tableAndWhere);
        boolean isDefault = isBlank(mapName);
        StringBuilder buff = new StringBuilder("\n").append(isDefault ? "DataMap" : mapName).append(".create(").append(list.size()).append(")");
        Map<String, List<String>> data = new HashMap<>(20);
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() == null ? "null" : DOUBLE_QUOTATION + entry.getValue() + DOUBLE_QUOTATION;
                if (!data.containsKey(key)) {
                    data.put(key, new ArrayList());
                }
                data.get(key).add(value);
            }
        }
        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            String value = entry.getValue().stream().collect(Collectors.joining(", "));
            if (!value.contains(DOUBLE_QUOTATION)) {
                continue;
            }
            if (isDefault) {
                buff.append("\n\t.kv(").append(DOUBLE_QUOTATION).append(entry.getKey()).append(DOUBLE_QUOTATION).append(", ").append(value).append(")");
            } else {
                buff.append("\n\t.").append(entry.getKey()).append(".values(").append(value).append(")");
            }
        }
        String text = buff.toString();
        MessageHelper.info(text);
        return text;

    }

    @Override
    public void printAsJson(String... tables) {
        this.printAsJson(tables, new String[0]);
    }

    @Override
    public String printAsJson(String[] tables, String[] orderColumns, String... excludes) {
        TableMap map = new TableMap();
        for (String table : tables) {
            TableData data = this.findTableData(table, orderColumns, excludes);
            map.put(table, data);
        }
        String text = JSON.toJSON(map, true);
        MessageHelper.info("\n" + text + "\n");
        return text;
    }

    private TableData findTableData(String tableAndWhere, String[] orderColumns, String[] excludes) {
        List<String> _excludes = ListHelper.toList(excludes);
        List<String> _columns = ListHelper.toList(orderColumns);
        List<Map<String, Object>> list = TableOp.queryData(env(), tableAndWhere);
        List<Map<String, Object>> records = new ArrayList<>(list.size());
        for (Map<String, Object> data : list) {
            Map<String, Object> map = new LinkedHashMap<>(data.size());
            if (data.containsKey(COLUMN_ID) && !_excludes.contains(COLUMN_ID)) {
                map.put(COLUMN_ID, StringHelper.toString(data.get(COLUMN_ID)));
            }
            for (String column : _columns) {
                if (data.containsKey(column) && !map.containsKey(column)) {
                    map.put(column, StringHelper.toString(data.get(column)));
                }
            }
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                if (!map.containsKey(key) && !_excludes.contains(key)) {
                    map.put(key, StringHelper.toString(entry.getValue()));
                }
            }
            records.add(map);
        }
        return new TableData(records);
    }

    @Override
    public String insert(TableMap data, boolean clean) {
        StringBuffer buff = new StringBuffer();
        try {
            for (Map.Entry<String, TableData> entry : data.entrySet()) {
                String table = entry.getKey();
                IDataMap records = entry.getValue().findDataMap();
                if (clean) {
                    this.deleteWhere(table, null);
                    buff.append("\t清空表[").append(table).append("]数据\n");
                }
                buff.append("\t准备[" + records.getRowSize() + "]条[").append(table).append("]表数据\n");
                this.table(table).insert(records);
            }
            return buff.toString();
        } catch (Throwable e) {
            throw new ExtraMessageError(buff.toString(), e);
        }
    }

    @Override
    public String queryEq(TableMap data) {
        StringBuffer buff = new StringBuffer();
        try {
            for (Map.Entry<String, TableData> entry : data.entrySet()) {
                String query = entry.getKey();
                if (!query.trim().toLowerCase().startsWith("select")) {
                    query = "select * from " + query;
                }
                IDataMap records = entry.getValue().findDataMap();
                try {
                    buff.append("\t检查[").append(query).append("]数据, 总[" + records.getRowSize() + "]条数据\n");
                    this.query(query).eqDataMap(records, EqMode.IGNORE_DEFAULTS, EqMode.IGNORE_ORDER, EqMode.EQ_STRING);
                } catch (AssertionError error) {
                    String message = error.getMessage();
                    throw new AssertionError("[" + query + "] data assert fail: " + message);
                }
            }
            return buff.toString();
        } catch (Throwable e) {
            throw new ExtraMessageError(buff.toString(), e);
        }
    }
}
