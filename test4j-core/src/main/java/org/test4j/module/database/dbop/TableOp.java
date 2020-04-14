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
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.datagen.IDataMap;
import org.test4j.tools.datagen.TableData;

import java.util.*;
import java.util.stream.Collectors;

import static org.test4j.module.database.dbop.DBOperator.IN_DB_OPERATOR;
import static org.test4j.module.database.utility.SqlKeyWord.COLUMN_ID;
import static org.test4j.tools.commons.StringHelper.DOUBLE_QUOTATION;
import static org.test4j.tools.commons.StringHelper.isBlankOrNull;

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
        this.setDbOperator(true);
        try {
            String sql = "delete from " + table;
            SqlRunner.execute(env, sql);
            return this;
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public ITableOp insert(IDataMap... datas) {
        if (datas == null || datas.length == 0) {
            return this;
        }
        this.setDbOperator(true);
        try {
            for (IDataMap map : datas) {
                InsertOp.insertNoException(env, table, map);
            }
            return this;
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert query() {
        this.setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(null);
            return new CollectionAssert(list);
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert queryList(Class pojo) {
        this.setDbOperator(true);
        try {
            String query = "select * from " + table;
            List list = SqlRunner.queryList(env, query, pojo);
            return new CollectionAssert(list);
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public INumberAssert count() {
        this.setDbOperator(true);
        try {
            String query = "select count(*) from " + table;
            Number number = (Number) SqlRunner.query(env, query, Object.class);
            return new IntegerAssert(number.intValue());
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public ITableOp deleteWhere(String where) {
        this.setDbOperator(true);
        try {
            String sql = String.format("delete from %s where %s", table, where);
            SqlRunner.execute(env, sql);
            return this;
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public IObjectAssert queryAs(Class pojo) {
        this.setDbOperator(true);
        try {
            String query = "select * from " + table;
            Object o = SqlRunner.query(env, query, pojo);
            return new ObjectAssert(o);
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert queryWhere(String where) {
        this.setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(where);
            return new CollectionAssert(list);
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert printAndAssert(String where) {
        this.setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(where);
            MessageHelper.info(JSON.toJSON(list, true));
            return new CollectionAssert(list);
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public ICollectionAssert queryWhere(IDataMap dataMap) {
        this.setDbOperator(true);
        try {
            Map<String, Object> param = dataMap.row(0);
            StringBuilder query = new StringBuilder("select * from ");
            query.append(table).append(" ");
            String where = DBHelper.getWhereCondition(param);
            query.append(where);
            List list = SqlRunner.queryMapList(env, query.toString(), param);
            return new CollectionAssert(list);
        } finally {
            this.setDbOperator(false);
        }
    }

    private void setDbOperator(boolean bool) {
        IN_DB_OPERATOR.set(bool);
        Test4JSqlContext.setTestOpStatus(bool);
    }

    @Override
    public String printAsDataMap(String where, String mapName) {
        this.setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(where);
            boolean isDefault = isBlankOrNull(mapName);
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
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public String printAsMulMap(String where, String mapName) {
        this.setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(where);
            boolean isDefault = isBlankOrNull(mapName);
            String text = "";
            for (Map<String, Object> map : list) {
                StringBuilder buff = new StringBuilder("\n").append(isDefault ? "DataMap" : mapName).append(".create(").append(1).append(")");
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue() == null) {
                        continue;
                    }
                    String value = entry.getValue() == null ? "null" : DOUBLE_QUOTATION + entry.getValue() + DOUBLE_QUOTATION;
                    if (isDefault) {
                        buff.append("\n\t.kv(").append(DOUBLE_QUOTATION).append(entry.getKey()).append(DOUBLE_QUOTATION).append(", ").append(value).append(")");
                    } else {
                        buff.append("\n\t.").append(entry.getKey()).append(".values(").append(value).append(")");
                    }
                }
                text += ("".equals(text) ? "" : ",") + buff.toString();
            }
            MessageHelper.info(text);
            return text;
        } finally {
            this.setDbOperator(false);
        }
    }

    @Override
    public String printAsJson(String where, String... orderColumns) {
        this.setDbOperator(true);
        try {
            List<Map<String, Object>> list = selectData(where);
            List<Map<String, Object>> orders = new ArrayList<>(list.size());
            for (Map<String, Object> data : list) {
                Map<String, Object> map = new LinkedHashMap<>(data.size());
                if (data.containsKey(COLUMN_ID)) {
                    map.put(COLUMN_ID, StringHelper.toString(data.get(COLUMN_ID)));
                }
                for (String column : orderColumns) {
                    if (data.containsKey(column) && !map.containsKey(column)) {
                        map.put(column, StringHelper.toString(data.get(column)));
                    }
                }
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    if (!map.containsKey(entry.getKey())) {
                        map.put(entry.getKey(), StringHelper.toString(entry.getValue()));
                    }
                }
                orders.add(map);
            }

            TableData tableData = new TableData();
            tableData.put(table, orders);
            String text = JSON.toJSON(tableData, true);
            MessageHelper.info("\n\n" + text);
            return text;
        } finally {
            this.setDbOperator(false);
        }
    }

    private List<Map<String, Object>> selectData(String where) {
        String _where = isBlankOrNull(where) ? "" : " where " + where.trim();
        String query = "select * from " + table + _where;
        return SqlRunner.queryMapList(env, query);
    }
}
