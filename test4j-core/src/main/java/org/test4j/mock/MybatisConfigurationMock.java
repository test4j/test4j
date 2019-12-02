package org.test4j.mock;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.test4j.module.database.sql.Test4JSqlContext;
import org.test4j.tools.commons.StringHelper;

import static java.util.stream.Collectors.toList;

/**
 * @author darui.wu
 */
public class MybatisConfigurationMock extends MockUp<Configuration> {
    static boolean hasMock = false;

    public MybatisConfigurationMock() {
        hasMock = true;
    }

    @Mock
    public StatementHandler newStatementHandler(Invocation it, Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        StatementHandler statementHandler = it.proceed(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
        BoundSql sql = boundSql;
        if (sql == null) {
            sql = mappedStatement.getSqlSource().getBoundSql(parameterObject);
        }
        if (sql != null) {
            TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
            Object[] parameters = this.getParameters(it.getInvokedInstance(), sql, typeHandlerRegistry);
            Test4JSqlContext.addSql(StringHelper.removeBreakingWhiteSpace(sql.getSql()), parameters);
        }
        return statementHandler;
    }

    private Object[] getParameters(Configuration configuration, BoundSql boundSql, TypeHandlerRegistry typeHandlerRegistry) {
        if (boundSql.getParameterMappings() == null) {
            return null;
        } else {
            return boundSql.getParameterMappings().stream()
                    .filter(this::isInParameter)
                    .map(parameterMapping -> parseParameterValue(configuration, typeHandlerRegistry, boundSql, parameterMapping))
                    .collect(toList())
                    .toArray();
        }
    }


    private boolean isInParameter(ParameterMapping parameterMapping) {
        return parameterMapping.getMode() != ParameterMode.OUT;
    }

    /**
     * 解析参数值
     *
     * @param configuration
     * @param typeHandlerRegistry
     * @param boundSql
     * @param parameterMapping
     * @return
     */
    private Object parseParameterValue(Configuration configuration, TypeHandlerRegistry typeHandlerRegistry, BoundSql boundSql, ParameterMapping parameterMapping) {
        Object parameterObject = boundSql.getParameterObject();
        String propertyName = parameterMapping.getProperty();
        if (boundSql.hasAdditionalParameter(propertyName)) {
            return boundSql.getAdditionalParameter(propertyName);
        } else if (parameterObject == null) {
            return null;
        } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            return parameterObject;
        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            return metaObject.getValue(propertyName);
        }
    }
}
