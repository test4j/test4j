package org.jtester.module.database.utility;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.module.ICore.DataMap;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.database.environment.normalise.TypeNormaliser;
import org.jtester.module.database.environment.normalise.TypeNormaliserFactory;
//import org.jtester.module.dbfit.db.model.DbParameterAccessor;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.StringHelper;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.jtester.tools.reflector.FieldAccessor;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class DBHelper {

	/**
	 * 关闭数据库statement句柄
	 */
	public static void closeStatement(Statement st) {
		if (st == null) {
			return;
		}
		try {
			st.close();
			st = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs == null) {
			return;
		}
		try {
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将ResultSet的当前行转换为Map数据返回
	 * 
	 * @param rs
	 *            数据库结果ResultSet
	 * @param rsmd
	 *            ResultSet的meta数据
	 * @return
	 * @throws SQLException
	 */
	public static Map getMapFromResult(ResultSet rs, ResultSetMetaData rsmd, boolean isCamelName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = rsmd.getColumnCount();
		for (int index = 1; index <= count; index++) {
			String key = getCamelFieldName(rsmd, index, isCamelName);
			String columnClaz = rsmd.getColumnClassName(index);
			Object o = null;
			if ("java.sql.Timestamp".equals(columnClaz)) {
				o = rs.getTimestamp(index);
			} else {
				o = rs.getObject(index);
			}
			Object value = DBHelper.normaliseValue(o);
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 将ResultSet的转换为Map List数据返回
	 * 
	 * @param rs
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	public static List<Map> getListMapFromResult(ResultSet rs, ResultSetMetaData rsmd, boolean isCamelName)
			throws Exception {
		List<Map> list = new ArrayList<Map>();
		while (rs.next()) {
			Map map = getMapFromResult(rs, rsmd, isCamelName);
			list.add(map);
		}

		return list;
	}

	/**
	 * 将ResultSet的当前行转换为PoJo数据返回
	 * 
	 * @param <T>
	 * @param rs
	 * @param rsmd
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	public static <T> T getPoJoFromResult(ResultSet rs, ResultSetMetaData rsmd, Class<T> clazz) throws Exception {
		int count = rsmd.getColumnCount();
		if (count == 1) {
			Object o = rs.getObject(1);
			Object value = DBHelper.normaliseValue(o);
			return (T) value;
		}
		T pojo = ClazzHelper.newInstance(clazz);
		for (int index = 1; index <= count; index++) {
			String key = getCamelFieldName(rsmd, index, true);

			try {
				FieldAccessor accessor = new FieldAccessor(clazz, key);
				Object o = rs.getObject(index);
				Object value = DBHelper.normaliseValue(o);
				accessor.set(pojo, value);
			} catch (NoSuchFieldRuntimeException e) {
				MessageHelper.warn("set pojo property errro: " + e.getMessage());
			}
		}
		return pojo;
	}

	/**
	 * 将ResultSet的当前行转换为PoJo列表数据返回
	 * 
	 * @param <T>
	 * @param rs
	 * @param rsmd
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	public static <T> List<T> getListPoJoFromResult(ResultSet rs, ResultSetMetaData rsmd, Class<T> clazz)
			throws Exception {
		List list = new ArrayList();
		while (rs.next()) {
			T o = getPoJoFromResult(rs, rsmd, clazz);
			list.add(o);
		}
		return list;
	}

	/**
	 * 返回数据库查询结果集第index个字段的camel名称
	 * 
	 * @param rsmd
	 * @param index
	 * @param isCamelName
	 *            是否过滤非法字符后返回驼峰命名<br>
	 *            false:返回数据库的原生字段名称
	 * @return
	 * @throws SQLException
	 */
	private static String getCamelFieldName(ResultSetMetaData rsmd, int index, boolean isCamelName) throws SQLException {
		String columnName = rsmd.getColumnName(index);
		if (isCamelName) {
			columnName = columnName.replaceAll("[^a-zA-Z0-9]", " ");
			String fieldName = StringHelper.camel(columnName.toLowerCase());
			return fieldName;
		} else {
			return columnName;
		}
	}

	/**
	 * 填充数据
	 * 
	 * @param map
	 * @param table
	 */
	public static void fillData(Map<String, Object> map, String table) {

	}

	/**
	 * 分解sql语句为单条可执行的sql语句集合,并过滤注释
	 * 
	 * @param content
	 *            多条sql语句(可能包含注释，换行等信息)
	 * @return
	 */
	public static String[] parseSQL(String content) {
		char[] chars = content.toCharArray();
		List<String> statements = new ArrayList<String>();

		StamentStatus status = StamentStatus.NORMAL;
		StringBuffer buff = new StringBuffer();
		for (int index = 0; index < chars.length; index++) {
			char ch = chars[index];
			char next = '\0';
			switch (status) {
			case SINGLE_NOTE:
				if (ch == '\n' || ch == '\r') {
					buff.append(' ');
					status = StamentStatus.NORMAL;
				}
				break;
			case MULTI_NOTE:
				next = (index == chars.length - 1) ? '/' : chars[index + 1];
				if (ch == '*' && next == '/') {
					index++;
					status = StamentStatus.NORMAL;
				}
				break;
			case SINGLE_QUOTATION:
				buff.append(ch);
				if (ch == '\'') {
					status = StamentStatus.NORMAL;
				}
				break;
			case DOUBLE_QUOTATION:
				buff.append(ch);
				if (ch == '"') {
					status = StamentStatus.NORMAL;
				}
				break;
			case NORMAL:
				next = (index == chars.length - 1) ? ';' : chars[index + 1];
				if (ch == '-' && next == '-') {
					index++;
					status = StamentStatus.SINGLE_NOTE;
				} else if (ch == '/' && next == '*') {
					index++;
					status = StamentStatus.MULTI_NOTE;
				} else if (ch == '\'') {
					buff.append(ch);
					status = StamentStatus.SINGLE_QUOTATION;
				} else if (ch == '"') {
					buff.append(ch);
					status = StamentStatus.DOUBLE_QUOTATION;
				} else if (ch == ';') {
					String statement = buff.toString().trim();
					if ("".equals(statement) == false) {
						statements.add(statement);
					}
					buff = new StringBuffer();
				} else if (ch == '\n' || ch == '\r') {
					buff.append(' ');
				} else {
					buff.append(ch);
				}
				break;
			}
		}
		String statement = buff.toString().trim();
		if ("".equals(statement) == false) {
			statements.add(statement);
		}

		String[] stmts = new String[statements.size()];
		statements.toArray(stmts);
		return stmts;
	}

	/**
	 * 根据DataMap构造查询条件
	 * 
	 * @param map
	 * @return
	 */
	public static String getWhereCondiction(DataMap map) {
		if (map == null || map.size() == 0) {
			return "";
		}
		StringBuilder where = new StringBuilder();
		where.append(" where ");
		boolean isFirst = true;
		for (String key : map.keySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				where.append(" and ");
			}
			where.append(key).append("=?");
		}
		return where.toString();
	}

	public static Object normaliseValue(Object currVal) throws Exception {
		if (currVal == null) {
			return null;
		}
		TypeNormaliser tn = TypeNormaliserFactory.getNormaliser(currVal.getClass());
		if (tn != null) {
			currVal = tn.normalise(currVal);
		}
		return currVal;
	}
}
