package org.jtester.module.spring.annotations;

import org.jtester.junit.JTester;
import org.junit.Test;

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
