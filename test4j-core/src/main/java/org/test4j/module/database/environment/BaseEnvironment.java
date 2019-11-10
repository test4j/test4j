package org.test4j.module.database.environment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.test4j.function.EConsumer;
import org.test4j.function.EFunction;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.environment.typesmap.AbstractTypeMap;
import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.module.database.utility.DataSourceType;
import org.test4j.tools.commons.ExceptionWrapper;

public abstract class BaseEnvironment implements DBEnvironment {
    protected final String dataSourceName;

    private final Map<String, TableMeta> metas = new HashMap<>();

    protected DataSourceType dataSourceType;

    private DataSource dataSource;

    protected AbstractTypeMap typeMap;

    protected BaseEnvironment(DataSourceType dataSourceType, String dataSourceName, String schema) {
        this.dataSourceName = dataSourceName;
        this.dataSourceType = dataSourceType;
        this.dataSource = DataSourceCreatorFactory.create(dataSourceName);
    }

    @Override
    public void commit() {
        this.doIt(connection -> {
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        });
    }

    @Override
    public void rollback() {
        this.doIt(connection -> {
            if (!connection.getAutoCommit()) {
                connection.rollback();
            }
        });
    }

    @Override
    public void execute(String sql, EConsumer<PreparedStatement> executor) {
        this.doIt(connection -> {
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                executor.accept(st);
            }
        });
    }

    @Override
    public void execute(EFunction<Connection, PreparedStatement> stFunction,
                        EConsumer<PreparedStatement> executor) {
        this.doIt(connection -> {
            try (PreparedStatement st = stFunction.apply(connection)) {
                executor.accept(st);
            }
        });
    }

    @Override
    public <R> R query(EFunction<Connection, PreparedStatement> stFunction,
                       EFunction<ResultSet, R> resultFunction) {
        return this.doIt(connection -> {
            try (PreparedStatement st = stFunction.apply(connection)) {
                ResultSet rs = st.executeQuery();
                return resultFunction.apply(rs);
            }
        });
    }

    @Override
    public <R> R query(String sql, EFunction<ResultSet, R> rsFunction) {
        return this.doIt(connection -> {
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                ResultSet rs = st.executeQuery();
                return rsFunction.apply(rs);
            }
        });
    }

    /**
     * 获得数据表的元信息
     *
     * @param table
     * @return
     * @throws Exception
     */
    @Override
    public TableMeta getTableMetaData(String table) {
        if (metas.get(table) != null) {
            return metas.get(table);
        }
        return this.query("select * from " + table + " where 1!=1",
                rs -> {
                    TableMeta meta = new TableMeta(table, rs.getMetaData(), this);
                    metas.put(table, meta);
                    return meta;
                }
        );
    }

    @Override
    public Object getDefaultValue(String javaType) {
        Object value = this.typeMap.getDefaultValue(javaType);
        return value;
    }

    @Override
    public Object toObjectValue(String input, String javaType) {
        try {
            Object value = this.typeMap.toObjectByType(input, javaType);
            return value;
        } catch (Exception e) {
            MessageHelper.info("convert input[" + input + "] to type[" + javaType + "] error, so return input value.\n"
                    + e.getMessage());
            return input;
        }
    }

    /**
     * {@inheritDoc} <br>
     * <br>
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object convertToSqlValue(Object value) {
        if (value instanceof Enum) {
            return ((Enum) value).name();
        }
        return value;
    }

    private <R> R doIt(EFunction<Connection, R> function) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.doGetConnection(dataSource);
            return function.apply(connection);
        } catch (Exception e) {
            throw ExceptionWrapper.getUndeclaredThrowableExceptionCaused(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    private void doIt(EConsumer<Connection> consumer) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.doGetConnection(dataSource);
            consumer.accept(connection);
        } catch (Exception e) {
            throw ExceptionWrapper.getUndeclaredThrowableExceptionCaused(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}
