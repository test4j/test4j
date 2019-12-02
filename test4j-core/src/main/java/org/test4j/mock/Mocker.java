package org.test4j.mock;

import mockit.internal.startup.Startup;

/**
 * 触发test4j内置的mocker开关
 *
 * @author darui.wu
 * @create 2019/11/27 4:08 下午
 */
public class Mocker {
    /**
     * 处理spring获取数据源，替换为Test4J的数据源
     */
    public static void mockSpringDataSource() {
        if (SpringMock.hasMock) {
            return;
        }
        Startup.initializing = true;
        new SpringMock();
        Startup.initializing = false;
    }

    /**
     * 对 org.apache.ibatis.session.Configuration 进行mock处理<br>
     * 用于在测试中收集mybatis执行的语句
     */
    public static void mockMybatisConfiguration() {
        if (MybatisConfigurationMock.hasMock) {
            return;
        }
        Startup.initializing = true;
        new MybatisConfigurationMock();
        Startup.initializing = false;
    }

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
        if (ReferenceAnnotationBeanPostProcessorMock.hasMocked) {
            return;
        }
        Startup.initializing = true;
        new ReferenceAnnotationBeanPostProcessorMock();
        Startup.initializing = false;
    }

//    public void demo(){
//        mockDubboReference();
//    }
}