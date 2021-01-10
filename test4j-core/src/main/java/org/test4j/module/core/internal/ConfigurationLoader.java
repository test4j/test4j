package org.test4j.module.core.internal;

import org.test4j.exception.Test4JException;
import org.test4j.module.ConfigHelper;
import org.test4j.tools.Logger;
import org.test4j.tools.commons.PropertiesReader;
import org.test4j.tools.commons.StrSubstitutor;

import java.util.Properties;

/**
 * test4j配置文件加载器<br>
 * 默认先加载test4j-default.properties文件中的配置项<br>
 * 根据 {@link #PROPKEY_CUSTOM_CONFIGURATION} 加载用户级的配置文件<br>
 * 根据 {@link #PROPKEY_LOCAL_CONFIGURATION} 加载项目级的配置文件
 */
public class ConfigurationLoader {

    /**
     * Name of the fixed configuration file that contains all defaults
     */
    public static final String DEFAULT_PROPERTIES_FILE_NAME = "test4j-default.properties";

    /**
     * Property in the defaults configuration file that contains the name of the
     * custom configuration file
     */
    public static final String PROPKEY_CUSTOM_CONFIGURATION = "test4j.configuration.customFileName";

    /**
     * Property in the defaults and/or custom configuration file that contains
     * the name of the user local configuration file
     */
    public static final String PROPKEY_LOCAL_CONFIGURATION = "test4j.configuration.localFileName";

    /**
     * reads properties from configuration file
     */
    private final PropertiesReader propertiesReader = new PropertiesReader();

    private static Properties properties = null;

    /**
     * loading the test4j configuration. <br>
     * Include test4j-default.properties, test4j.properties,
     * test4j-local.properties
     *
     * @return the settings, not null
     */
    public static synchronized Properties loading() {
        if (properties == null) {
            ConfigurationLoader loader = new ConfigurationLoader();
            properties = new Properties();

            loader.loadDefaultConfiguration(properties);
            loader.loadCustomConfiguration(properties);
            loader.loadLocalConfiguration(properties);
            loader.loadSystemProperties(properties);
            loader.expandPropertyValues(properties);
        }
        return properties;
    }

    private ConfigurationLoader() {

    }

    /**
     * Load the default properties file (test4j-default.properties)
     *
     * @param properties The instance to add to loaded properties to, not null
     */
    private void loadDefaultConfiguration(Properties properties) {
        Properties defaultProperties = propertiesReader.loadPropertiesFileFromClasspath(DEFAULT_PROPERTIES_FILE_NAME);
        if (defaultProperties == null) {
            throw new Test4JException("Configuration file: " + DEFAULT_PROPERTIES_FILE_NAME
                + " not found in classpath.");
        }
        properties.putAll(defaultProperties);
    }

    /**
     * Load the custom project level configuration file (test4j.properties)
     *
     * @param properties The instance to add to loaded properties to, not null
     */
    private void loadCustomConfiguration(Properties properties) {
        String customConfigurationFileName = getConfigurationFileName(PROPKEY_CUSTOM_CONFIGURATION, properties);
        Properties customProperties = propertiesReader.loadPropertiesFileFromClasspath(customConfigurationFileName);
        if (customProperties == null) {
            Logger.warn("No custom configuration file " + customConfigurationFileName + " found.");
        } else {
            properties.putAll(customProperties);
        }
    }

    /**
     * Load the local configuration file from the user home, or from the
     * classpath
     *
     * @param properties The instance to add to loaded properties to, not null
     */
    private void loadLocalConfiguration(Properties properties) {
        String localConfigurationFileName = getConfigurationFileName(PROPKEY_LOCAL_CONFIGURATION, properties);
        Properties localProperties = propertiesReader.loadPropertiesFileFromClasspath(localConfigurationFileName);
        if (localProperties == null) {
            localProperties = propertiesReader.loadPropertiesFileFromUserHome(localConfigurationFileName);
        }
        if (localProperties == null) {
            Logger.info("No local configuration file " + localConfigurationFileName + " found.");
        } else {
            properties.putAll(localProperties);
        }
    }

    /**
     * Load the environment properties.
     *
     * @param properties The instance to add to loaded properties to, not null
     */
    private void loadSystemProperties(Properties properties) {
        properties.putAll(System.getProperties());
    }

    /**
     * Expands all property place holders to actual values.<br>
     * <br>
     * For example suppose you have a property defined as follows:
     * root.dir=/usr/home <br>
     * Expanding following ${root.dir}/somesubdir will then give following
     * result: /usr/home/somesubdir
     *
     * @param properties The properties, not null
     */
    private void expandPropertyValues(Properties properties) {
        for (Object key : properties.keySet()) {
            Object value = properties.get(key);
            try {
                String expandedValue = StrSubstitutor.replace(value, properties);
                properties.put(key, expandedValue);
            } catch (Throwable e) {
                throw new Test4JException("Unable to load configuration. Could not expand property value for key: "
                    + key + ", value " + value, e);
            }
        }

    }

    /**
     * Gets the configuration file name from the system properties or if not
     * defined, from the given loaded properties. An exception is raised if no
     * value is defined.
     *
     * @param propertyName The name of the property that defines the
     *                     local/custom file name, not null
     * @param properties   The propertis that were already loaded, not null
     * @return The property value, not null
     */
    private String getConfigurationFileName(String propertyName, Properties properties) {
        String configurationFileName = System.getProperty(propertyName);
        if (configurationFileName != null) {
            return configurationFileName;
        }
        return ConfigHelper.getString(properties, propertyName);
    }
}