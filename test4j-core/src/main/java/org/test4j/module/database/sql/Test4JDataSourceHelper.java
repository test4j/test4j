package org.test4j.module.database.sql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.utility.DataSourceType;
import org.test4j.tools.commons.ConfigHelper;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Set;

import static org.test4j.module.core.internal.IPropItem.*;
import static org.test4j.module.database.sql.Test4JDataSource.wrapper;

public class Test4JDataSourceHelper {
    private static final Set<String> registered = new HashSet<>();

    static Test4JDataSource createLocalDataSource(String dataSourceName) {
        DataSourceType type = type(dataSourceName);
        String schemaName = ConfigHelper.getDataSourceKey(dataSourceName, PROP_KEY_DATASOURCE_SCHEMA);

        checkDoesTestDB(type, dataSourceName, schemaName);
        registerDriver(dataSourceName);
        MessageHelper.info("Creating data source. Driver: " + driver(dataSourceName) + ", url: " + url(dataSourceName) + ", user: " + username(dataSourceName)
                + ", password: <not shown>");

        BasicDataSource dataSource = new BasicDataSource();
        {
            dataSource.setDriverClassName(driver(dataSourceName));
            dataSource.setUrl(url(dataSourceName));
            dataSource.setUsername(username(dataSourceName));
            dataSource.setPassword(password(dataSourceName));
        }

        return wrapper(type, schemaName, dataSourceName, dataSource);
    }

    /**
     * 判断是否本地数据库或者是test数据库<br>
     * 如果不是返回RuntimeException
     */
    private static void checkDoesTestDB(DataSourceType type, String dataSourceName, String schemaName) {
        if (ConfigHelper.doesOnlyTestDatabase() == false) {
            return;
        }
        if (type.isMemoryDB()) {
            return;
        }

        if (url(dataSourceName).contains("127.0.0.1") || url(dataSourceName).toUpperCase().contains("LOCALHOST")) {
            return;
        }
        String temp = schemaName.toUpperCase();

        if (!temp.endsWith("TEST") && !temp.startsWith("TEST")) {
            throw new RuntimeException("only local db or test db will be allowed to connect,url:" + url(dataSourceName)
                    + ", schemas:" + schemaName);
        }
    }

    /**
     * 注册数据库驱动
     */
    private static void registerDriver(String dataSourceName) {
        try {
            if (registered.contains(driver(dataSourceName))) {
                return;
            }
            DriverManager.registerDriver((Driver) Class.forName(driver(dataSourceName)).newInstance());
            registered.add(driver(dataSourceName));
        } catch (Throwable e) {
            throw new RuntimeException("Cannot register SQL driver " + driver(dataSourceName));
        }
    }

    private static DataSourceType type(String dataSourceName) {
        String type = ConfigHelper.getDataSourceKey(dataSourceName, PROP_KEY_DATASOURCE_TYPE);
        return DataSourceType.databaseType(type);
    }

    private static String driver(String dataSourceName) {
        return ConfigHelper.getDataSourceKey(dataSourceName, PROP_KEY_DATASOURCE_DRIVER);
    }

    private static String url(String dataSourceName) {
        return ConfigHelper.getDataSourceKey(dataSourceName, PROP_KEY_DATASOURCE_URL);
    }


    private static String username(String dataSourceName) {
        return ConfigHelper.getDataSourceKey(dataSourceName, PROP_KEY_DATASOURCE_USERNAME);
    }

    private static String password(String dataSourceName) {
        return ConfigHelper.getDataSourceKey(dataSourceName, PROP_KEY_DATASOURCE_PASSWORD);

    }
}
