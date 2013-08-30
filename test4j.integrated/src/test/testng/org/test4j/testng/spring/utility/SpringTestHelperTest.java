package org.test4j.testng.spring.utility;

import org.test4j.module.database.IDatabase;
import org.test4j.module.spring.ISpring;
import org.test4j.module.spring.annotations.AutoBeanInject;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.module.spring.utility.SpringAfterInit;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@Test(groups = { "test4j" })
@SpringContext("org/test4j/module/spring/testedbeans/xml/data-source.cglib.xml")
@AutoBeanInject
public class SpringTestHelperTest extends Test4J implements IDatabase, ISpring {

    @SpringBeanByName(claz = SpringAfterInit.class)
    SpringAfterInit bean;

    @Test
    public void testInvalidate_SpringUnInvalidate() {
        String ret = bean.getProp();
        want.string(ret).isEqualTo("unset");

        SpringAfterInit.initProp = "invalid";
        spring.invalidate();
    }

    @Test(dependsOnMethods = "testInvalidate_SpringUnInvalidate")
    public void testInvalidate_SpringINvalidate() {
        String ret = bean.getProp();
        want.string(ret).isEqualTo("invalid");
    }
}
