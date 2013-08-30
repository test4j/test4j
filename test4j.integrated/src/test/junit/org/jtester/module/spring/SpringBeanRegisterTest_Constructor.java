package org.jtester.module.spring;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
@SpringContext
@AutoBeanInject
public class SpringBeanRegisterTest_Constructor implements JTester {

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
