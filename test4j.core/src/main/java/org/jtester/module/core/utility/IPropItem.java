package org.jtester.module.core.utility;

/**
 * jTester配置常量属性
 * 
 * @author darui.wudr
 * 
 */
public interface IPropItem {
	/**
	 * jTester模块加载序列<br>
	 * Property that contains the names of the modules that are to be loaded.
	 */
	public static final String PROPKEY_MODULES = "jtester.modules";

	public static final String SPRING_APPLICATION_CONTEXT_FACTORY_CLASS_NAME = "SpringModule.ApplicationContextFactory.ImplClassName";

	public static final String SPRING_DATASOURCE_NAME = "SpringModule.DataSource.Name";

	public static final String LOG4J_XML_FILE = "log4j.xml.file";

	/**
	 * 默认的数据库事务模式
	 */
	public static final String TRANSACTIONAL_MODE_DEFAULT = "transactional.mode.default";

	public static final String dbexport_auto = "dbexport.auto";

	public static final String PROPKEY_DATABASE_TYPE = "database.type";

	public static final String PROPKEY_DATASOURCE_URL = "database.url";

	public static final String PROPKEY_DATASOURCE_SCHEMAS = "database.schemaNames";

	public static final String PROPKEY_DATASOURCE_USERNAME = "database.userName";

	public static final String PROPKEY_DATASOURCE_PASSWORD = "database.password";

	public static final String PROPKEY_DATASOURCE_DRIVERCLASSNAME = "database.driverClassName";

	public static final String DBMAINTAINER_DISABLECONSTRAINTS = "dbMaintainer.disableConstraints.enabled";

	public static final String CONNECT_ONLY_TESTDB = "database.only.testdb.allowing";
}
