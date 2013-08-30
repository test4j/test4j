package org.test4j.testng.jmockit;

import mockit.Mocked;

import org.test4j.fortest.formock.SomeInterface;
import org.test4j.fortest.formock.SpringBeanService;
import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.module.spring.annotations.SpringBeanFrom;
import org.test4j.module.spring.annotations.SpringContext;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@SpringContext({ "org/jtester/module/spring/testedbeans/xml/springbean service.xml" })
@Test(groups = { "jtester", "mock-demo" })
public class MockBeanTest_NodefineBean extends JTester {
	@SpringBeanByName
	private SpringBeanService springBeanService1;

	@Mocked
	@SpringBeanFrom
	protected SomeInterface dependency1;

	@SpringBeanFrom
	@Mocked
	protected SomeInterface dependency2;

	public void testDependency() {
		want.object(springBeanService1).notNull();
		want.object(springBeanService1.getDependency1()).notNull();
		want.object(springBeanService1.getDependency2()).notNull();
	}
}
