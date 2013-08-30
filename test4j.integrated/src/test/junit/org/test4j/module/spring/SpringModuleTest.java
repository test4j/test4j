package org.test4j.module.spring;

import org.junit.Before;
import org.junit.Test;
import org.test4j.fortest.hibernate.AddressService;
import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "file:./extern-spring/project.xml" })
public class SpringModuleTest implements Test4J {
    @SpringBeanByName("addressService")
    private AddressService addressService;

    /**
     * 更改spring bean的字段的值
     */
    @Test
    public void testBeforeTestSetUp_ChangeSpringBeanValue() {
        this.addressService = null;
    }

    /**
     * 验证spring bean的字段的值是从spring容器重新注入的
     */
    @Test
    public void testBeforeTestSetUp_CheckSpringBeanValue() {
        want.object(addressService).notNull();
    }

    @Before
    public void atestSetupClass() {
        want.object(addressService).notNull();
    }
}
