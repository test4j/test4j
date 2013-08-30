package org.jtester.module.spring.annotations;

import java.util.HashMap;

import org.jtester.fortest.reflector.MyServiceImpl.MyTestException;
import org.jtester.junit.JTester;
import org.junit.Test;

@SpringContext("org/jtester/module/spring/reflector/reflector-beans.xml")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SuperClazMethodAccessorTest implements JTester {
    @SpringBeanByName
    private ExMyService myService;

    @Test
    public void protectedInvoked() {
        String ret = myService.protectedInvoked();
        want.string(ret).isEqualTo("protectedInvoked");
    }

    @Test
    public void privateInvoked() throws Throwable {
        String ret = myService.privateInvoked("test");
        want.string(ret).isEqualTo("privateInvoked:test");
    }

    @Test
    public void testPrimitivePara() {
        int ret = myService.primitivePara(2, true);
        want.number(ret).isEqualTo(4);
    }

    @Test
    public void mapPara() {
        HashMap map = new HashMap();
        map.put(1, "test");
        int ret = myService.mapPara(map);
        want.number(ret).isEqualTo(1);
    }

    @Test(expected = MyTestException.class)
    public void invokeException() {
        myService.invokeException();
    }

    @Test
    public void reflectSetField() {
        reflector.setField(myService, "privateStr", "test");
        want.object(myService).propertyEq("privateStr", "test");

        Object o = reflector.getField(myService, "privateStr");
        want.object(o).isEqualTo("test");
    }
}
