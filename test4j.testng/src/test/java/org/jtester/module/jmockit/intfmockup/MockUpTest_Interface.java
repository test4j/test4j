package org.jtester.module.jmockit.intfmockup;

import mockit.Mock;

import org.jtester.module.jmockit.mockbug.SayHelloImpl;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("unused")
@Test
public class MockUpTest_Interface extends JTester {
	private ISayHello sayHello1;

	/**
	 * 测试接口使用MockUp的方式进行mock
	 */
	public void testInterfaceMockUp1() {
		new MockUp<ISayHello>() {
			{
				sayHello1 = this.getMockInstance();
			}

			@Mock
			public String sayHello() {
				return "say hello1.";
			}
		};

		String hello1 = this.sayHello1.sayHello();
		want.string(hello1).isEqualTo("say hello1.");
	}

	public void testInterfaceMockUp2() {

		sayHello1 = new MockUp<ISayHello>() {
			@Mock
			public String sayHello() {
				return "say hello2.";
			}
		}.getMockInstance();

		String hello1 = this.sayHello1.sayHello();
		want.string(hello1).isEqualTo("say hello2.");
	}
}
