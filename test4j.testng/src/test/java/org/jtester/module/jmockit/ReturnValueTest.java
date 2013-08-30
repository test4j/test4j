package org.jtester.module.jmockit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mockit.Mocked;

import org.jtester.fortest.beans.ComplexObject;
import org.jtester.module.inject.annotations.Inject;
import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@SuppressWarnings("unchecked")
@Test(groups = "jtester")
public class ReturnValueTest extends JTester {

	public SomeService someService = new SomeService();

	@Mocked
	@Inject(targets = "someService")
	public SomeInterface someInterface;

	public void testMock() {
		new Expectations() {
			{
				someInterface.someCall(the.string().isEqualTo("darui.wu").wanted(),
						the.collection().sizeEq(0).wanted(List.class), the.map().any().wanted(HashMap.class));
				result = ComplexObject.instance();

			}
		};
		String result = this.someService.call("darui.wu");
		want.string(result).contains("name=");
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void testMock_ThrowException() {
		new Expectations() {
			{
				someInterface.someCall(the.string().isEqualTo("darui.wu").wanted(),
						the.collection().sizeEq(0).wanted(List.class), the.map().any().wanted(HashMap.class));
				thenThrow(new RuntimeException("test exception"));

			}
		};
		this.someService.call("darui.wu");
	}

	public void testMock_CatchThrowException() {
		new Expectations() {
			{
				someInterface.someCall(the.string().isEqualTo("darui.wu").wanted(),
						the.collection().sizeEq(0).wanted(List.class), the.map().any().wanted(HashMap.class));
				thenThrow(new RuntimeException("test exception"));

			}
		};
		try {
			this.someService.call("darui.wu");
		} catch (RuntimeException e) {
			want.string(e.getMessage()).isEqualTo("test exception");
		}
	}

	public void testThrowException() throws InterruptedException, IOException {
		new Expectations() {
			{
				someInterface.someCallException();
				maxTimes = -1;
				thenThrow(new IOException("test exception"));

			}
		};
		this.someService.callThrowException("darui.wu");
	}

	public void factualInvoke() {
		SomeInterface si = new SomeInterfaceImpl();
		ComplexObject so = si.someCall("darui.wu", null, null);
		want.object(so).propertyEq("name", "I am a test");
	}

	public static class SomeService {
		private SomeInterface someInterface;

		public String call(String name) {
			List<String> list = new ArrayList<String>();
			HashMap<String, String> map = new HashMap<String, String>();
			ComplexObject co = this.someInterface.someCall(name, list, map);
			return co.toString();
		}

		public String callThrowException(String name) throws InterruptedException {
			try {
				this.someInterface.someCallException();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}
	}

	public static interface SomeInterface {
		public ComplexObject someCall(String name, List<?> list, HashMap<String, String> map);

		public void someCallException() throws IOException;
	}

	public static class SomeInterfaceImpl implements SomeInterface {
		public ComplexObject someCall(String name, List<?> list, HashMap<String, String> map) {
			return ComplexObject.instance();
		}

		public void someCallException() throws IOException {
		}
	}

}
