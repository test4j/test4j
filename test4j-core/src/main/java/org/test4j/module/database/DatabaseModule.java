package org.test4j.module.database;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.test4j.module.core.Module;
import org.test4j.module.core.internal.TestListener;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.mock.MybatisConfigurationMock;
import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.module.database.sql.Test4JDataSource;
import org.test4j.module.database.sql.Test4JSqlContext;
import org.test4j.module.spring.interal.SpringEnv;
import org.test4j.tools.commons.ConfigHelper;

public class DatabaseModule implements Module {

    @Override
    public void init() {
        MessageHelper.info("PlatformTransactionManager class init.");
        PlatformTransactionManager.class.getName();
    }

    @Override
    public void afterInit() {
    }

    /**
     * @return The {@link TestListener} associated with this module
     */
    @Override
    public TestListener getTestListener() {
        return new DatabaseTestListener();
    }

    /**
     * The {@link TestListener} for this module
     */
    protected class DatabaseTestListener extends TestListener {
        @Override
        public void beforeClass(Class testClazz) {
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
        public void beforeMethod(Object testObject, Method testMethod) {
            Test4JSqlContext.clean();
            new MybatisConfigurationMock();
        }

        /**
         * 移除测试方法的事务<br>
         * <br>
         * {@inheritDoc}
         */
        @Override
        public void afterMethod(Object testObject, Method testMethod, Throwable testThrowable) {
            DBEnvironmentFactory.closeDBEnvironment();
            Test4JSqlContext.clean();
        }

        @Override
        protected String getName() {
            return "DatabaseTestListener";
        }
    }
}
