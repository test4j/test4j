package org.jtester.module.spring.utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import mockit.internal.annotations.MockClassSetup;

import org.jtester.module.JTesterException;
import org.jtester.module.core.TestContext;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.core.utility.ModulesManager;
import org.jtester.module.spring.SpringModule;
import org.jtester.module.spring.SpringTestedContext;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.strategy.ApplicationContextFactory;
import org.jtester.module.spring.strategy.JTesterSpringContext;
import org.jtester.tools.commons.AnnotationHelper;
import org.jtester.tools.commons.ClazzHelper;
import org.jtester.tools.commons.ConfigHelper;
import org.springframework.aop.framework.MockCglib2AopProxy;
import org.springframework.beans.factory.BeanFactory;

@SuppressWarnings("rawtypes")
public class SpringModuleHelper {

    /**
     * 获得当前测试类spring容器中名称为beanname的spring bean
     * 
     * @param beanName
     * @return
     */
    public static Object getBeanByName(String beanname) {
        BeanFactory factory = (BeanFactory) SpringTestedContext.getSpringBeanFactory();
        if (factory == null) {
            throw new RuntimeException("can't find SpringApplicationContext for tested class:"
                    + TestContext.currTestedClazzName());
        } else {
            Object bean = factory.getBean(beanname);
            return bean;
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

    private static final Map<Class, JTesterSpringContext> SHARED_SPRING_CONTEXT = new HashMap<Class, JTesterSpringContext>();

    /**
     * 初始化当前测试类用到的spring application context对象
     * 
     * @param testedObject
     * @param contextFactory
     * @return does initial spring context successfully
     */
    public static JTesterSpringContext initSpringContext(Class testClazz, ApplicationContextFactory contextFactory) {
        JTesterSpringContext springContext = SpringTestedContext.getSpringContext();
        if (springContext != null) {
            return springContext;
        }
        SpringContext annotation = AnnotationHelper.getClassLevelAnnotation(SpringContext.class, testClazz);
        Class declaredAnnotationClazz = AnnotationHelper.getClassWithAnnotation(SpringContext.class, testClazz);
        if (annotation == null) {
            return null;
        }

        boolean share = annotation.share();
        JTesterSpringContext context = null;
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
    private static JTesterSpringContext newSpringContext(Class testClazz, ApplicationContextFactory contextFactory,
                                                         SpringContext annotation) {
        long startTime = System.currentTimeMillis();
        String[] locations = annotation.value();
        boolean allowLazy = annotation.allowLazy();

        JTesterSpringContext springContext = contextFactory.createApplicationContext(Arrays.asList(locations), false,
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
        if (!(springContext instanceof JTesterSpringContext)) {
            String error = String.format("there must be something error, the type[%s] object isn't a spring context.",
                    springContext.getClass().getName());
            throw new RuntimeException(error);
        }
        JTesterSpringContext context = (JTesterSpringContext) springContext;
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
                throw new JTesterException(e);
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
        new MockClassSetup(MockCglib2AopProxy.class).setUpStartupMock();
    }
}
