package org.jtester.testng.jmockit.mockbug;

import mockit.Mock;

import org.jtester.module.jmockit.mockbug.TestedMethodService;
import org.jtester.module.spring.annotations.AutoBeanInject;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.module.spring.annotations.SpringInitMethod;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test
@SpringContext
@AutoBeanInject
public class MethodServiceTest_SpringInject extends JTester {
	@SpringBeanByName
	TestedMethodService service;

	@SpringInitMethod
	protected void mockTestedMethodService() {
		new MockUp<TestedMethodService>() {
			TestedMethodService it;

			@Mock
			public void $init() {
				reflector.setField(it, "name", "init mock");
			}
		};
	}

	public void testBeforeMethodMock() {
		String result = service.sayHello();
		System.out.println(result);
		want.string(result).isEqualTo("hello, init mock");

		String name = reflector.getField(service, "name");
		want.string(name).isEqualTo("init mock");
	}
}
