package org.test4j.testng.jmockit.mockbug;

import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.Test4J;
import org.testng.annotations.Test;

@SpringContext({ "org/test4j/fortest/spring/sayhello.xml" })
@Test(groups = "for-test")
public class JMockitMockManageBaseTest extends Test4J {

}
