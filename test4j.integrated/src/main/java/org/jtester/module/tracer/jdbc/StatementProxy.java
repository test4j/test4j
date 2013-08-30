package org.jtester.module.tracer.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.jtester.module.tracer.TracerManager;

public class StatementProxy implements InvocationHandler {
	private final Statement statement;

	public StatementProxy(final Statement statement) {
		this.statement = statement;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = method.invoke(this.statement, args);

		String methodname = method.getName();
		boolean hasArgs = args != null && args.length > 0;
		if (SQL_EXECUTE_METHODS.contains(methodname) && hasArgs) {
			Object sql = args[0];
			if (sql instanceof String) {
				TracerManager.traceJdbcStatement((String) sql);
			}
		}

		return result;
	}

	private final static Set<String> SQL_EXECUTE_METHODS = new HashSet<String>() {
		private static final long serialVersionUID = 3331093299449024077L;

		{
			this.add("executeQuery");
			this.add("execute");
			this.add("executeUpdate");
		}
	};
}
