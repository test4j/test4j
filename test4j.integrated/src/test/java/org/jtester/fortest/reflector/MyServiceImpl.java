package org.jtester.fortest.reflector;

import java.util.Map;

@SuppressWarnings({ "unused", "rawtypes" })
public class MyServiceImpl implements MyService {

	private String privateStr;

	public void mySay() {
		// do business
	}

	protected String protectedInvoked() {
		// ... do something
		return "protectedInvoked";
	}

	private String privateInvoked(String in) {
		// ... do something
		return "privateInvoked:" + in;
	}

	private int primitivePara(int i, boolean bl) {
		return 4;
	}

	private int mapPara(Map map) {
		return map.size();
	}

	private void invokeException() {
		throw new MyTestException("test invoke exception");
	}

	public static class MyTestException extends RuntimeException {
		private static final long serialVersionUID = 7021065297701291216L;

		public MyTestException() {
			super();
		}

		public MyTestException(String message) {
			super(message);
		}
	}
}
