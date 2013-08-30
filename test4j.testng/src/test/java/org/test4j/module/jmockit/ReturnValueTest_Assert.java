package org.test4j.module.jmockit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import mockit.Mocked;

import org.test4j.fortest.beans.ComplexObject;
import org.test4j.module.inject.annotations.Inject;
import org.test4j.module.jmockit.ReturnValueTest.SomeInterface;
import org.test4j.module.jmockit.ReturnValueTest.SomeService;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReturnValueTest_Assert extends JTester {
	public SomeService someService = new SomeService();

	@Mocked
	@Inject(targets = "someService")
	public SomeInterface someInterface;

	@Test(description="断言与值混用")
	public void testJMockit() throws IOException {
		new Expectations() {
			{
				someInterface.someCallException();

				someInterface.someCall("darui.wu", (List) any, (HashMap) any);
				result = ComplexObject.instance();

			}
		};
		someInterface.someCallException();

		String result = this.someService.call("darui.wu");
		want.string(result).contains("name=");
	}
}
