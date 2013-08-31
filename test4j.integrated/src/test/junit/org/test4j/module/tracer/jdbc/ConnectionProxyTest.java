package org.test4j.module.tracer.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import mockit.Mock;

import org.junit.Test;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit.Test4J;
import org.test4j.module.database.environment.Test4JDataSource;
import org.test4j.module.database.utility.DataSourceType;
import org.test4j.module.tracer.TracerHelper;
import org.test4j.module.tracer.TracerLogger;
import org.test4j.module.tracer.TracerManager;
import org.test4j.module.tracer.TxtFileTracerLogger;
import org.test4j.tools.commons.ConfigHelper;
import org.test4j.tools.commons.ResourceHelper;

public class ConnectionProxyTest extends Test4J {
    @Test
    public void minitorSql() throws Exception {
        new MockUp<TracerHelper>() {
            @Mock
            public boolean doesTracerEnabled() {
                return true;
            }
        };
        new MockUp<TracerLogger>() {
            @Mock
            public String getFile(String method, String surfix) {
                return "ConnectionProxyTest.minitorSql.txt";
            }

            @Mock
            public TracerLogger instance(boolean logInTxt) {
                return new TxtFileTracerLogger();
            }
        };
        TracerManager.startTracer();
        TracerManager.continueJDBC();
        DataSourceType type = DataSourceType.databaseType("mysql");
        String username = ConfigHelper.databaseUserName();
        String driver = ConfigHelper.databaseDriver();
        String url = ConfigHelper.databaseUrl();
        String password = ConfigHelper.databasePassword();

        Test4JDataSource proxy = new Test4JDataSource(type, driver, url, "test4j", username, password);

        Connection conn = proxy.getConnection();
        want.object(conn).clazIs(Connection.class).clazIs(IProxyMarker.class);
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select * from tdd_user");
        want.object(rs).notNull();

        TracerManager.endTracer();
        String traces = ResourceHelper.readFromFile("file://" + System.getProperty("user.dir")
                + "/target/tracer/ConnectionProxyTest.minitorSql.txt");
        want.string(traces).isEqualTo("#START_SQL\nselect * from tdd_user\n#END_SQL", StringMode.SameAsSpace);
    }
}
