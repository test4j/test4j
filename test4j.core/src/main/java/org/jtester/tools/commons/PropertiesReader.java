package org.jtester.tools.commons;

import static ext.jtester.apache.commons.io.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.jtester.module.JTesterException;
import org.jtester.module.core.utility.MessageHelper;

public class PropertiesReader {

	/**
	 * Loads the properties file with the given name, which is available in the
	 * user home folder. If no file with the given name is found, null is
	 * returned.
	 * 
	 * @param propertiesFileName
	 *            The name of the properties file
	 * @return The Properties object, null if the properties file wasn't found.
	 */
	public Properties loadPropertiesFileFromUserHome(String propertiesFileName) {
		InputStream inputStream = null;
		try {
			if ("".equals(propertiesFileName)) {
				throw new IllegalArgumentException("Properties Filename must be given.");
			}
			Properties properties = new Properties();
			String userHomeDir = System.getProperty("user.home");
			File localPropertiesFile = new File(userHomeDir, propertiesFileName);
			if (!localPropertiesFile.exists()) {
				return null;
			}
			inputStream = new FileInputStream(localPropertiesFile);
			properties.load(inputStream);
			MessageHelper.info("Loaded configuration file " + propertiesFileName + " from user home");
			return properties;

		} catch (FileNotFoundException e) {
			return null;
		} catch (Throwable e) {
			throw new JTesterException("Unable to load configuration file: " + propertiesFileName + " from user home",
					e);
		} finally {
			closeQuietly(inputStream);
		}
	}

	/**
	 * Loads the properties file with the given name, which is available in the
	 * classpath. If no file with the given name is found, null is returned.
	 * 
	 * @param propertiesFileName
	 *            The name of the properties file
	 * @return The Properties object, null if the properties file wasn't found.
	 */
	public Properties loadPropertiesFileFromClasspath(String propertiesFileName) {
		InputStream inputStream = null;
		try {
			if ("".equals(propertiesFileName)) {
				throw new IllegalArgumentException("Properties Filename must be given.");
			}
			Properties properties = new Properties();
			inputStream = ResourceHelper.getResourceAsStream(propertiesFileName);
			if (inputStream == null) {
				return null;
			}
			properties.load(inputStream);
			return properties;
		} catch (FileNotFoundException e) {
			return null;
		} catch (Throwable e) {
			throw new JTesterException("Unable to load configuration file: " + propertiesFileName, e);
		} finally {
			closeQuietly(inputStream);
		}
	}
}
