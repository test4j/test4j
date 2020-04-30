package org.test4j.tools.commons;

import org.test4j.exception.Test4JException;
import org.test4j.module.core.internal.IPropItem;
import org.test4j.module.core.utility.ConfigurationLoader;
import org.test4j.module.core.utility.MessageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.test4j.tools.commons.ClazzHelper.createInstanceOfType;

/**
 * test4j配置文件工具类
 *
 * @author darui.wudr
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConfigHelper implements IPropItem {

    private static Properties properties = ConfigurationLoader.loading();

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
        String value = properties.getProperty(key);
        return value;
    }

    /**
     * 获取test4j配置项
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static String getString(String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (StringHelper.isBlank(value)) {
            return defaultValue;
        } else {
            return value.trim();
        }
    }

    /**
     * 先从properties中获取key的属性值，如果找不到再从test4j配置中查找<br>
     * <br>
     * Gets the string value for the property with the given name.<br>
     * If no such property is found or the value is empty, then to find in
     * test4j configuration.<br>
     * If the final value is empty or null, an exception will be raised.
     *
     * @param properties
     * @param key
     * @return
     */
    public static String getString(Properties properties, String key) {
        String value = getString(properties, key, "");
        if (StringHelper.isBlank(value)) {
            throw new Test4JException("No value found for property " + key);
        } else {
            return value.trim();
        }
    }

    /**
     * Gets the string value for the property with the given name. If no such
     * property is found or the value is empty, the given default value is
     * returned.
     *
     * @param properties   The name, not null
     * @param key          The properties, not null
     * @param defaultValue The default value
     * @return The trimmed string value, not null
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        String value = null;
        if (properties == null) {
            value = properties.getProperty(key);
        } else {
            value = properties.getProperty(key);
            if (StringHelper.isBlank(value)) {
                value = properties.getProperty(key);
            }
        }
        if (StringHelper.isBlank(value)) {
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
     * @param propertyName The name, not null
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
     * @param propertyName The name, not null
     * @param required     If true an exception will be raised when the property is
     *                     not found or empty
     * @return The trimmed string list, empty or exception if none found
     */
    public static List<String> getStringList(String propertyName, boolean required) {
        String values = getString(propertyName);
        if (values == null || "".equals(values.trim())) {
            if (required) {
                throw new Test4JException("No value found for property " + propertyName);
            }
            return new ArrayList<String>(0);
        }
        String[] splitValues = values.split(",");
        List<String> result = new ArrayList<String>(splitValues.length);
        for (String value : splitValues) {
            result.add(value.trim());
        }

        if (required && result.isEmpty()) {
            throw new Test4JException("No value found for property " + propertyName);
        }
        return result;
    }

    public static boolean getBoolean(String key) {
        String prop = properties.getProperty(key);
        return "true".equalsIgnoreCase(prop);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (StringHelper.isBlank(value)) {
            return defaultValue;
        } else {
            return "true".equalsIgnoreCase(value);
        }
    }

    public static int getInteger(String key, int defaultValue) {
        String prop = properties.getProperty(key);
        try {
            int i = Integer.valueOf(prop);
            return i;
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * Checks whether the property with the given name exists in the test4j or
     * in the given properties.
     *
     * @param key The property name, not null
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
     * @param clazNameProperty the property of class name, not null.
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
     * @param type                              The type of the instance
     * @param implementationDiscriminatorValues The values that define which
     *                                          specific implementation class should be used. This is
     *                                          typically an environment specific property, like the DBMS that
     *                                          is used.
     * @return The configured instance
     */
    public static <T> T getInstanceOf(Class<? extends T> type, String... implementationDiscriminatorValues) {
        String implClassName = getConfiguredClassName(type, implementationDiscriminatorValues);
        MessageHelper.debug("Creating instance of " + type + ". Implementation class " + implClassName);
        return (T) createInstanceOfType(implClassName);
    }

    /**
     * 返回数据库列表
     *
     * @return
     */
    public static List<String> getDataSourceList() {
        String list = getString(DB_DATASOURCE_LIST);
        return Arrays.stream(list.split("[,;]"))
                .filter(item -> !StringHelper.isBlank(item))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * 返回默认的数据库名称
     *
     * @return
     */
    public static String getDefaultDataSource() {
        String dataSource = getString(DB_DATASOURCE_DEFAULT_NAME);
        return StringHelper.isBlank(dataSource) ? DB_DATASOURCE_DEFAULT : dataSource.trim();
    }

    /**
     * 返回指定数据源的数据库类型
     *
     * @param dataSource
     * @return
     */
    public static String databaseType(String dataSource) {
        return getDataSourceKey(dataSource, PROP_KEY_DATASOURCE_TYPE);
    }

    /**
     * 返回默认的数据库类型
     *
     * @return
     */
    public static String defaultDatabaseType() {
        return databaseType(getDefaultDataSource());
    }

    public static String getDataSourceKey(String dataSourceName, String key) {
        String propKey = String.format("db.%s.%s", dataSourceName, key);
        return getString(propKey, "");
    }

    /**
     * 除非显示的声明了database.istest=false，否则test4j认为只能连接测试库
     *
     * @return
     */
    public static boolean doesOnlyTestDatabase() {
        String onlyTest = properties.getProperty(CONNECT_ONLY_TESTDB);
        return !"FALSE".equalsIgnoreCase(onlyTest);
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
     * @param type                              The type of the instance
     * @param implementationDiscriminatorValues The values that define which
     *                                          specific implementation class should be used. This is
     *                                          typically an environment specific property, like the DBMS that
     *                                          is used.
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
        throw new Test4JException("Missing configuration for " + propKey);
    }
}
