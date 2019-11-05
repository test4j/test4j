package org.test4j.tools.commons;

import org.test4j.exception.Test4JException;
import org.test4j.module.core.utility.MessageHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    /**
     * Loads the properties file with the given name, which is available in the
     * user home folder. If no file with the given name is found, null is
     * returned.
     *
     * @param propertiesFileName The name of the properties file
     * @return The Properties object, null if the properties file wasn't found.
     */
    public Properties loadPropertiesFileFromUserHome(String propertiesFileName) {
        String userHomeDir = System.getProperty("user.home");
        File localPropertiesFile = new File(userHomeDir, propertiesFileName);
        if (!localPropertiesFile.exists()) {
            return null;
        }
        try (InputStream inputStream = new FileInputStream(localPropertiesFile)) {
            if ("".equals(propertiesFileName)) {
                throw new IllegalArgumentException("Properties Filename must be given.");
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            MessageHelper.info("Loaded configuration file " + propertiesFileName + " from user home");
            return properties;
        } catch (FileNotFoundException e) {
            return null;
        } catch (Throwable e) {
            throw new Test4JException("Unable to load configuration file: " + propertiesFileName + " from user home",
                    e);
        }
    }

    /**
     * Loads the properties file with the given name, which is available in the
     * classpath. If no file with the given name is found, null is returned.
     *
     * @param propertiesFileName The name of the properties file
     * @return The Properties object, null if the properties file wasn't found.
     */
    public Properties loadPropertiesFileFromClasspath(String propertiesFileName) {
        if ("".equals(propertiesFileName)) {
            throw new IllegalArgumentException("Properties Filename must be given.");
        }
        Properties properties = new Properties();
        try (InputStream inputStream = ResourceHelper.getResourceAsStream(PropertiesReader.class.getClassLoader(), propertiesFileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
            return properties;
        } catch (FileNotFoundException e) {
            return properties;
        } catch (Throwable e) {
            throw new Test4JException("Unable to load configuration file: " + propertiesFileName, e);
        }
    }
}
