package org.test4j.testng.jmockit.mockbug;

import mockit.Invocation;
import mockit.Mock;

import org.test4j.module.jmockit.mockbug.TestedMethodService;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.annotations.SpringInitMethod;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test
@SpringContext
@AutoBeanInject
public class MethodServiceTest_SpringInject extends Test4J {
    @SpringBeanByName
    TestedMethodService service;

    @SpringInitMethod
    protected void mockTestedMethodService() {
        new MockUp<TestedMethodService>() {
            @Mock
            public void $init(Invocation it) {
                reflector.setField(it.getInvokedInstance(), "name", "init mock");
            }
        };
    }

    public void testBeforeMethodMock() {
        String result = service.sayHello();
        System.out.println(result);
        want.string(result).isEqualTo("hello, init mock");

        String name = reflector.getField(service, "name");
        want.string(name).isEqualTo("init mock");
    }
}
