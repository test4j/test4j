package org.jtester.testng.tracer.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import mockit.Mock;

import org.jtester.hamcrest.matcher.string.StringMode;
import org.jtester.module.database.environment.JTesterDataSource;
import org.jtester.module.database.utility.DataSourceType;
import org.jtester.module.tracer.TracerHelper;
import org.jtester.module.tracer.TracerLogger;
import org.jtester.module.tracer.TracerManager;
import org.jtester.module.tracer.TxtFileTracerLogger;
import org.jtester.module.tracer.jdbc.IProxyMarker;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ConfigHelper;
import org.jtester.tools.commons.ResourceHelper;
import org.testng.annotations.Test;

public class ConnectionProxyTest extends JTester {
	@Test(groups = "jtester")
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

		JTesterDataSource proxy = new JTesterDataSource(type, driver, url, "jtester", username, password);

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
