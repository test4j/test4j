package org.test4j.module.spring;

import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.test4j.module.core.Module;
import org.test4j.module.core.internal.Test4JContext;
import org.test4j.module.core.internal.TestListener;
import org.test4j.module.spring.interal.SpringEnv;
import org.test4j.module.spring.interal.SpringModuleHelper;
import org.test4j.module.spring.mock.AbstractBeanFactoryMock;


@SuppressWarnings("rawtypes")
public class SpringModule implements Module {

    /**
     * 用来在test4j初始化之前工作<br>
     * 比如spring加载前的mock工作等
     *
     * @param test 测试类实例
     */
    public static void invokeSpringInitMethod(Object test) {
        SpringModuleHelper.invokeSpringInitMethod(test);
    }

    /**
     * 获取当前测试实例的spring application context实例
     *
     * @return
     */
    public static Optional<ApplicationContext> getSpringContext() {
        return SpringModuleHelper.getSpringContext();
    }

    /**
     * 设置测试实例下的spring容器
     *
     * @param beanFactory
     */
    public static void setSpringContext(ApplicationContext beanFactory) {
        SpringModuleHelper.setSpringContext(beanFactory);
    }

    /**
     * 返回spring事务
     *
     * @return
     */
    public static Optional<PlatformTransactionManager> getTransactionManager() {
        return SpringModuleHelper.getSpringPlatformTransactionManager(Test4JContext.currTestedObject());
    }

    public static void injectSpringBeans(Object testedObject) {
        if (SpringEnv.isSpringEnv() && getSpringContext().isPresent()) {
            SpringModuleHelper.injectSpringBeans(testedObject);
        }
    }


    /**
     * 根据配置初始化ApplicationContextFactory <br>
     * <br>
     * {@inheritDoc}
     */
    @Override
    public void init() {
    }

    @Override
    public void afterInit() {
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
            new AbstractBeanFactoryMock();
        }

        @Override
        protected String getName() {
            return "SpringTestListener";
        }
    }
}
