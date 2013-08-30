package org.jtester.testng.jmockit;

import mockit.Mocked;

import org.jtester.fortest.formock.SomeInterface;
import org.jtester.fortest.formock.SpringBeanService;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringBeanFrom;
import org.jtester.testng.JTester;
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
