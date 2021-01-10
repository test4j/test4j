package org.test4j.mock;

import org.test4j.mock.startup.Startup;

/**
 * 触发test4j内置的mocker开关
 *
 * @author darui.wu
 * @create 2019/11/27 4:08 下午
 */
public class Mocker {


    /**
     * 对dubbo的 @Reference引用进行替换处理
     * <pre><![CDATA[
     *  @Bean
     *  public ReferenceAnnotationBeanPostProcessor newReferenceAnnotationBeanPostProcessor() {
     *      Mocker.mockDubboReference();
     *      return new ReferenceAnnotationBeanPostProcessor();
     *  }
     * ]]></pre>
     */
    public static void mockDubboReference() {
        if (ReferenceAnnotationBeanPostProcessorMock.hasMocked || ReferenceAnnotationBeanPostProcessorMock.classNotFound) {
            return;
        }
        try {
            Class.forName("org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor");
        } catch (ClassNotFoundException e) {
            ReferenceAnnotationBeanPostProcessorMock.classNotFound = true;
        }
        Startup.initializing = true;
        new ReferenceAnnotationBeanPostProcessorMock();
        Startup.initializing = false;
    }
}