package org.jtester.module.database.environment;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TableMeta {
	/**
	 * 表名
	 */
	String tableName;

	Map<String, ColumnMeta> columns;

	public TableMeta(String table, ResultSetMetaData meta, DBEnvironment dbEnvironment) throws Exception {
		this.tableName = table;
		this.columns = new HashMap<String, ColumnMeta>();
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
	 * @param input
	 * @param type
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
	public int getCloumnSize(String column) {
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
		int size = this.getCloumnSize(column);
		if (size > input.length()) {
			return input;
		} else {
			return input.substring(0, size);
		}
	}

	/**
	 * 填充在data中未指定字段的默认值
	 * 
	 * @param data
	 */
	public void fillData(Map<String, Object> data, DBEnvironment dbEnvironment) {
		Set<String> keys = data.keySet();
		for (String key : this.columns.keySet()) {
			if (keys.contains(key)) {
				continue;
			}
			ColumnMeta column = this.columns.get(key);
			Object value = column.getDefaultValue(dbEnvironment);
			data.put(key, value);
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
		 * 默认值
		 */
		Object defaultValue;

		/**
		 * 对应的java类型
		 */
		String javaType;

		@Override
		public String toString() {
			return "[columnName=" + columnName + ", size=" + size + ", typeName=" + typeName + ", isNullable="
					+ isNullable + ", defaultValue=" + defaultValue + "]";
		}

		public Object getDefaultValue(DBEnvironment dbEnvironment) {
			if (this.isNullable()) {
				return null;
			}

			if ("java.lang.String".equals(javaType)) {
				return "jtester".subSequence(0, size > 7 ? 7 : size);
			} else {
				Object value = dbEnvironment.getDefaultValue(javaType);
				return value;
			}
		}

		public String getColumnName() {
			return columnName;
		}

		public int getSize() {
			return size;
		}

		public String getTypeName() {
			return typeName;
		}

		public boolean isNullable() {
			return isNullable;
		}

		public Object getDefaultValue() {
			return defaultValue;
		}
	}
}
