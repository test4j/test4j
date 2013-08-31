package org.test4j.module.spring;

import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/test4j/module/spring/testedbeans/xml/beans.xml",
        "org/test4j/module/spring/testedbeans/xml/data-source.xml" })
public abstract class MockedBeanByNameTest_Base extends Test4J {

}
