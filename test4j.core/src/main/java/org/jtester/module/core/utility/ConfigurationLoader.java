package org.jtester.module.core.utility;

import java.util.Properties;

import org.jtester.module.JTesterException;
import org.jtester.tools.commons.ConfigHelper;
import org.jtester.tools.commons.PropertiesReader;

import ext.jtester.apache.commons.lang.text.StrSubstitutor;

/**
 * jtester配置文件加载器<br>
 * 默认先加载jtester-default.properties文件中的配置项<br>
 * 根据 {@link #PROPKEY_CUSTOM_CONFIGURATION} 加载用户级的配置文件<br>
 * 根据 {@link #PROPKEY_LOCAL_CONFIGURATION} 加载项目级的配置文件
 * 
 */
public class ConfigurationLoader {

	/**
	 * Name of the fixed configuration file that contains all defaults
	 */
	public static final String DEFAULT_PROPERTIES_FILE_NAME = "jtester-default.properties";

	/**
	 * Property in the defaults configuration file that contains the name of the
	 * custom configuration file
	 */
	public static final String PROPKEY_CUSTOM_CONFIGURATION = "jtester.configuration.customFileName";

	/**
	 * Property in the defaults and/or custom configuration file that contains
	 * the name of the user local configuration file
	 */
	public static final String PROPKEY_LOCAL_CONFIGURATION = "jtester.configuration.localFileName";

	/**
	 * reads properties from configuration file
	 */
	private PropertiesReader propertiesReader = new PropertiesReader();

	private static Properties properties = null;

	/**
	 * loading the jTester configuration. <br>
	 * Include jtester-default.properties, jtester.properties,
	 * jtester-local.properties
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
	 * Load the default properties file (jtester-default.properties)
	 * 
	 * @param properties
	 *            The instance to add to loaded properties to, not null
	 */
	private void loadDefaultConfiguration(Properties properties) {
		Properties defaultProperties = propertiesReader.loadPropertiesFileFromClasspath(DEFAULT_PROPERTIES_FILE_NAME);
		if (defaultProperties == null) {
			throw new JTesterException("Configuration file: " + DEFAULT_PROPERTIES_FILE_NAME
					+ " not found in classpath.");
		}
		properties.putAll(defaultProperties);
	}

	/**
	 * Load the custom project level configuration file (jtester.properties)
	 * 
	 * @param properties
	 *            The instance to add to loaded properties to, not null
	 */
	private void loadCustomConfiguration(Properties properties) {
		String customConfigurationFileName = getConfigurationFileName(PROPKEY_CUSTOM_CONFIGURATION, properties);
		Properties customProperties = propertiesReader.loadPropertiesFileFromClasspath(customConfigurationFileName);
		if (customProperties == null) {
			MessageHelper.warn("No custom configuration file " + customConfigurationFileName + " found.");
		} else {
			properties.putAll(customProperties);
		}
	}

	/**
	 * Load the local configuration file from the user home, or from the
	 * classpath
	 * 
	 * @param properties
	 *            The instance to add to loaded properties to, not null
	 */
	private void loadLocalConfiguration(Properties properties) {
		String localConfigurationFileName = getConfigurationFileName(PROPKEY_LOCAL_CONFIGURATION, properties);
		Properties localProperties = propertiesReader.loadPropertiesFileFromClasspath(localConfigurationFileName);
		if (localProperties == null) {
			localProperties = propertiesReader.loadPropertiesFileFromUserHome(localConfigurationFileName);
		}
		if (localProperties == null) {
			MessageHelper.info("No local configuration file " + localConfigurationFileName + " found.");
		} else {
			properties.putAll(localProperties);
		}
	}

	/**
	 * Load the environment properties.
	 * 
	 * @param properties
	 *            The instance to add to loaded properties to, not null
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
	 * @param properties
	 *            The properties, not null
	 */
	private void expandPropertyValues(Properties properties) {
		for (Object key : properties.keySet()) {
			Object value = properties.get(key);
			try {
				String expandedValue = StrSubstitutor.replace(value, properties);
				properties.put(key, expandedValue);
			} catch (Throwable e) {
				throw new JTesterException("Unable to load configuration. Could not expand property value for key: "
						+ key + ", value " + value, e);
			}
		}

	}

	/**
	 * Gets the configuration file name from the system properties or if not
	 * defined, from the given loaded properties. An exception is raised if no
	 * value is defined.
	 * 
	 * @param propertyName
	 *            The name of the property that defines the local/custom file
	 *            name, not null
	 * @param properties
	 *            The propertis that were already loaded, not null
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
