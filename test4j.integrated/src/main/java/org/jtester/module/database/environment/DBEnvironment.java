package org.jtester.module.database.environment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jtester.module.database.transaction.TransactionManagementConfiguration;

public interface DBEnvironment {
	/**
	 * 默认的数据源名称
	 */
	public final static String DEFAULT_DATASOURCE_NAME = "DEFAULT";

	/**
	 * jTester配置文件
	 */
	public final static String DEFAULT_DATASOURCE_FROM = "JTESTER-PROPERTIES";

	/**
	 * 自定义数据源
	 */
	public final static String CUSTOMIZED_DATASOURCE_NAME = "CUSTOMIZED";

	/**
	 * 设置数据库连接属性
	 * 
	 * @param driver
	 *            驱动类
	 * @param url
	 *            连接url
	 * @param username
	 *            数据库用户
	 * @param password
	 *            用户密码
	 */
	void setDataSource(String driver, String url, String schemas, String username, String password);

	/**
	 * 获取当前的DataSource
	 * 
	 * @param withTransactionManager
	 *            是否包装事务
	 * @return
	 */
	DataSource getDataSource();

	/**
	 * 连接数据源
	 * 
	 * @throws SQLException
	 */
	Connection connect();

	/**
	 * Retrieve an exception code from a database exception. This method should
	 * perform any required conversion between a JDBC exception and the real
	 * database error code.
	 */
	int getExceptionCode(SQLException ex);

	/**
	 * Create a {@link PreparedStatement} object and binds fixture symbols to
	 * SQL statement parameters with matching names.
	 */
	PreparedStatement createStatementWithBoundFixtureSymbols(String commandText) throws SQLException;

	/**
	 * 获得数据表的元信息
	 * 
	 * @param table
	 * @return
	 * @throws Exception
	 */
	TableMeta getTableMetaData(String table);

	/**
	 * 返回指定类型的默认值
	 * 
	 * @param javaType
	 * @return
	 */
	Object getDefaultValue(String javaType);

	/**
	 * 将字符串类型转换为java对象
	 * 
	 * @param input
	 * @param javaType
	 * @return
	 */
	Object toObjectValue(String input, String javaType);

	/**
	 * 返回数据库的字段引号符(mysql:"'"),oracle(")
	 * 
	 * @return
	 */
	String getFieldQuato();

	/**
	 * 将java对象转换为对应的SQL对象<br>
	 * 比如: java.util.Date对象在oracle插入时必须转为java.sql.Date对象
	 * 
	 * @param value
	 * @return
	 */
	Object converToSqlValue(Object value);

	// =======================================
	/**
	 * 获取事务包装的数据源
	 * 
	 * @return
	 */
	DataSource getDataSourceAndActivateTransactionIfNeeded();

	/**
	 * 注册事务配置信息，如果配置为空，则注册默认的配置
	 * 
	 * @param transactionManagementConfiguration
	 */
	void registerTransactionManagementConfiguration(
			TransactionManagementConfiguration transactionManagementConfiguration);

	/**
	 * 事务开始
	 */
	void startTransaction();

	/**
	 * 结束事务，提交或回滚
	 */
	void endTransaction();

	/**
	 * Commit current transaction.
	 */
	void commit();

	/**
	 * Rollback current transaction.
	 */
	void rollback();

}
