package org.test4j.module.database.sql;

import java.util.Optional;

public class Test4JSqlContext {
    private static ThreadLocal<SqlList> sqlContext = new ThreadLocal<>();
    private static ThreadLocal<Boolean> hasRecord = new ThreadLocal<>();
    /**
     * 是否在db操作
     */
    private static ThreadLocal<Boolean> IN_DB_OPERATOR = new ThreadLocal<>();

    public static void setRecordStatus(boolean status) {
        hasRecord.set(status);
    }

    public static void setDbOpStatus(boolean status) {
        IN_DB_OPERATOR.set(status);
    }

    public static void setNextRecordStatus() {
        hasRecord.set(false);
    }


    public static boolean needRecord() {
        return !isRecord() && !isTestOp();
    }

    public static SqlList getSqlContext() {
        return Optional.ofNullable(sqlContext.get())
                .orElseGet(() -> {
                    SqlList list = new SqlList();
                    sqlContext.set(list);
                    return list;
                });
    }

    public static void addSql(String sql, Object... parameters) {
        getSqlContext().add(new SqlContext(sql, parameters));
        setRecordStatus(true);
    }

    public static void clean() {
        getSqlContext().clear();
    }

    private static boolean isRecord() {
        return hasRecord.get() == null ? false : hasRecord.get();
    }

    private static boolean isTestOp() {
        return IN_DB_OPERATOR.get() == null ? false : IN_DB_OPERATOR.get();
    }
}