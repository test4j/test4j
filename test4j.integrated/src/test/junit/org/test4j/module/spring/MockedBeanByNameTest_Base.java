package org.test4j.module.spring;

import org.test4j.junit.JTester;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public abstract class MockedBeanByNameTest_Base implements JTester {

}
