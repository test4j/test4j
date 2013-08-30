package org.jtester.module.dbfit.fixture;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.database.environment.DBEnvironment;
import org.jtester.module.database.environment.DBEnvironmentFactory;
import org.jtester.module.dbfit.DbFitOp;
import org.jtester.module.dbfit.DbFitTestedContext;
import org.jtester.module.dbfit.DbFitTestedContext.RunIn;
import org.jtester.module.dbfit.environment.DbFitAbstractDBEnvironment;
import org.jtester.module.dbfit.environment.DbFitEnvironment;
import org.jtester.module.dbfit.fixture.fit.CleanFixture;
import org.jtester.module.dbfit.fixture.fit.CompareStoredQueriesFixture;
import org.jtester.module.dbfit.fixture.fit.DeleteFixture;
import org.jtester.module.dbfit.fixture.fit.ExecuteProcedureFixture;
import org.jtester.module.dbfit.fixture.fit.InsertFixture;
import org.jtester.module.dbfit.fixture.fit.InspectFixture;
import org.jtester.module.dbfit.fixture.fit.QueryFixture;
import org.jtester.module.dbfit.fixture.fit.QueryStatsFixture;
import org.jtester.module.dbfit.fixture.fit.StoreQueryFixture;
import org.jtester.module.dbfit.fixture.fit.StoreQueryTableFixture;
import org.jtester.module.dbfit.fixture.fit.UpdateFixture;
import org.jtester.module.dbfit.utility.DBFitSqlRunner;
import org.jtester.module.dbfit.utility.DbFixtureUtil;
import org.jtester.module.dbfit.utility.SymbolUtil;
import org.jtester.tools.commons.DateHelper;

import fit.Fixture;
import fitlibrary.SequenceFixture;
import fitlibrary.table.Table;
import fitlibrary.utility.TestResults;

public class DatabaseFixture extends SequenceFixture implements DbFitOp {

	public DatabaseFixture() {
	}

	@Override
	public void setUp(Table firstTable, TestResults testResults) {
		super.setUp(firstTable, testResults);
	}

	@Override
	public void tearDown(Table firstTable, TestResults testResults) {
		DbFitEnvironment environment = workingEnvironment();

		try {
			MessageHelper.info("tearDown dbfit table");
			if (environment == null) {
				return;
			}
			RunIn runIn = DbFitTestedContext.getRunIn();

			if (runIn == RunIn.TestCase) {
				commit();
			}
			environment.teardown();
		} catch (Throwable e) {
			this.exception(firstTable.parse, e);
		}
		super.tearDown(firstTable, testResults);
	}

	public boolean connect() throws SQLException {
		DbFitEnvironment environment = workingEnvironment();
		environment.connect();
		return true;
	}

	public boolean connect(String type, String driver, String url, String username, String password) throws Exception {
		DBEnvironment environment = DBEnvironmentFactory.getDBEnvironment(type, driver, url, username, password);
		DBEnvironmentFactory.changeDBEnvironment(environment);
		environment.connect();
		return true;
	}

	final static String NO_VALID_VALUE_MESSAGE = "can't find valid value of key[%s] in file[%s]!";

	public boolean connectFromFile(String dbname) throws Exception {
		DBEnvironment environment = DBEnvironmentFactory.getDBEnvironment(dbname);
		DBEnvironmentFactory.changeDBEnvironment(environment);
		environment.connect();

		return true;
	}

	public boolean connectFromFile(String dataSourceName, String propFile) throws Exception {
		DBEnvironment environment = DBEnvironmentFactory.getDBEnvironment(dataSourceName, propFile);
		DBEnvironmentFactory.changeDBEnvironment(environment);
		environment.connect();

		return true;
	}

	public boolean setParameter(String name, String value) {
		DbFixtureUtil.setParameter(name, value);
		return true;
	}

	public Fixture storeQuery(String query, String symbolName) {
		DbFitEnvironment environment = workingEnvironment();
		return new StoreQueryFixture(environment, query, symbolName);
	}

	/**
	 * 设置当前时间格式
	 * 
	 * @param format
	 * @return
	 */
	public boolean setDateTimeFormat(String format) {
		String datetime = DateHelper.currDateTimeStr(format);
		DbFixtureUtil.setParameter("datetime", datetime);
		return true;
	}

	/**
	 * 设置当前日期格式
	 * 
	 * @param format
	 * @return
	 */
	public boolean setDateFormat(String format) {
		String date = DateHelper.currDateTimeStr(format);
		DbFixtureUtil.setParameter("date", date);
		return true;
	}

	public boolean clearParameters() {
		SymbolUtil.cleanSymbols();
		return true;
	}

	public Fixture query(String query) {
		DbFitEnvironment environment = workingEnvironment();
		return new QueryFixture(environment, query);
	}

	public Fixture orderedQuery(String query) {
		DbFitEnvironment environment = workingEnvironment();
		return new QueryFixture(environment, query, true);
	}

	public boolean execute(String statement) {
		DbFitEnvironment environment = workingEnvironment();
		return DbFixtureUtil.execute(environment, statement);
	}

	public Fixture executeProcedure(String statement) {
		DbFitEnvironment environment = workingEnvironment();
		return new ExecuteProcedureFixture(environment, statement);
	}

	public Fixture executeProcedureExpectException(String statement) {
		DbFitEnvironment environment = workingEnvironment();
		return new ExecuteProcedureFixture(environment, statement, true);
	}

	public Fixture executeProcedureExpectException(String statement, int code) {
		DbFitEnvironment environment = workingEnvironment();
		return new ExecuteProcedureFixture(environment, statement, code);
	}

	public Fixture insert(String tableName) {
		DbFitEnvironment environment = workingEnvironment();
		return new InsertFixture(environment, tableName);
	}

	public Fixture update(String tableName) {
		DbFitEnvironment environment = workingEnvironment();
		return new UpdateFixture(environment, tableName);
	}

	public Fixture clean() {
		DbFitEnvironment environment = workingEnvironment();
		return new CleanFixture(environment);
	}

	public boolean cleanTable(String tables) {
		DbFitEnvironment environment = workingEnvironment();
		String ts[] = tables.split("[;,]");
		for (String table : ts) {
			DbFixtureUtil.cleanTable(environment, table);
		}
		return true;
	}

	/**
	 * 根据表字段删除数据
	 * 
	 * @param table
	 * @return
	 */
	public Fixture delete(String table) {
		DbFitEnvironment environment = workingEnvironment();
		return new DeleteFixture(environment, table);
	}

	public boolean rollback() throws SQLException {
		DbFitEnvironment environment = workingEnvironment();
		environment.rollback();
		environment.connect().setAutoCommit(false);
		return true;
	}

	public boolean commit() throws SQLException {
		DbFitEnvironment environment = workingEnvironment();
		environment.commit();
		environment.connect().setAutoCommit(false);
		return true;
	}

	public Fixture queryStats() {
		DbFitEnvironment environment = workingEnvironment();
		return new QueryStatsFixture(environment);
	}

	public Fixture inspectProcedure(String procName) {
		DbFitEnvironment environment = workingEnvironment();
		return new InspectFixture(environment, InspectFixture.MODE_PROCEDURE, procName);
	}

	public Fixture inspectTable(String tableName) {
		DbFitEnvironment environment = workingEnvironment();
		return new InspectFixture(environment, InspectFixture.MODE_TABLE, tableName);
	}

	public Fixture inspectView(String tableName) {
		DbFitEnvironment environment = workingEnvironment();
		return new InspectFixture(environment, InspectFixture.MODE_TABLE, tableName);
	}

	public Fixture inspectQuery(String query) {
		DbFitEnvironment environment = workingEnvironment();
		return new InspectFixture(environment, InspectFixture.MODE_QUERY, query);
	}

	/**
	 * 把查询结果的整张表作为结果存储下来 <br>
	 * storeQuery把查询结果是单个值，以变量形式存储下来
	 * 
	 * @param query
	 * @param symbolName
	 * @return
	 */
	public Fixture storeQueryTable(String query, String symbolName) {
		DbFitEnvironment environment = workingEnvironment();
		return new StoreQueryTableFixture(environment, query, symbolName);
	}

	public Fixture compareStoredQueries(String symbol1, String symbol2) {
		DbFitEnvironment environment = workingEnvironment();
		return new CompareStoredQueriesFixture(environment, symbol1, symbol2);
	}

	/**
	 * 执行指定文件中的sql语句
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	public boolean executeFile(String file) throws Exception {
		DBFitSqlRunner.instance.executeFromFile(file);
		return true;
	}

	/**
	 * 返回当前的数据库环境
	 * 
	 * @return
	 */
	public DbFitEnvironment workingEnvironment() {
		DBEnvironment environment = DBEnvironmentFactory.getCurrentDBEnvironment();
		DbFitEnvironment dbfit = DbFitAbstractDBEnvironment.convert(environment);
		return dbfit;
	}
}
