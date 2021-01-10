package org.test4j.module.database;

import org.test4j.integration.spring.SpringEnv;
import org.test4j.mock.Mocker;
import org.test4j.module.ConfigHelper;
import org.test4j.module.core.Module;
import org.test4j.module.core.internal.ModuleListener;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.module.database.sql.Test4JSqlContext;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author darui.wu
 */
public class DatabaseModule implements Module {

    @Override
    public void init() {
        Mocker.mockSpringDataSource();
        Mocker.mockMybatisConfiguration();
    }

    @Override
    public void afterInit() {
    }

    /**
     * @return The {@link ModuleListener} associated with this module
     */
    @Override
    public ModuleListener getTestListener() {
        return new DatabaseModuleListener();
    }

    /**
     * The {@link ModuleListener} for this module
     */
    protected class DatabaseModuleListener extends ModuleListener {
        @Override
        public void beforeAll(Class testClazz) {
            if (!SpringEnv.isSpringEnv()) {
                return;
            }
            List<String> dataSources = ConfigHelper.getDataSourceList();
            for (String dataSourceName : dataSources) {
                DataSourceCreatorFactory.create(dataSourceName);
            }
        }

        /**
         * 初始化测试方法的事务<br>
         * <br>
         * {@inheritDoc}
         */
        @Override
        public void beforeExecute(Object testObject, Method testMethod) {
            Test4JSqlContext.clean();
        }

        /**
         * 移除测试方法的事务<br>
         * <br>
         * {@inheritDoc}
         */
        @Override
        public void afterExecute(Object testObject, Method testMethod, Throwable testThrowable) {
            DBEnvironmentFactory.closeDBEnvironment();
            Test4JSqlContext.clean();
        }

        @Override
        protected String getName() {
            return "DatabaseTestListener";
        }
    }
}