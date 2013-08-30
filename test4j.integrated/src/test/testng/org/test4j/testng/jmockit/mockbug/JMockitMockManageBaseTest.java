package org.test4j.testng.jmockit.mockbug;

import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/fortest/spring/sayhello.xml" })
@Test(groups = "for-test")
public class JMockitMockManageBaseTest extends JTester {

}
