package org.test4j.module.spring.utility;

import org.springframework.beans.factory.BeanFactory;
import org.test4j.module.spring.SpringTestedContext;
import org.test4j.module.spring.util.ISpringHelper;

/**
 * 测试类中spring上下文
 * 
 * @author darui.wudr
 */
@SuppressWarnings("unchecked")
public class SpringHelperImpl implements ISpringHelper {

    public SpringHelperImpl() {
    }

    @Override
    public <T> T getBean(String beanname) {
        return (T) SpringModuleHelper.getBeanByName(beanname);
    }

    @Override
    public void invalidate() {
        SpringModuleHelper.invalidateApplicationContext();
    }

    public BeanFactory getBeanFactory() {
        BeanFactory factory = (BeanFactory) SpringTestedContext.getSpringBeanFactory();
        return factory;
    }
}
