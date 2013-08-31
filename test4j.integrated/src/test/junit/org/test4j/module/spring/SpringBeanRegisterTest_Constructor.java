package org.test4j.module.spring;

import org.junit.Ignore;
import org.junit.Test;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

@Ignore
@SpringContext
@AutoBeanInject
public class SpringBeanRegisterTest_Constructor extends Test4J {

    @SpringBeanByName
    BeanClass bean;

    /**
     * 自动注册的Clazz没有默认的构造函数
     */
    @Test
    public void test_AutoInject_NoDefaultConstructor() {

    }

    public static class BeanClass {
        public BeanClass(String value) {

        }
    }
}
