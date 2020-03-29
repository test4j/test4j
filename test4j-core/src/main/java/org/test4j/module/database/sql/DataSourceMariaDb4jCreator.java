package org.test4j.module.database.sql;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.DateHelper;

import javax.sql.DataSource;

import static ch.vorburger.mariadb4j.DB.newEmbeddedDB;
import static ch.vorburger.mariadb4j.DBConfigurationBuilder.newBuilder;
import static org.test4j.module.core.utility.MessageHelper.warn;

/**
 * MariaDb4j数据源创建
 */
public class DataSourceMariaDb4jCreator {
    /**
     * MariaDb4j数据源创建
     *
     * @param dataSourceName
     * @return
     */
    public static Test4JDataSource createTest4JDataSource(String dataSourceName) {
        try {
            DataSource dataSource = createDb(dataSourceName);
            return new Test4JDataSource(dataSourceName, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * MariaDb4j数据源创建
     *
     * @param dataSourceName
     * @return
     */
    static BasicDataSource createDb(String dataSourceName) throws ManagedProcessException {
        String schema = DataSourceDefaultCreator.schema(dataSourceName);
        String username = DataSourceDefaultCreator.username(dataSourceName);
        String password = DataSourceDefaultCreator.password(dataSourceName);
        String driver = ConfigHelper.getString("dataSource.mariaDB4j.driver");
        warn("begin to start MariaDB4j: " + DateHelper.currDateTimeStr());
        int port = ConfigHelper.getInteger("dataSource.mariaDB4j.port",0);

        DBConfigurationBuilder config = newBuilder().setPort(port);
        DB mariaDB = newEmbeddedDB(config.build());
        mariaDB.start();
        String url = config.getURL(schema);
        warn("MariaDB4j driver[ " + driver + " ], url[ " + url + " ], username[ " + username + " ], password[ " + password + " ].");
        warn("end of start MariaDB4j:" + DateHelper.currDateTimeStr());
        BasicDataSource dataSource = new BasicDataSource();
        {
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
        }
        return dataSource;
    }
}
