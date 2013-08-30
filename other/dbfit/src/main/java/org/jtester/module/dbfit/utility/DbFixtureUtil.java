package org.jtester.module.dbfit.utility;

import java.sql.PreparedStatement;

import org.jtester.module.database.utility.DBHelper;
import org.jtester.module.dbfit.environment.DbFitEnvironment;

public class DbFixtureUtil {
	/**
	 * 执行statement语句
	 * 
	 * @param dbEnvironment
	 * @param statement
	 * @return
	 */
	public static final boolean execute(final DbFitEnvironment dbEnvironment, final String statement) {
		PreparedStatement st = null;
		try {
			st = dbEnvironment.createStatementWithBoundFixtureSymbols(statement);
			// return st.execute();
			// 注释掉的原因是apache的dbcp中st.execute()执行成功也是返回false，郁闷！
			st.execute();
			return true;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			DBHelper.closeStatement(st);
			st = null;
		}
	}

	/**
	 * 清除表table中所有数据
	 * 
	 * @param env
	 * @param table
	 */
	public static void cleanTable(DbFitEnvironment env, String table) {
		PreparedStatement st = null;
		try {
			String statement = String.format("delete from %s ", table);
			st = env.createStatementWithBoundFixtureSymbols(statement);
			st.execute();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			DBHelper.closeStatement(st);
			st = null;
		}
	}

	public static void setParameter(String name, String value) {
		if (value == null || "<null>".equals(value.toString().toLowerCase())) {
			SymbolUtil.setSymbol(name, null);
		} else if (value != null && value.toString().startsWith("<<")) {
			String varname = value.toString().substring(2);
			if (!name.equals(varname)) {
				SymbolUtil.setSymbol(name, org.jtester.module.dbfit.utility.SymbolUtil.getSymbol(varname));
			}
		} else {
			SymbolUtil.setSymbol(name, value);
		}
	}
}
