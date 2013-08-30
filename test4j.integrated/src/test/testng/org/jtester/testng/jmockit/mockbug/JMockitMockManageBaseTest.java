package org.jtester.testng.jmockit.mockbug;

import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/fortest/spring/sayhello.xml" })
@Test(groups = "for-test")
public class JMockitMockManageBaseTest extends JTester {

}
