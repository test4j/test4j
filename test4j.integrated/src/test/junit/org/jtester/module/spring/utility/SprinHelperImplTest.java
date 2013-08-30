package org.jtester.module.spring.utility;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringContext;
import org.junit.Test;

@SpringContext("org/jtester/module/spring/testedbeans/xml/data-source.cglib.xml")
@AutoBeanInject
public class SprinHelperImplTest implements JTester {

    @SpringBeanByName(claz = SpringAfterInit.class)
    SpringAfterInit bean;

    @Test
    public void testInvalidate_SpringUnInvalidate() {
        String ret = bean.getProp();
        want.string(ret).isEqualTo("unset");

        SpringAfterInit.initProp = "invalid";
        spring.invalidate();
    }

    @Test
    public void testInvalidate_SpringINvalidate() {
        String ret = bean.getProp();
        want.string(ret).isEqualTo("invalid");
    }
}
