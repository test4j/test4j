package org.jtester.module.spring.util;

/**
 * jtester测试中获取spring容器相关的接口
 * 
 * @author darui.wudr 2013-1-15 上午9:43:10
 */
public interface ISpringHelper {

    /**
     * 如果spring容器有效，返回名称为beanname的spring bean
     * 
     * @param beanname
     * @return
     */
    <T> T getBean(String string);

    /**
     * 使原先的spring容器失效，重新初始化容器
     */
    void invalidate();
}
