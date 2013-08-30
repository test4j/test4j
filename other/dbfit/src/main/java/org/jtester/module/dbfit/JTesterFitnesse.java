package org.jtester.module.dbfit;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jtester.module.database.environment.DBEnvironment;
import org.jtester.module.database.environment.DBEnvironmentFactory;
import org.jtester.module.dbfit.utility.DBFitSqlRunner;
import org.jtester.module.dbfit.utility.DbFitRunner;
import org.jtester.module.dbfit.utility.FitRunner;
import org.jtester.module.dbfit.utility.SymbolUtil;

@SuppressWarnings("rawtypes")
public class JTesterFitnesse {
	/**
	 * 通过程序来准备数据库数据<br>
	 * 一般用于在spring加载缓存资源时使用，在@BeforeClass中调用
	 * 
	 * @param clazz
	 * @param wiki
	 * @param wikis
	 */
	public void runDbFit(Class clazz, String wiki, String... wikis) {
		DbFitRunner.runDbFit(clazz, wiki, wikis);
	}

	public void runDbFit(Class clazz, boolean cleanSymbols, String wiki, String... wikis) {
		DbFitRunner.runDbFit(clazz, cleanSymbols, wiki, wikis);
	}

	/**
	 * 通过程序来准备数据库数据<br>
	 * 一般用于在spring加载缓存资源时使用，在@BeforeClass中调用<br>
	 * 或者用程序来整备wiki变量时使用
	 * 
	 * @param clazz
	 * @param symbols
	 * @param wiki
	 * @param wikis
	 */
	public void runDbFit(Class clazz, Map<String, ?> symbols, String wiki, String... wikis) {
		DbFitRunner.runDbFit(clazz, symbols, wiki, wikis);
	}

	/**
	 * 通过程序来运行fitnesse wiki文件<br>
	 * 一般用于在spring加载缓存资源时使用，在@BeforeClass中调用
	 * 
	 * @param clazz
	 * @param wiki
	 * @param wikis
	 */
	public void runFit(Class clazz, String wiki, String... wikis) {
		FitRunner.runFit(clazz, wiki, wikis);
	}

	/**
	 * 通过程序来运行fitnesse wiki文件<br>
	 * 一般用于在spring加载缓存资源时使用，在@BeforeClass中调用<br>
	 * 或者用程序来整备wiki变量时使用
	 * 
	 * @param clazz
	 * @param symbols
	 * @param wiki
	 * @param wikis
	 */
	public void runFit(Class clazz, Map<String, Object> symbols, String wiki, String... wikis) {
		FitRunner.runFit(clazz, symbols, wiki, wikis);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSymbol(String key) {
		Object o = SymbolUtil.getSymbol(key);
		return (T) o;
	}

	public void setSymbol(String key, Object value) {
		SymbolUtil.setSymbol(key, value);
	}

	public void setSymbols(Map<String, Object> symbols) {
		SymbolUtil.setSymbol(symbols);
	}

	public void cleanSymbols() {
		SymbolUtil.cleanSymbols();
	}

	/**
	 * 使用默认的数据源执行相关sql操作
	 */
	public void useDefaultDataSource() {
		DBEnvironment environment = DBEnvironmentFactory.getDefaultDBEnvironment();
		DBEnvironmentFactory.changeDBEnvironment(environment);
	}

	/**
	 * 使用指定的数据源执行相关sql操作
	 * 
	 * @param dataSourceName
	 */
	public void useSpecDataSource(String dataSourceName) {
		DBEnvironment environment = DBEnvironmentFactory.getDBEnvironment(dataSourceName);
		DBEnvironmentFactory.changeDBEnvironment(environment);
	}

	/**
	 * 使用指定的数据源执行相关sql操作
	 * 
	 * @param dataSourceName
	 *            数据源名称
	 * @param dataSourceFrom
	 *            数据源配置文件
	 */
	public void useSpecDataSource(String dataSourceName, String dataSourceFrom) {
		DBEnvironment environment = DBEnvironmentFactory.getDBEnvironment(dataSourceName, dataSourceFrom);
		DBEnvironmentFactory.changeDBEnvironment(environment);
	}

	/**
	 * 执行单条sql语句
	 * 
	 * @param sql
	 *            单条sql
	 * @param sqls
	 *            单条sql
	 * @throws SQLException
	 */
	public void execute(String sql, String... sqls) {
		DBFitSqlRunner.instance.execute(sql);
		for (String item : sqls) {
			DBFitSqlRunner.instance.execute(item);
		}
	}

	/**
	 * 提交操作
	 * 
	 * @throws SQLException
	 */
	public void commit() {
		DBFitSqlRunner.instance.commit();
	}

	/**
	 * 回滚操作
	 * 
	 * @throws SQLException
	 */
	public void rollback() {
		DBFitSqlRunner.instance.rollback();
	}

	/**
	 * 执行sql文件<br>
	 * 默认从classpath中读取<br>
	 * classpath:前缀开头，表示从classpath中读取<br>
	 * file:前缀开头，表示从文件系统中读取<br>
	 * 
	 * @param sqlFile
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public void executeSQLFile(String sqlFile) throws Exception {
		DBFitSqlRunner.instance.executeFromFile(sqlFile);
	}

	/**
	 * 替换sql文件中@{variable}变量后，在执行sql文件
	 * 
	 * @param symbols
	 * @param sqlFile
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public void executeSQLFile(Map<String, ?> symbols, String sqlFile) throws Exception {
		DBFitSqlRunner.instance.executeFromFile(symbols, sqlFile);
	}

	/**
	 * 根据sql查询数据，如果result是Map.class则返回Map类型<br>
	 * 如果是PoJo，则根据camel name命名方式初始化result
	 * 
	 * @param <T>
	 * @param sql
	 *            query语句
	 * @param claz
	 *            返回值类型
	 * @return
	 * @throws SQLException
	 */
	public <T> T query(String sql, Class<T> claz) {
		T result = DBFitSqlRunner.instance.query(sql, claz);
		return result;
	}

	/**
	 * 执行sql，返回查询数据列表，如果result是Map.class则返回Map列表<br>
	 * 如果是PoJo，则根据camel name命名方式初始化result，返回PoJo列表
	 * 
	 * @param <T>
	 * @param sql
	 *            查询语句
	 * @param claz
	 *            列表中数据类型
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryList(String sql, Class<T> clazz) {
		List<T> list = DBFitSqlRunner.instance.queryList(sql, clazz);
		return list;
	}
}
