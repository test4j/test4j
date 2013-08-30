package org.jtester.module.spring.utility;

import org.jtester.module.spring.SpringTestedContext;
import org.jtester.module.spring.util.ISpringHelper;
import org.springframework.beans.factory.BeanFactory;

/**
 * 测试类中spring上下文
 * 
 * @author darui.wudr
 */
@SuppressWarnings("unchecked")
public class SprinHelperImpl implements ISpringHelper {

    public SprinHelperImpl() {
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
