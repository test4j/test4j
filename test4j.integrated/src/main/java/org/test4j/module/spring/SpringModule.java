package org.test4j.module.spring;

import java.lang.reflect.Method;

import org.test4j.module.core.Module;
import org.test4j.module.core.TestContext;
import org.test4j.module.core.TestListener;
import org.test4j.module.core.utility.ModulesManager;
import org.test4j.module.database.DatabaseModule;
import org.test4j.module.database.environment.DBEnvironmentFactory;
import org.test4j.module.database.transaction.SpringTransactionManagementConfiguration;
import org.test4j.module.spring.strategy.ApplicationContextFactory;
import org.test4j.module.spring.strategy.Test4JSpringContext;
import org.test4j.module.spring.utility.SpringInitInvoker;
import org.test4j.module.spring.utility.SpringModuleHelper;
import org.test4j.tools.commons.ConfigHelper;

@SuppressWarnings("rawtypes")
public class SpringModule implements Module {
    private ApplicationContextFactory contextFactory;

    /**
     * 根据配置初始化ApplicationContextFactory <br>
     * <br>
     * {@inheritDoc}
     */
    @Override
    public void init() {
        contextFactory = ConfigHelper.getInstance(SPRING_APPLICATION_CONTEXT_FACTORY_CLASS_NAME);
        //SpringModuleHelper.mockCglibAopProxy();
    }

    @Override
    public void afterInit() {
        boolean isDatabaseModuleEnabled = ModulesManager.isModuleEnabled(DatabaseModule.class);
        if (isDatabaseModuleEnabled) {
            DBEnvironmentFactory.getDefaultDBEnvironment().registerTransactionManagementConfiguration(
                    new SpringTransactionManagementConfiguration());
        }
    }

    /**
     * 强制让SpringApplicationContext失效，重新初始化
     * 
     * @param testedObject
     */
    public void invalidateApplicationContext() {
        Class testClazz = TestContext.currTestedClazz();
        SpringTestedContext.removeSpringContext();
        Test4JSpringContext springContext = SpringModuleHelper.initSpringContext(testClazz, this.contextFactory);
        SpringTestedContext.setSpringContext(springContext);
    }

    @Override
    public TestListener getTestListener() {
        return new SpringTestListener();
    }

    /**
     * The {@link TestListener} for this module
     */
    protected class SpringTestListener extends TestListener {
        @Override
        public void beforeClass(Class testClazz) {
            SpringModuleHelper.resetDumpReference();

            SpringInitInvoker.invokeSpringInitMethod(TestContext.currTestedObject());

            Test4JSpringContext springContext = SpringModuleHelper.initSpringContext(testClazz, contextFactory);
            SpringTestedContext.setSpringContext(springContext);
        }

        /**
         * 重新注入spring bean,避免字段的值受上个测试的影响<br>
         * <br>
         * {@inheritDoc}
         */
        @Override
        public void beforeMethod(Object testObject, Method testMethod) {
            SpringModuleHelper.setSpringBean(testObject);
        }

        @Override
        public void afterClass(Object testedObject) {
            SpringTestedContext.removeSpringContext();
        }

        @Override
        protected String getName() {
            return "SpringTestListener";
        }
    }
}
