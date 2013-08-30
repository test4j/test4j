package org.jtester.module.dbfit.utility;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Map;

import org.jtester.module.database.environment.DBEnvironment;
import org.jtester.module.database.utility.SqlRunner;
import org.jtester.module.dbfit.environment.DbFitAbstractDBEnvironment;

public class DBFitSqlRunner extends SqlRunner {
	public static final DBFitSqlRunner instance = new DBFitSqlRunner();

	protected DBFitSqlRunner() {

	}

	/**
	 * 设置变量，执行sql文件
	 * 
	 * @param symbols
	 * @param fileName
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public void executeFromFile(Map<String, ?> symbols, String fileName) throws Exception {
		SymbolUtil.setSymbol(symbols);
		executeFromFile(fileName);
	}

	@Override
	protected DBEnvironment getCurrentEnvironment() {
		DBEnvironment environment = super.getCurrentEnvironment();
		return DbFitAbstractDBEnvironment.convert(environment);
	}
}
