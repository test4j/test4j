package org.jtester.module.tracer.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.jtester.module.tracer.TracerManager;

@SuppressWarnings("rawtypes")
public class ConnectionProxy implements InvocationHandler {
	private final Connection connection;
	private final ClassLoader cl;

	public ConnectionProxy(final Connection connection) {
		this.connection = connection;
		this.cl = connection.getClass().getClassLoader();
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = method.invoke(connection, args);
		String methodname = method.getName();

		if (CREATE_STATEMENT_METHODS.contains(methodname)) {
			boolean hasProxied = result instanceof IProxyMarker;
			boolean isStatment = result instanceof Statement;
			if (hasProxied || isStatment == false) {
				return result;
			}
			Class[] types = getStatementTypes((Statement) result);
			Object o = Proxy.newProxyInstance(cl, types, new StatementProxy((Statement) result));

			addJdbcTracerEvent(args, methodname);
			return o;
		}
		return result;
	}

	private final static Class[] Statement_Types = new Class[] { Statement.class, IProxyMarker.class };

	private final static Class[] PreparedStatement_Types = new Class[] { PreparedStatement.class, IProxyMarker.class };

	private final static Class[] CallableStatement_Types = new Class[] { CallableStatement.class, IProxyMarker.class };

	private final static Class[] getStatementTypes(Statement statment) {
		if (statment instanceof CallableStatement) {
			return CallableStatement_Types;
		} else if (statment instanceof PreparedStatement) {
			return PreparedStatement_Types;
		} else {
			return Statement_Types;
		}
	}

	private final static Class[] CONNECTION_TYPES = new Class[] { Connection.class, IProxyMarker.class };

	private final static Set<String> CREATE_STATEMENT_METHODS = new HashSet<String>() {
		private static final long serialVersionUID = 3331093299449024077L;

		{
			this.add("createStatement");
			this.add("prepareStatement");
			this.add("prepareCall");
		}
	};

	private static final void addJdbcTracerEvent(Object[] args, String methodname) {
		if (args == null || args.length < 1) {
			return;
		}
		Object sql = args[0];
		if (sql instanceof String) {
			TracerManager.traceJdbcStatement((String) sql);
		}
	}

	public static final Connection getConnectionProxy(Connection conn) {
		boolean hasProxied = conn instanceof IProxyMarker;
		if (hasProxied) {
			return conn;
		}
		ClassLoader cl = conn.getClass().getClassLoader();
		Object o = Proxy.newProxyInstance(cl, CONNECTION_TYPES, new ConnectionProxy(conn));
		return (Connection) o;
	}
}
