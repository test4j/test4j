package org.test4j.spec.spring;

import java.util.Map;

import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.strategy.Test4JSpringContext;
import org.test4j.module.spring.utility.SpringModuleHelper;
import org.test4j.testng.JSpec;
import org.testng.annotations.BeforeMethod;

@SuppressWarnings("rawtypes")
@SpringContext(value = { "spring/spring-demo.xml" }, share = true)
public abstract class SpringBaseDemo extends JSpec {
    static Map<Class, Test4JSpringContext> SHARED_SPRING_CONTEXT = reflector.getStaticField(SpringModuleHelper.class,
                                                                          "SHARED_SPRING_CONTEXT");

    @BeforeMethod
    public void checkSpringContext() {
        want.map(SHARED_SPRING_CONTEXT).sizeEq(1).hasKeys(SpringBaseDemo.class);
    }
}
