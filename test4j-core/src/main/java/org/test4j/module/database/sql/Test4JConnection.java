package org.test4j.module.database.sql;

import org.test4j.module.database.IProxyMarker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class Test4JConnection implements InvocationHandler {
    private final static Class[] Statement_Types = new Class[]{Statement.class, IProxyMarker.class};
    private final static Class[] PreparedStatement_Types = new Class[]{PreparedStatement.class, IProxyMarker.class};
    private final static Class[] CallableStatement_Types = new Class[]{CallableStatement.class, IProxyMarker.class};
    private final static Class[] Connection_Types = new Class[]{Connection.class, IProxyMarker.class};

    private final static Set<String> Create_Statement_Methods = new HashSet<>();

    static {
        Create_Statement_Methods.add("createStatement");
        Create_Statement_Methods.add("prepareStatement");
        Create_Statement_Methods.add("prepareCall");
    }

    private final Connection connection;

    public Test4JConnection(Connection connection) {
        this.connection = connection;
    }

    public static Connection getConnectionProxy(Connection conn) {
        if (conn instanceof IProxyMarker) {
            return conn;
        } else {
            return (Connection) Proxy.newProxyInstance(
                    conn.getClass().getClassLoader(),
                    Connection_Types,
                    new Test4JConnection(conn)
            );
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(connection, args);
        return isProxyOrNotStatement(method, result) ? result : this.createProxyStatement((Statement) result);
    }

    private Object createProxyStatement(Statement result) {
        return Proxy.newProxyInstance(
                this.connection.getClass().getClassLoader(),
                this.getStatementProxyTypes(result),
                new Test4JStatement(result)
        );
    }

    private boolean isProxyOrNotStatement(Method method, Object result) {
        if (!Create_Statement_Methods.contains(method.getName())) {
            return true;
        } else if (result instanceof IProxyMarker) {
            return true;
        } else {
            return !(result instanceof Statement);
        }
    }

    private Class[] getStatementProxyTypes(Statement statement) {
        if (statement instanceof CallableStatement) {
            return CallableStatement_Types;
        } else if (statement instanceof PreparedStatement) {
            return PreparedStatement_Types;
        } else {
            return Statement_Types;
        }
    }
}