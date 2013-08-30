package org.jtester.module.jmockit.mockbug;

import org.jtester.junit.JTester;
import org.jtester.module.spring.annotations.SpringContext;

@SpringContext({ "org/jtester/fortest/spring/sayhello.xml" })
public abstract class JMockitMockManageBaseTest implements JTester {

}
