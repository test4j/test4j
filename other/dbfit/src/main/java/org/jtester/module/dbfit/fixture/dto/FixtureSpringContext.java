package org.test4j.module.dbfit.fixture.dto;

import org.test4j.module.spring.remote.RemoteInvokerRegister;
import org.test4j.module.spring.strategy.Test4JSpringContext;
import org.test4j.module.spring.strategy.register.RegisterDynamicBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import fit.Fixture;

/**
 * @author darui.wudr
 */
public class FixtureSpringContext extends Test4JSpringContext {
    final Class<? extends Fixture> fixtureClazz;

    public FixtureSpringContext(String[] configLocations, final Class<? extends Fixture> fixtureClazz, boolean shared)
            throws BeansException {
        super(configLocations, false, false);
        this.fixtureClazz = fixtureClazz;
        refresh();
    }

    @Override
    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) super.obtainFreshBeanFactory();

        // Fixture暂不能mock任何东西
        // MockBeanRegister.registerAllMockBean(fixtureClazz);

        RegisterDynamicBean.dynamicRegisterBeanDefinition(beanFactory, fixtureClazz);
        // 增加httpInovoke的配置
        RemoteInvokerRegister.registerSpringBeanRemoteOnClient(beanFactory, fixtureClazz);

        return beanFactory;
    }
}
