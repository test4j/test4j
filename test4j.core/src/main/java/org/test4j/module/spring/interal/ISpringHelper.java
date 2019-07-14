package org.test4j.module.spring.interal;

import org.springframework.beans.factory.BeanFactory;
import org.test4j.module.spring.SpringModule;
import org.test4j.module.spring.interal.SpringModuleHelper;

/**
 * test4j测试中获取spring容器相关的接口
 *
 * @author darui.wudr 2013-1-15 上午9:43:10
 */
public interface ISpringHelper {

    /**
     * 如果spring容器有效，返回名称为beanname的spring bean
     *
     * @param beanName
     * @return
     */
    default <T> T getBean(String beanName) {
        return (T) SpringModuleHelper.getBeanByName(beanName);
    }

    /**
     * 返回当前spring容器
     *
     * @return
     */
    default BeanFactory getBeanFactory() {
        return (BeanFactory) SpringModule.getSpringContext().orElse(null);
    }
}
