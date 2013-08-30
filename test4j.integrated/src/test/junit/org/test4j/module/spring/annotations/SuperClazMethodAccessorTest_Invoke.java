package org.test4j.module.spring.annotations;

import org.junit.Test;
import org.test4j.junit.Test4J;

@SpringContext("org/test4j/module/spring/reflector/reflector-invoke.xml")
public class SuperClazMethodAccessorTest_Invoke implements Test4J {
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
