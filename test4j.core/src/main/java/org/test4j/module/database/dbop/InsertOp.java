package org.test4j.module.database.dbop;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.test4j.module.database.environment.DBEnvironment;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.environment.TableMeta;
import org.test4j.tools.commons.ExceptionWrapper;
import org.test4j.tools.datagen.IDataMap;

public class InsertOp implements IInsertOp {
    private String table;

    private DBEnvironment env;

    private TableMeta tableMeta;

    private String quato;

    public InsertOp() {
        this.env = DBEnvironmentFactory.getDefaultDBEnvironment();
        this.quato = this.env.getFieldQuato();
    }

    public InsertOp(DBEnvironment environment) {
        this.env = environment;
        this.quato = environment.getFieldQuato();
    }

    public static void insertNoException(DBEnvironment env, String table, IDataMap data) {
        try {
            InsertOp op = new InsertOp(env);
            op.insert(table, data);
        } catch (Exception e) {
            throw ExceptionWrapper.getUndeclaredThrowableExceptionCaused(e);
        }
    }

    /**
     * 往数据库中插入数据
     *
     * @param table
     * @param data
     * @throws Exception
     */
    public void insert(String table, IDataMap data) throws Exception {
        this.table = table;
        this.tableMeta = env.getTableMetaData(table);
        tableMeta.fillData(data, env);
        List<Map<String, ? extends Object>> datas = data.toList();
        for (Map<String, ? extends Object> map : datas) {
            env.execute(connection -> {
                PreparedStatement st = connection.prepareStatement(getInsertCommandText(map));
                return this.setParametersByMap(st, map);
            }, PreparedStatement::execute);
        }
    }

    private PreparedStatement setParametersByMap(PreparedStatement statement, Map<String, ?> map) {
        int index = 1;
        for (String key : map.keySet()) {
            try {
                Object value = getValueByColumn(key, map);
                if (value instanceof InputStream) {
                    InputStream is = (InputStream) value;
                    statement.setBinaryStream(index, is, is.available());
                } else {
                    Object sqlValue = env.convertToSqlValue(value);
                    statement.setObject(index, sqlValue);
                }
                index++;
            } catch (Throwable e) {
                throw new RuntimeException("set column[" + key + "] value error:" + e.getMessage(), e);
            }
        }
        return statement;
    }

    private Object getValueByColumn(String column, Map<String, ?> map) {
        Object value = map.get(column);
        if (!(value instanceof String)) {
            return value;
        }
        String javaType = this.tableMeta.getColumnType(column);
        if (String.class.getName().equals(javaType)) {
            value = this.tableMeta.truncateString(column, (String) value);
        } else {
            value = env.toObjectValue((String) value, javaType);
        }
        return value;
    }

    /**
     * 构造map的insert sql语句
     *
     * @param map
     * @return
     */
    private String getInsertCommandText(Map<String, ?> map) {
        StringBuilder text = new StringBuilder();
        StringBuilder values = new StringBuilder();

        text.append("insert into ").append(table).append("(");
        boolean isFirst = true;
        for (String key : map.keySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                text.append(",");
                values.append(",");
            }
            text.append(this.quato).append(key).append(this.quato);
            values.append("?");
        }

        text.append(") values(").append(values).append(")");
        return text.toString();
    }
}
