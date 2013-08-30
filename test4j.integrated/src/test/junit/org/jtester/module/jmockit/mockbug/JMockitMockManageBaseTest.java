package org.jtester.module.jmockit.mockbug;

import org.jtester.module.spring.annotations.SpringContext;
import org.test4j.junit.JTester;

@SpringContext({ "org/jtester/fortest/spring/sayhello.xml" })
public abstract class JMockitMockManageBaseTest implements JTester {

}
