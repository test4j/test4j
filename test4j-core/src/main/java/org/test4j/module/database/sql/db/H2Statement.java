package org.test4j.module.database.sql.db;

import org.h2.command.Command;
import org.h2.jdbc.JdbcPreparedStatement;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.json.JSON;
import org.test4j.module.database.sql.Test4JSqlContext;
import org.test4j.tools.reflector.FieldAccessor;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class H2Statement {
    private final static String H2_Statement_Class = "org.h2.jdbc.JdbcPreparedStatement";

    public static boolean isH2PreparedStatement(Statement statement) {
        boolean available = ClazzHelper.isClassAvailable(H2_Statement_Class);
        if (!available) {
            return false;
        }
        Class klass = ClazzHelper.getClazz(H2_Statement_Class);
        return statement.getClass().isAssignableFrom(klass);
    }

    public static void addSql(Statement statement) {
        JdbcPreparedStatement h2statment = (JdbcPreparedStatement) statement;
        Command command = (Command) FieldAccessor.getValue(h2statment, "command");
        String sql = (String) FieldAccessor.getValue(command, "sql");
        String str = command.toString();
        if (str.contains(sql)) {
            int index = str.indexOf(sql);
            try {
                String parameters = str.substring(index + sql.length());
                Map map = JSON.toObject(parameters, HashMap.class);
                List list = new ArrayList<>();
                for (int loop = 0; loop < map.size(); loop++) {
                    list.add(map.get(index));
                }
                Test4JSqlContext.addSql(sql, list.toArray());
            } catch (Exception e) {
                Test4JSqlContext.addSql(sql);
            }
        } else {
            Test4JSqlContext.addSql(sql);
        }
    }
}
