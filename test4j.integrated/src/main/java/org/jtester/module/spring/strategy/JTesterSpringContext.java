package org.jtester.module.spring.strategy;

import org.jtester.module.core.TestContext;
import org.jtester.module.core.utility.MessageHelper;
import org.jtester.module.spring.strategy.register.RegisterDynamicBean;
import org.jtester.module.tracer.TracerHelper;
import org.jtester.module.tracer.spring.SpringBeanTracer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * {@link ClassPathXmlApplicationContext}的子类，运行使用@MockBean来替代spring中加载的bean值
 */
@SuppressWarnings({ "rawtypes" })
public class JTesterSpringContext extends ClassPathXmlApplicationContext {

    private boolean shared;

    public JTesterSpringContext(String[] configLocations, boolean refresh, boolean shared) throws BeansException {
        super(configLocations, false, null);
        this.shared = shared;
        if (refresh) {
            refresh();
        }
    }

    public JTesterSpringContext(String[] configLocations, boolean refresh) throws BeansException {
        this(configLocations, refresh, false);
    }

    public boolean isShared() {
        return shared;
    }

    /**
     * 设置是否共享spring
     * 
     * @param share
     */
    public void setShared(boolean share) {
        this.shared = true;
    }

    /**
     * 将BeanFactory按JTesterBeanFactory类型返回
     * 
     * @return
     */
    public final JTesterBeanFactory getJTesterBeanFactory() {
        ConfigurableListableBeanFactory beanFactory = super.getBeanFactory();
        return (JTesterBeanFactory) beanFactory;
    }

    @Override
    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) super.obtainFreshBeanFactory();
        Class testedClazz = this.getTestedClazzz();
        // 注册SpringBeanFrom的proxy bean
        SpringBeanFromFactory.registerSpringBeanFromField(beanFactory, testedClazz);

        MessageHelper.info("Refresh spring classpath application context, tested class:" + testedClazz.getName());
        this.dynamicRegisterBean(beanFactory, testedClazz);
        return beanFactory;
    }

    protected Class getTestedClazzz() {
        Class testedClazz = TestContext.currTestedClazz();
        return testedClazz;
    }

    /**
     * 动态注册spring bean
     * 
     * @param beanFactory
     * @param testedClazz
     */
    protected void dynamicRegisterBean(DefaultListableBeanFactory beanFactory, Class testedClazz) {
        // @AutoInject生效时：@SpringBeanByName 和 @SpringBeanByType bean注册
        RegisterDynamicBean.dynamicRegisterBeanDefinition(beanFactory, testedClazz);

        // 是否定义bean输入输出跟踪日志
        boolean tracerEnabled = TracerHelper.doesTracerEnabled();
        if (tracerEnabled) {
            SpringBeanTracer.addTracerBeanDefinition(beanFactory);
        }
    }

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        BeanFactory parent = getInternalParentBeanFactory();
        return new JTesterBeanFactory(parent);
    }
}
