package org.test4j.module.database.sql;

import org.test4j.module.database.sql.db.H2Statement;
import org.test4j.tools.reflector.Reflector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Test4JStatement implements InvocationHandler {
    private static final String Execute_Method = "execute";

    private static final String Get_Prepared_Sql_Method = "getPreparedSql";

    private static final String Get_Delegate_Method = "getDelegate";

    private static Map<String, Method> Statement_Methods = new HashMap<>();

    private final Statement statement;

    public Test4JStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(this.statement, args);
        String methodName = method.getName();

        if (methodName.startsWith(Execute_Method)) {
            this.recordSql(args);
        }
        return result;
    }

    private void recordSql(Object[] args) {
        if (Test4JSqlContext.needRecord()) {
            if (hasArgument(args)) {
                Test4JSqlContext.addSql(String.valueOf(args[0]), Arrays.copyOfRange(args, 1, args.length));
            } else {
                Statement _stmt = this.getDelegate(statement);
                addPreparedStatementSql(_stmt);
            }
        }
        Test4JSqlContext.setNextRecordStatus();
    }

    private void addPreparedStatementSql(Statement statement) {
        if (!(statement instanceof PreparedStatement)) {
            return;
        }
        if (H2Statement.isH2PreparedStatement(statement)) {
            H2Statement.addSql(statement);
        } else {
            String sql = this.getPreparedSql(statement);
            Test4JSqlContext.addSql(sql);
        }
    }

    private String getPreparedSql(Statement statement) {
        try {
            Method mehtod = this.getStatementMethod(statement, Get_Prepared_Sql_Method);
            return mehtod == null ? null : (String) mehtod.invoke(statement);
        } catch (Exception e) {
            return null;
        }
    }

    private Statement getDelegate(Statement statement) {
        Method method = this.getStatementMethod(statement, Get_Delegate_Method);
        Statement _stmt = statement;
        while (method != null) {
            try {
                _stmt = (Statement) method.invoke(_stmt);
                method = this.getStatementMethod(_stmt, Get_Delegate_Method);
            } catch (Exception e) {
                method = null;
            }
        }
        return _stmt;
    }

    private Method getStatementMethod(Statement statement, String methodName) {
        Class klass = statement.getClass();
        String key = klass.getName() + "#" + methodName;
        if (Statement_Methods.containsKey(key)) {
            return Statement_Methods.get(key);
        } else {
            try {
                Method method = Reflector.getMethod(klass, methodName);
                Statement_Methods.put(key, method);
                return method;
            } catch (Exception e) {
                Statement_Methods.put(key, null);
                return null;
            }
        }
    }


    private boolean hasArgument(Object[] args) {
        return args != null && args.length > 0;
    }
}