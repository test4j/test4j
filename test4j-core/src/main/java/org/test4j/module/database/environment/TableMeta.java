package org.test4j.module.database.environment;

import org.test4j.tools.datagen.IDataMap;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class TableMeta {
    /**
     * 表名
     */
    String tableName;

    Map<String, ColumnMeta> columns;

    public TableMeta(String table, ResultSetMetaData meta) throws Exception {
        this.tableName = table;
        this.columns = new HashMap<>();
        int count = meta.getColumnCount();
        for (int index = 1; index <= count; index++) {
            ColumnMeta columnMeta = new ColumnMeta();

            columnMeta.columnName = meta.getColumnName(index);
            columnMeta.size = meta.getColumnDisplaySize(index);
            columnMeta.typeName = meta.getColumnTypeName(index);
            columnMeta.isNullable = meta.isNullable(index) == 1;

            columnMeta.javaType = meta.getColumnClassName(index);// dbEnvironment.getJavaClass(columnMeta.typeName);

            this.columns.put(columnMeta.columnName, columnMeta);
        }
    }

    public Map<String, ColumnMeta> getColumns() {
        return columns;
    }

    /**
     * 将string转换为对应的java对象
     *
     * @param column
     * @return
     */
    public String getColumnType(String column) {
        ColumnMeta meta = this.getColumns().get(column);
        if (meta == null) {
            throw new RuntimeException("can't find column[" + column + "] field in table[" + tableName + "].");
        }

        return meta.javaType;
    }

    /**
     * 返回字段的长度
     *
     * @param column
     * @return
     */
    public int getColumnSize(String column) {
        ColumnMeta meta = this.getColumns().get(column);
        if (meta == null) {
            throw new RuntimeException("can't find column[" + column + "] field in table[" + tableName + "].");
        }

        return meta.size;
    }

    /**
     * 根据数据库字段的最大长度，截断插入的值
     *
     * @param column
     * @param input
     * @return
     */
    public String truncateString(String column, String input) {
        if (input == null) {
            return null;
        }
        int size = this.getColumnSize(column);
        if (size > input.length()) {
            return input;
        } else {
            return input.substring(0, size);
        }
    }

    public void fillData(IDataMap data, DBEnvironment environment) {
        Set<String> keys = ((Set<String>) data.keySet()).stream()
            .map(String::toUpperCase)
            .collect(toSet());
        for (String key : this.columns.keySet()) {
            if (keys.contains(key.toUpperCase())) {
                continue;
            }
            ColumnMeta column = this.columns.get(key);
            Object value = column.getDefaultValue(environment);
            data.kv(key, value);
        }
    }

    public static class ColumnMeta {
        /**
         * 字段名称
         */
        String columnName;
        /**
         * 字段大小
         */
        int size;
        /**
         * 字段类型名称
         */
        String typeName;
        /**
         * 是否允许null?
         */
        boolean isNullable;

        /**
         * 对应的java类型
         */
        String javaType;

        @Override
        public String toString() {
            return "[columnName=" + columnName + ", size=" + size + ", typeName=" + typeName + ", isNullable="
                + isNullable + "]";
        }

        public Object getDefaultValue(DBEnvironment dbEnvironment) {
            if (this.isNullable()) {
                return null;
            }

            if ("java.lang.String".equals(javaType)) {
                return "test4j".subSequence(0, size > 6 ? 6 : size);
            } else {
                Object value = dbEnvironment.getDefaultValue(javaType);
                return value;
            }
        }

        public boolean isNullable() {
            return isNullable;
        }

    }
}