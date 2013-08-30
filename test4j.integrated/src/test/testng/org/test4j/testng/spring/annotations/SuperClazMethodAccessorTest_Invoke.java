package org.test4j.testng.spring.annotations;

import org.test4j.module.spring.annotations.ExMyService;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext("org/test4j/module/spring/reflector/reflector-invoke.xml")
@Test(groups = "test4j")
public class SuperClazMethodAccessorTest_Invoke extends Test4J {
    @SpringBeanByName
    private ExMyService myService;

    @Test
    public void privateInvoked() throws Throwable {
        String ret = myService.privateInvoked("test");
        want.string(ret).isEqualTo("privateInvoked:test");
    }

    @Test
    public void privateInvoked_nullPara() throws Throwable {
        String ret = myService.privateInvoked(null);
        want.string(ret).isEqualTo("privateInvoked:null");
    }
}
