package org.test4j.module.spring.utility;

import org.junit.Test;
import org.test4j.junit.JTester;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.utility.SpringAfterInit;

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
