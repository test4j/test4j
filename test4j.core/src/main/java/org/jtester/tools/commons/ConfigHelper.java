package org.jtester.tools.commons;

import static org.jtester.tools.commons.ClazzHelper.createInstanceOfType;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jtester.module.JTesterException;
import org.jtester.module.core.utility.IPropItem;
import org.jtester.module.core.utility.ConfigurationLoader;
import org.jtester.module.core.utility.MessageHelper;

/**
 * jtester配置文件工具类
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ConfigHelper implements IPropItem {

	/**
	 * Property key of the SQL dialect of the underlying DBMS implementation
	 */
	public static final String PROPKEY_DATABASE_DIALECT = "database.dialect";

	private static Properties properties = null;

	/**
	 * Returns all properties that are used to configure jtester.
	 * 
	 * @return a <code>Properties</code> object
	 */
	public static Properties getConfiguration() {
		if (properties == null) {
			properties = ConfigurationLoader.loading();
		}
		return properties;
	}

	/**
	 * 返回log级别
	 * 
	 * @return
	 */
	public static int logLevel() {
		String level = getString("log.level", "INFO");
		if ("DEBUG".equalsIgnoreCase(level)) {
			return MessageHelper.DEBUG;
		} else if ("INFO".equalsIgnoreCase(level)) {
			return MessageHelper.INFO;
		} else if ("WARNING".equalsIgnoreCase(level)) {
			return MessageHelper.WARNING;
		} else if ("ERROR".equalsIgnoreCase(level)) {
			return MessageHelper.ERROR;
		}
		return MessageHelper.INFO;
	}

	public static String getString(String key) {
		String value = getConfiguration().getProperty(key);
		return value;
	}

	/**
	 * 获取jTester配置项
	 * 
	 * @param key
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static String getString(String key, String defaultValue) {
		String value = getConfiguration().getProperty(key);
		if (StringHelper.isBlankOrNull(value)) {
			return defaultValue;
		} else {
			return value.trim();
		}
	}

	/**
	 * 先从properties中获取key的属性值，如果找不到再从jtester配置中查找<br>
	 * <br>
	 * Gets the string value for the property with the given name.<br>
	 * If no such property is found or the value is empty, then to find in
	 * jTester configuration.<br>
	 * If the final value is empty or null, an exception will be raised.
	 * 
	 * @param properties
	 * @param key
	 * @return
	 */
	public static String getString(Properties properties, String key) {
		String value = getString(properties, key, "");
		if (StringHelper.isBlankOrNull(value)) {
			throw new JTesterException("No value found for property " + key);
		} else {
			return value.trim();
		}
	}

	/**
	 * Gets the string value for the property with the given name. If no such
	 * property is found or the value is empty, the given default value is
	 * returned.
	 * 
	 * @param propertyName
	 *            The name, not null
	 * @param defaultValue
	 *            The default value
	 * @param properties
	 *            The properties, not null
	 * @return The trimmed string value, not null
	 */
	public static String getString(Properties properties, String key, String defaultValue) {
		String value = null;
		if (properties == null) {
			value = getConfiguration().getProperty(key);
		} else {
			value = properties.getProperty(key);
			if (StringHelper.isBlankOrNull(value)) {
				value = getConfiguration().getProperty(key);
			}
		}
		if (StringHelper.isBlankOrNull(value)) {
			return defaultValue;
		} else {
			return value.trim();
		}
	}

	/**
	 * Gets the list of comma separated string values for the property with the
	 * given name. If no such property is found or the value is empty, an empty
	 * list is returned. Empty elements (",,") will not be added. A space
	 * (", ,") is not empty, a "" will be added.
	 * 
	 * @param propertyName
	 *            The name, not null
	 * @return The trimmed string list, empty if none found
	 */
	public static List<String> getStringList(String propertyName) {
		return getStringList(propertyName, false);

	}

	/**
	 * Gets the list of comma separated string values for the property with the
	 * given name. If no such property is found or the value is empty, an empty
	 * list is returned if not required, else an exception is raised. Empty
	 * elements (",,") will not be added. A space (", ,") is not empty, a ""
	 * will be added.
	 * 
	 * @param propertyName
	 *            The name, not null
	 * @param required
	 *            If true an exception will be raised when the property is not
	 *            found or empty
	 * @return The trimmed string list, empty or exception if none found
	 */
	public static List<String> getStringList(String propertyName, boolean required) {
		String values = getString(propertyName);
		if (values == null || "".equals(values.trim())) {
			if (required) {
				throw new JTesterException("No value found for property " + propertyName);
			}
			return new ArrayList<String>(0);
		}
		String[] splitValues = values.split(",");
		List<String> result = new ArrayList<String>(splitValues.length);
		for (String value : splitValues) {
			result.add(value.trim());
		}

		if (required && result.isEmpty()) {
			throw new JTesterException("No value found for property " + propertyName);
		}
		return result;
	}

	public static boolean getBoolean(String key) {
		String prop = getConfiguration().getProperty(key);
		return "true".equalsIgnoreCase(prop);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = getConfiguration().getProperty(key);
		if (StringHelper.isBlankOrNull(value)) {
			return defaultValue;
		} else {
			return "true".equalsIgnoreCase(value);
		}
	}

	public static int getInteger(String key, int defaultValue) {
		String prop = getConfiguration().getProperty(key);
		try {
			int i = Integer.valueOf(prop);
			return i;
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	/**
	 * Checks whether the property with the given name exists in the jTester or
	 * in the given properties.
	 * 
	 * @param key
	 *            The property name, not null
	 * 
	 * @return True if the property exitsts
	 */
	public static boolean hasProperty(String key) {
		String value = getString(key);
		return value != null;
	}

	/**
	 * Gets an instance of the type specified by the property with the given
	 * name. If no such property is found, the value is empty or the instance
	 * cannot be created, an exception will be raised.
	 * 
	 * @param clazNameProperty
	 *            the property of class name, not null.
	 * @return The instance value, not null
	 */

	public static <T> T getInstance(String clazNameProperty) {
		String className = getString(clazNameProperty);
		return (T) createInstanceOfType(className);
	}

	/**
	 * Retrieves the concrete instance of the class with the given type as
	 * configured by the given <code>Configuration</code>. Tries to retrieve a
	 * specific implementation first (propery key = fully qualified name of the
	 * interface type + '.impl.className.' + implementationDiscriminatorValue).
	 * If this key does not exist, the generally configured instance is
	 * retrieved (same property key without the
	 * implementationDiscriminatorValue).
	 * 
	 * @param type
	 *            The type of the instance
	 * @param implementationDiscriminatorValues
	 *            The values that define which specific implementation class
	 *            should be used. This is typically an environment specific
	 *            property, like the DBMS that is used.
	 * 
	 * @return The configured instance
	 */
	public static <T> T getInstanceOf(Class<? extends T> type, String... implementationDiscriminatorValues) {
		String implClassName = getConfiguredClassName(type, implementationDiscriminatorValues);
		MessageHelper.debug("Creating instance of " + type + ". Implementation class " + implClassName);
		return (T) createInstanceOfType(implClassName);
	}

	/**
	 * 默认数据库驱动class
	 * 
	 * @return
	 */
	public static String databaseDriver() {
		return getConfiguration().getProperty(PROPKEY_DATASOURCE_DRIVERCLASSNAME);
	}

	/**
	 * 默认数据库连接url
	 * 
	 * @return
	 */
	public static String databaseUrl() {
		return getConfiguration().getProperty(PROPKEY_DATASOURCE_URL);
	}

	public static String databaseUserName() {
		return getConfiguration().getProperty(PROPKEY_DATASOURCE_USERNAME);
	}

	public static String databasePassword() {
		return getConfiguration().getProperty(PROPKEY_DATASOURCE_PASSWORD);
	}

	public static boolean doesDisableConstraints() {
		String disableConstraints = getConfiguration().getProperty(DBMAINTAINER_DISABLECONSTRAINTS);
		return "TRUE".equalsIgnoreCase(disableConstraints);
	}

	/**
	 * 除非显示的声明了database.istest=false，否则jtester认为只能连接测试库
	 * 
	 * @return
	 */
	public static boolean doesOnlyTestDatabase() {
		String onlytest = getConfiguration().getProperty(CONNECT_ONLY_TESTDB);
		return !"FALSE".equalsIgnoreCase(onlytest);
	}

	/**
	 * 判断是否是spring的data source bean name<br>
	 * <br>
	 * beanName.equals("spring.datasource.name")是为了兼容老版本中属性的定义
	 * 
	 * @param beanName
	 * @return
	 */
	public static boolean isSpringDataSourceName(String beanName) {
		String dataSourceName = getConfiguration().getProperty(SPRING_DATASOURCE_NAME);
		// beanName.equals("spring.datasource.name")是为了兼容老版本中属性的定义
		return beanName.equals(dataSourceName) || beanName.equals("spring.datasource.name");
	}

	public static boolean autoExport() {
		String auto = getConfiguration().getProperty(dbexport_auto);
		if (auto != null && auto.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isScript() {
		String script = getConfiguration().getProperty("dbexport.script");
		if (script != null && script.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 默认的数据库类型
	 * 
	 * @return
	 */
	public static String databaseType() {
		String type = System.getProperty(PROPKEY_DATABASE_TYPE);// from vm
		if (!StringHelper.isBlankOrNull(type)) {
			return type;
		}
		// from jTester property
		type = getConfiguration().getProperty(PROPKEY_DATABASE_TYPE);
		return type;
	}

	/**
	 * dbfit测试结果文件输出目录
	 * 
	 * @return
	 */
	public static String dbfitDir() {
		String dir = getConfiguration().getProperty("dbfit.dir");
		return StringHelper.isBlankOrNull(dir) ? "target/dbfit" : dir;
	}

	public static void disableDbMaintain() {
		// disable dbmaintainer properties
		getConfiguration().setProperty("updateDataBaseSchema.enabled", "false");
		getConfiguration().setProperty("dbMaintainer.dbVersionSource.autoCreateVersionTable", "false");
	}

	/**
	 * Retrieves the class name of the concrete instance of the class with the
	 * given type as configured by the given <code>Configuration</code>. Tries
	 * to retrieve a specific implementation first (propery key = fully
	 * qualified name of the interface type + '.impl.className.' +
	 * implementationDiscriminatorValue). If this key does not exist, the
	 * generally configured instance is retrieved (same property key without the
	 * implementationDiscriminatorValue).
	 * 
	 * @param type
	 *            The type of the instance
	 * @param implementationDiscriminatorValues
	 *            The values that define which specific implementation class
	 *            should be used. This is typically an environment specific
	 *            property, like the DBMS that is used.
	 * @return The configured class name
	 */
	public static String getConfiguredClassName(Class type, String... implementationDiscriminatorValues) {
		String propKey = type.getName() + ".implClassName";

		// first try specific instance using the given discriminators
		if (implementationDiscriminatorValues != null) {
			String implementationSpecificPropKey = propKey;
			for (String implementationDiscriminatorValue : implementationDiscriminatorValues) {
				implementationSpecificPropKey += '.' + implementationDiscriminatorValue;
			}
			if (ConfigHelper.hasProperty(implementationSpecificPropKey)) {
				return getString(implementationSpecificPropKey);
			}
		}

		// specifig not found, try general configured instance
		if (ConfigHelper.hasProperty(propKey)) {
			return getString(propKey);
		}

		// no configuration found
		throw new JTesterException("Missing configuration for " + propKey);
	}
}
