package org.test4j.module.database.sql;

import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import lombok.ToString;
import org.test4j.module.database.utility.DataSourceType;
import org.test4j.module.database.utility.DatabaseModuleHelper;
import org.test4j.module.spring.SpringModule;
import org.test4j.module.spring.interal.SpringEnv;

/**
 * test4j DataSource
 *
 * @author darui.wudr
 */
@ToString
public class Test4JDataSource implements DataSource {

    private final DataSource dataSource;

    private final DataSourceType type;
    private final String dataSourceName;
    private final String schemaName;

    private Test4JDataSource(DataSourceType type, String schemaName, String dataSourceName, DataSource dataSource) {
        this.type = type;
        this.dataSourceName = dataSourceName;
        this.schemaName = schemaName;
        this.dataSource = dataSource;
        DatabaseModuleHelper.runInitScripts(this, this.dataSourceName);
    }

    private Test4JDataSource(String beanName, DataSource dataSource) {
        this.type = null;
        this.dataSourceName = beanName;
        this.schemaName = null;
        this.dataSource = dataSource;
        DatabaseModuleHelper.runInitScripts(this, this.dataSourceName);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        return Test4JConnection.getConnectionProxy(conn);
    }

    @Override
    public Connection getConnection(String arg0, String arg1) throws SQLException {
        Connection conn = dataSource.getConnection(arg0, arg1);
        return Test4JConnection.getConnectionProxy(conn);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }


    @Override
    public void setLogWriter(PrintWriter arg0) throws SQLException {
        dataSource.setLogWriter(arg0);
    }

    @Override
    public void setLoginTimeout(int arg0) throws SQLException {
        dataSource.setLoginTimeout(arg0);
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSource.isWrapperFor(iface);
    }

    public static Test4JDataSource wrapper(DataSourceType type, String schemaName, String dataSourceName, DataSource dataSource) {
        return dataSource instanceof Test4JDataSource ? (Test4JDataSource) dataSource :
                new Test4JDataSource(type, schemaName, dataSourceName, dataSource);
    }


    public static Map<String, Test4JDataSource> EXIST_DATASOURCE = new HashMap<>();

    public static boolean isDataSource(String beanName) {
        return EXIST_DATASOURCE.containsKey(beanName);
    }

    public static DataSource create(String dataSourceName) {
        if (EXIST_DATASOURCE.containsKey(dataSourceName)) {
            return EXIST_DATASOURCE.get(dataSourceName);
        } else {
            Test4JDataSource dataSource = Test4JDataSourceHelper.createLocalDataSource(dataSourceName);
            EXIST_DATASOURCE.put(dataSourceName, dataSource);
            return dataSource;
        }
    }
}
