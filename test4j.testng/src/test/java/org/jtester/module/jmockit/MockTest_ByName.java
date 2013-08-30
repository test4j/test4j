package org.jtester.module.jmockit;

import mockit.Mocked;

import org.jtester.fortest.formock.SomeInterface;
import org.jtester.fortest.formock.SpringBeanService;
import org.jtester.fortest.formock.SpringBeanService.SpringBeanServiceImpl1;
import org.jtester.module.inject.annotations.Inject;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class MockTest_ByName extends JTester {

	private SpringBeanService springBeanService = new SpringBeanServiceImpl1();

	@Inject(targets = "springBeanService", properties = "dependency1")
	@Mocked
	private SomeInterface someInterface1;

	@Inject(targets = "springBeanService", properties = "dependency2")
	@Mocked
	private SomeInterface someInterface2;

	@Test
	public void testMock_ByName() {
		SomeInterface intf1 = springBeanService.getDependency1();
		want.object(intf1).not(the.object().same(someInterface1));
		want.object(springBeanService.getDependency2()).not(the.object().same(someInterface2));
	}
}
