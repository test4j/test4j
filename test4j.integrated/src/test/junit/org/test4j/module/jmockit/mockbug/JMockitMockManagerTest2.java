package org.test4j.module.jmockit.mockbug;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Test;
import org.test4j.module.jmockit.mockbug.SayHelloImpl;
import org.test4j.module.jmockit.mockbug.SayHelloImpl2;
import org.test4j.module.spring.annotations.SpringBeanByName;

@SuppressWarnings("unused")
public class JMockitMockManagerTest2 extends JMockitMockManageBaseTest {

	@SpringBeanByName
	private SayHelloImpl sayHello;

	@SpringBeanByName
	private SayHelloImpl2 sayHello2;

	@Test
	public void sayHello_real1() {
		String str = sayHello.sayHello();
		want.string(str).isEqualTo("say hello:darui.wu");
	}

	@Test
	public void sayHello_real2() {
		new MockUp<SayHelloImpl>() {
			@Mock
			public String getName() {
				return "test";
			}
		};
		String str = sayHello.sayHello();
		want.string(str).isEqualTo("say hello:test");
	}

	@Test
	public void sayHello_real3() {
		String str = sayHello2.sayHello();
		want.string(str).isEqualTo("say hello:darui.wu");
	}
}
