package org.test4j.module.jmockit.mockbug;

import org.test4j.junit.Test4J;
import org.test4j.module.spring.annotations.SpringContext;

@SpringContext({ "org/test4j/fortest/spring/sayhello.xml" })
public abstract class JMockitMockManageBaseTest extends Test4J {

}
