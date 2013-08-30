package org.jtester.module.spring;

import org.jtester.module.spring.annotations.SpringContext;
import org.test4j.junit.JTester;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/beans.xml",
        "org/jtester/module/spring/testedbeans/xml/data-source.xml" })
public abstract class MockedBeanByNameTest_Base implements JTester {

}
