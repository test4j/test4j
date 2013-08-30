package org.jtester.fortest.reflector;

@SuppressWarnings("unused")
public class TestObject extends SuperTestObject {

	private boolean magic = true;

	private int wrong;

	private static int aStaticPrivate = 27022008;

	private int ANonStandardJavaBeanStyleField = -1;

	private void throwingMethod() throws TestException {
		throw new TestException("from throwingMethod");
	}

	private void nonThrowingMethod() {
		// do nothing
	}

	private class InnerThrowsThrowable {

		private InnerThrowsThrowable() throws Throwable {
			throw new Throwable();
		}
	}

	private class InnerThrowsRuntimeException {

		private InnerThrowsRuntimeException() {
			throw new RuntimeException();
		}
	}

	private class InnerThrowsError {

		private InnerThrowsError() {
			throw new Error();
		}
	}
}
