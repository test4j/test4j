package org.test4j.module.jmockit.mockbug;

import org.test4j.junit.JTester;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/jtester/fortest/spring/sayhello.xml" })
public abstract class JMockitMockManageBaseTest implements JTester {

}
