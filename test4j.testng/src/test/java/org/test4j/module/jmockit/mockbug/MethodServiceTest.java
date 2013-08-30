package org.test4j.module.jmockit.mockbug;

import mockit.Mock;

import org.test4j.module.jmockit.mockbug.TestedMethodService;
import org.test4j.testng.JTester;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class MethodServiceTest extends JTester {

	@BeforeMethod
	public void setup() {
		new MockUp<TestedMethodService>() {
			@Mock
			public void $init() {

			}

			@Mock
			public String sayHello() {
				return "hello,mock!";
			}
		};
	}

	public void testBeforeMethodMock() {
		TestedMethodService service = new TestedMethodService();
		String result = service.sayHello();
		System.out.println(result);
		want.string(result).isEqualTo("hello,mock!");

		String name = reflector.getField(service, "name");
		want.string(name).isNull();
	}
}
