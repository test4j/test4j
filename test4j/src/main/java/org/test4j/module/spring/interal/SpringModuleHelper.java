package org.test4j.module.spring.interal;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.test4j.exception.Test4JException;
import org.test4j.module.core.internal.Test4JContext;
import org.test4j.module.spring.annotations.BeforeSpringContext;
import org.test4j.tools.commons.AnnotationHelper;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.StringHelper;

import javax.sql.DataSource;


@SuppressWarnings("rawtypes")
public class SpringModuleHelper {
    public static final String TRANSACTION_MANAGER_CLASS = "org.springframework.transaction.PlatformTransactionManager";

    /**
     * key: 测试类， value：AbstractApplicationContext实例
     */
    private static Map<Class, WeakReference<ApplicationContext>> springBeanFactories = new HashMap<>();

    /**
     * 获取当前测试实例的spring容器
     *
     * @return
     */
    public static Optional<ApplicationContext> getSpringContext() {
        WeakReference<ApplicationContext> reference = springBeanFactories.get(Test4JContext.currTestedClazz());
        if (reference == null || reference.get() == null) {
            return Optional.empty();
        } else {
            return Optional.of(reference.get());
        }
    }

    /**
     * 设置当前测试实例的spring容器
     *
     * @param context
     */
    public static void setSpringContext(ApplicationContext context) {
        springBeanFactories.put(Test4JContext.currTestedClazz(), new WeakReference<>(context));
    }

    public static boolean existBean(String name) {
        return getSpringContext().map(c -> c.containsBean(name)).orElse(false);
    }

    /**
     * 获得当前测试类spring容器中名称为beanname的spring bean
     *
     * @param beanname
     * @return
     */
    public static <T> T getBeanByName(String beanname) {
        return (T) getSpringContext().map(c -> c.getBean(beanname)).orElse(null);
    }

    public static DataSource getDataSource(String bean) {
        if (existBean(bean)) {
            Object dataSource = getBeanByName(bean);
            return dataSource instanceof DataSource ? (DataSource) dataSource : null;
        } else {
            return null;
        }
    }


    /**
     * 用来在test4j初始化之前工作<br>
     * 比如spring加载前的mock工作等
     *
     * @param test 测试类实例
     */
    public static void invokeSpringInitMethod(Object test) {
        Method[] methods = test.getClass().getDeclaredMethods();
        Arrays.stream(methods)
                .filter(m -> m.getParameterCount() == 0)
                .filter(m -> m.getAnnotation(BeforeSpringContext.class) != null)
                .forEach(m -> invokeSpringInitMethod(test, m));
    }

    private static void invokeSpringInitMethod(Object test, Method m) {
        boolean accessible = m.isAccessible();
        try {
            m.setAccessible(true);
            m.invoke(test);
        } catch (Exception e) {
            String err = String.format("invoke @%s %s() error: %s.",
                    BeforeSpringContext.class.getSimpleName(),
                    m.getName(),
                    e.getMessage()
            );
            throw new RuntimeException(err, e);
        } finally {
            m.setAccessible(accessible);
        }
    }

    public static void injectSpringBeans(Object testedObject) {
        if (!SpringEnv.isSpringEnv()) {
            return;
        }
        AutowireCapableBeanFactory beanFactory = getSpringContext().get().getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(testedObject, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.initializeBean(testedObject, testedObject.getClass().getSimpleName());
    }

    public static Optional<PlatformTransactionManager> getSpringPlatformTransactionManager(Object testedObject) {
        if (!getSpringContext().isPresent()) {
            return Optional.empty();
        }
        ListableBeanFactory context = getSpringContext().get();
        Class<PlatformTransactionManager> transactionManagerClass = ClazzHelper.getClazz(TRANSACTION_MANAGER_CLASS);
        Map<String, PlatformTransactionManager> platformTransactionManagerMap = context.getBeansOfType(transactionManagerClass);
        if (platformTransactionManagerMap.size() == 0) {
            throw new Test4JException("can't find a bean of type " + TRANSACTION_MANAGER_CLASS);
        } else if (platformTransactionManagerMap.size() == 1) {
            return Optional.ofNullable(platformTransactionManagerMap.values().iterator().next());
        } else {
            Method testMethod = Test4JContext.currTestedMethod();
            String managerName = AnnotationHelper.getMethodOrClassLevelAnnotationProperty(
                    Transactional.class, "value", "", testMethod, testedObject.getClass()
            );
            if (StringHelper.isBlankOrNull(managerName)) {
                throw new Test4JException(
                        "Found more than one bean of type " + TRANSACTION_MANAGER_CLASS +
                                ", please use the @Transactional(\"transactionalName\") to select the correct one."
                );
            }
            if (!platformTransactionManagerMap.containsKey(managerName)) {
                throw new Test4JException("no bean of type " + TRANSACTION_MANAGER_CLASS
                        + " found in the spring ApplicaitonContext with the name " + managerName);
            }
            return Optional.ofNullable(platformTransactionManagerMap.get(managerName));
        }
    }

    /**
     * 返回spring代理的目标对象
     *
     * @param target
     * @return
     */
    public static Object getAdvisedObject(Object target) {
        if (!ClazzHelper.isClassAvailable("org.springframework.aop.framework.Advised")) {
            return target;
        }
        if (target instanceof org.springframework.aop.framework.Advised) {
            try {
                return ((org.springframework.aop.framework.Advised) target).getTargetSource().getTarget();
            } catch (Exception e) {
                throw new Test4JException(e);
            }
        } else {
            return target;
        }
    }
}
