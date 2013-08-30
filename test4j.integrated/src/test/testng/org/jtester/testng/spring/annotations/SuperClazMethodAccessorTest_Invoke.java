package org.jtester.testng.spring.annotations;

import org.jtester.module.spring.annotations.ExMyService;
import org.jtester.module.spring.annotations.SpringContext;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SpringContext("org/jtester/module/spring/reflector/reflector-invoke.xml")
@Test(groups = "jtester")
public class SuperClazMethodAccessorTest_Invoke extends JTester {
	@SpringBeanByName
	private ExMyService myService;

	@Test
	public void privateInvoked() throws Throwable {
		String ret = myService.privateInvoked("test");
		want.string(ret).isEqualTo("privateInvoked:test");
	}

	@Test
	public void privateInvoked_nullPara() throws Throwable {
		String ret = myService.privateInvoked(null);
		want.string(ret).isEqualTo("privateInvoked:null");
	}
}
