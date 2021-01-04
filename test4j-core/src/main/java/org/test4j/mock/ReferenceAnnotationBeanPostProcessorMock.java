package org.test4j.mock;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.test4j.tools.reflector.MethodAccessor;

/**
 * 替换 @Reference 注解 @Autowired注解
 *
 * @author darui.wu
 * @create 2019/11/27 4:21 下午
 */

public class ReferenceAnnotationBeanPostProcessorMock extends MockUp<ReferenceAnnotationBeanPostProcessor> {

    static boolean hasMocked = false;
    /**
     * 要mock的class文件默认存在
     */
    static boolean classNotFound = false;

    public ReferenceAnnotationBeanPostProcessorMock() {
        hasMocked = true;
    }

    @Mock
    public Object doGetInjectedBean(Invocation it, Reference reference, Object bean, String beanName, Class<?> injectedType,
                                    InjectionMetadata.InjectedElement injectedElement) throws Exception {
        ReferenceAnnotationBeanPostProcessor processor = it.getTarget();
        ConfigurableListableBeanFactory factory = MethodAccessor.invoke(processor, "getBeanFactory");
        Object injected = factory.getBean(injectedType);
        return injected;
    }
}