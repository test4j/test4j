package org.jtester.module.dbfit;

import java.sql.SQLException;

import fit.Fixture;

/**
 * 在dbfit wiki文件中可以直接使用的数据库操作命令
 * 
 * @author darui.wudr
 * 
 */
public interface DbFitOp {

	/**
	 * 使用jtester默认的数据库配置类连接数据库<br>
	 * wiki语法<br>
	 * 
	 * <pre>
	 * |connect|你的数据库schema|
	 * </pre>
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	boolean connect() throws SQLException;

	/**
	 * 使用指定的driver来建立连接<br>
	 * wiki语法<br>
	 * 
	 * <pre>
	 * 	|connect|mysql|com.mysql.jdbc.Driver|jdbc:mysql://localhost/jtester?characterEncoding=UTF8|root|password|
	 * </pre>
	 * 
	 * @param type
	 *            数据库类型
	 * @param driver
	 *            驱动程序
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	boolean connect(String type, String driver, String url, String username, String password) throws Exception;

	/**
	 * 使用jtester的配置项连接数据库<br>
	 * wiki语法<br>
	 * 
	 * <pre>
	 * 	|connect from file|dbname|
	 * 	 
	 *  dbname	     定义在jtester.properties文件中的数据库名称
	 *  定义示例如下
	 * 	dbname.database.type=oracle
	 * 	dbname.database.url=jdbc:oracle:thin:@localhost:1523:crms?args[applicationEncoding=UTF-8,databaseEncoding=UTF-8]
	 * 	dbname.database.userName=yourdb_test
	 * 	dbname.database.password=ca
	 * 	dbname.database.driverClassName=com.alibaba.china.jdbc.SimpleDriver
	 * </pre>
	 * 
	 * @param dbname
	 *            定义在jtester.properties文件中的数据库名称<br>
	 * @return
	 * @throws Exception
	 */
	boolean connectFromFile(String dbname) throws Exception;

	/**
	 * 使用配置文件建立连接,prefix是数据库连接属性前缀
	 * 
	 * wiki语法<br>
	 * 
	 * <pre>
	 * 	|connect from file|dbname|db.properties|
	 * 	 
	 *  dbname	     定义在db.properties文件中的数据库名称
	 *  定义示例如下
	 * 	dbname.database.type=oracle
	 * 	dbname.database.url=jdbc:oracle:thin:@localhost:1523:crms?args[applicationEncoding=UTF-8,databaseEncoding=UTF-8]
	 * 	dbname.database.userName=yourdb_test
	 * 	dbname.database.password=ca
	 * 	dbname.database.driverClassName=com.alibaba.china.jdbc.SimpleDriver
	 * </pre>
	 * 
	 * @param dataSourceName
	 *            数据库配置的前缀, ${dataSourceName}.database.type =...
	 * @param propFile
	 *            如果为null，则读取jtester配置项
	 * @return
	 * @throws Exception
	 */
	boolean connectFromFile(String dataSourceName, String propFile) throws Exception;

	/**
	 * 设置dbfit wiki文件中用到的变量<br>
	 * wiki语法
	 * 
	 * <pre>
	 * 	|set parameter|my_name|davey.wu|
	 * 
	 *  在后续的wiki文件中可以用 @{my_name}引用这个变量
	 * </pre>
	 * 
	 * @param name
	 *            变量名称
	 * @param value
	 *            变量值
	 * @return
	 */
	boolean setParameter(String name, String value);

	/**
	 * query查询结果是单个值，以变量形式存储下来备用<br>
	 * wiki语法
	 * 
	 * <pre>
	 * 	|store query|select name from your_table where id=1|your_name|
	 *  
	 *  select返回值必须是单行单列的对象，结果被存储到your_name变量中
	 *  在后续wiki中可以用@{your_name}引用查询出来的对象值
	 * </pre>
	 * 
	 * @param para
	 * @param query
	 * @return
	 */
	Fixture storeQuery(String query, String symbolName);

	/**
	 * 清空dbfit的所有非系统变量<br>
	 * 
	 * <pre>
	 * dbfit中有4个系统变量
	 * <code>@{date}</code> 当前日期
	 * <code>@{datetime}</code> 当前时间
	 * <code>@{space}</code> 空格
	 * <code>@{at}</code> @ 字符
	 * </pre>
	 * 
	 * @return
	 */
	boolean clearParameters();

	/**
	 * 验证数据库数据<br>
	 * wiki语法
	 * 
	 * <pre>
	 * 	|query|select * from your_table where 你的条件|
	 * 	|field1|field2|
	 * 	|验证数据|验证数据|
	 *	|验证数据|验证数据|
	 *
	 * </pre>
	 * 
	 * @param query
	 *            查询sql语句
	 * @return
	 */
	Fixture query(String query);
}
