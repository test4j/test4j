package org.test4j.module.spring.utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.framework.MockCglib2AopProxy;
import org.springframework.beans.factory.BeanFactory;
import org.test4j.module.Test4JException;
import org.test4j.module.core.TestContext;
import org.test4j.module.core.utility.MessageHelper;
import org.test4j.module.core.utility.ModulesManager;
import org.test4j.module.spring.SpringModule;
import org.test4j.module.spring.SpringTestedContext;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.strategy.ApplicationContextFactory;
import org.test4j.module.spring.strategy.Test4JBeanFactory;
import org.test4j.module.spring.strategy.Test4JSpringContext;
import org.test4j.module.spring.strategy.injector.SpringBeanInjector;
import org.test4j.tools.commons.AnnotationHelper;
import org.test4j.tools.commons.ClazzHelper;
import org.test4j.tools.commons.ConfigHelper;

@SuppressWarnings("rawtypes")
public class SpringModuleHelper {

    /**
     * 获得当前测试类spring容器中名称为beanname的spring bean
     * 
     * @param beanName
     * @return
     */
    public static Object getBeanByName(String beanname) {
        BeanFactory factory = SpringTestedContext.getSpringBeanFactory();
        if (factory == null) {
            throw new RuntimeException("can't find SpringApplicationContext for tested class:"
                    + TestContext.currTestedClazzName());
        } else {
            Object bean = factory.getBean(beanname);
            return bean;
        }
    }

    /**
     * 往测试对象中注入spring bean<br>
     * 支持注解<br>
     * o @SpringBeanByName <br>
     * o @SpringBeanByType <br>
     * o @Autowired <br>
     * o @Resource
     * 
     * @param injected
     */
    public static void setSpringBean(Object injected) {
        Test4JBeanFactory beanFactory = SpringTestedContext.getSpringBeanFactory();
        if (beanFactory != null) {
            SpringBeanInjector.injectSpringBeans(beanFactory, injected);
        }
    }

    /**
     * 强制重新加载spring 容器<br>
     * Forces the reloading of the application context the next time that it is
     * requested. If classes are given only contexts that are linked to those
     * classes will be reset. If no classes are given, all cached contexts will
     * be reset.
     * 
     * @param classes The classes for which to reset the contexts
     */
    public static void invalidateApplicationContext() {
        boolean springModuleEnabled = ModulesManager.isModuleEnabled(SpringModule.class);
        if (springModuleEnabled) {
            SpringModule module = ModulesManager.getModuleInstance(SpringModule.class);
            module.invalidateApplicationContext();
        }
    }

    private static final Map<Class, Test4JSpringContext> SHARED_SPRING_CONTEXT = new HashMap<Class, Test4JSpringContext>();

    /**
     * 初始化当前测试类用到的spring application context对象
     * 
     * @param testedObject
     * @param contextFactory
     * @return does initial spring context successfully
     */
    public static Test4JSpringContext initSpringContext(Class testClazz, ApplicationContextFactory contextFactory) {
        Test4JSpringContext springContext = SpringTestedContext.getSpringContext();
        if (springContext != null) {
            return springContext;
        }
        SpringContext annotation = AnnotationHelper.getClassLevelAnnotation(SpringContext.class, testClazz);
        Class declaredAnnotationClazz = AnnotationHelper.getClassWithAnnotation(SpringContext.class, testClazz);
        if (annotation == null) {
            return null;
        }

        boolean share = annotation.share();
        Test4JSpringContext context = null;
        if (share) {
            context = SHARED_SPRING_CONTEXT.get(declaredAnnotationClazz);
        }
        if (context == null) {
            context = newSpringContext(testClazz, contextFactory, annotation);
        }
        if (share) {
            SHARED_SPRING_CONTEXT.put(declaredAnnotationClazz, context);
        }
        SpringTestedContext.setSpringContext(springContext);
        return context;
    }

    /**
     * 创建新的spring容器
     * 
     * @param testClazz
     * @param contextFactory
     * @param locations
     * @param ignoreNoSuchBean
     * @return
     */
    private static Test4JSpringContext newSpringContext(Class testClazz, ApplicationContextFactory contextFactory,
                                                        SpringContext annotation) {
        long startTime = System.currentTimeMillis();
        String[] locations = annotation.value();
        boolean allowLazy = annotation.allowLazy();

        Test4JSpringContext springContext = contextFactory.createApplicationContext(Arrays.asList(locations), false,
                allowLazy);
        springContext.setShared(annotation.share());
        springContext.refresh();
        long duration = System.currentTimeMillis() - startTime;
        MessageHelper.warn(String.format("take %d ms to init spring context for test obejct[%s]", duration,
                testClazz.getName()));
        return springContext;
    }

    /**
     * 释放测试类的spring容器
     * 
     * @param springContext
     *            AbstractApplicationContext实例，这里定义为Object是方便其它模块脱离spring依赖
     */
    public static void closeSpringContext(Object springContext) {
        if (springContext == null) {
            return;
        }
        if (!(springContext instanceof Test4JSpringContext)) {
            String error = String.format("there must be something error, the type[%s] object isn't a spring context.",
                    springContext.getClass().getName());
            throw new RuntimeException(error);
        }
        Test4JSpringContext context = (Test4JSpringContext) springContext;
        if (context.isShared() == false) {
            context.destroy();
            MessageHelper.warn("close spring context for class:" + TestContext.currTestedClazzName());
        }
    }

    /**
     * 返回spring代理的目标对象
     * 
     * @param target
     * @return
     */
    public static Object getAdvisedObject(Object target) {
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

    /**
     * 清空spring AOP容器的Dump容器，防止多次启动spring容器导致的Cglib AOP内存泄漏
     */
    public static void resetDumpReference() {
        boolean isAvailable = ClazzHelper.isClassAvailable("org.aspectj.weaver.Dump");
        if (isAvailable == false) {
            return;
        }
        org.aspectj.weaver.Dump.reset();
    }

    static boolean USE_ENHANCER_CACHED = ConfigHelper.getBoolean("spring.enhancer.cache.used", true);

    /**
     * 全局性替换Cglib2AopProxy类的createEnhancer方法<br>
     * 将net.sf.cglib.proxy.Enhancer的useCache置为false<br>
     * 防止多次启动spring容器导致的Cglib AOP内存泄漏
     */
    public static void mockCglibAopProxy() {
        if (USE_ENHANCER_CACHED) {
            return;
        }
        boolean isAvailable = ClazzHelper.isClassAvailable("org.springframework.aop.framework.Cglib2AopProxy");
        if (!isAvailable) {
            return;
        }
        new MockCglib2AopProxy();
    }
}
