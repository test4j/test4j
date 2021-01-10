package org.test4j.module.database.sql;

import lombok.ToString;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * test4j DataSource
 *
 * @author darui.wudr
 */
@ToString
public class Test4JDataSource implements DataSource {

    private final DataSource dataSource;

    private final String dataSourceName;

    Test4JDataSource(String beanName, DataSource dataSource) {
        this.dataSourceName = beanName;
        this.dataSource = dataSource;
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
}