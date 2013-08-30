package org.jtester.spec.spring;

import java.util.Map;

import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.strategy.JTesterSpringContext;
import org.jtester.module.spring.utility.SpringModuleHelper;
import org.jtester.testng.JSpec;
import org.testng.annotations.BeforeMethod;

@SuppressWarnings("rawtypes")
@SpringContext(value = { "spring/spring-demo.xml" }, share = true)
public abstract class SpringBaseDemo extends JSpec {
    static Map<Class, JTesterSpringContext> SHARED_SPRING_CONTEXT = reflector.getStaticField(SpringModuleHelper.class,
                                                                          "SHARED_SPRING_CONTEXT");

    @BeforeMethod
    public void checkSpringContext() {
        want.map(SHARED_SPRING_CONTEXT).sizeEq(1).hasKeys(SpringBaseDemo.class);
    }
}
