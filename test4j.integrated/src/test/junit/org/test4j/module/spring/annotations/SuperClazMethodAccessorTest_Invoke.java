package org.test4j.module.spring.annotations;

import org.junit.Test;
import org.test4j.junit.JTester;
import org.test4j.module.spring.annotations.ExMyService;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext("org/jtester/module/spring/reflector/reflector-invoke.xml")
public class SuperClazMethodAccessorTest_Invoke implements JTester {
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
