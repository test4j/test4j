package org.jtester.tools.reflector;

@SuppressWarnings("unused")
public class ClazzForReflectTest {

	private String myPrivate;

	private static String myStatic;

	private static String myStaticMethod() {
		return "static method";
	}

	private String privateMethod() {
		return "private method";
	}

	public String getMyPrivate() {
		return myPrivate;
	}

	public void setMyPrivate(String myPrivate) {
		this.myPrivate = myPrivate;
	}

	public static String getMyStatic() {
		return myStatic;
	}

	public static void setMyStatic(String myStatic) {
		ClazzForReflectTest.myStatic = myStatic;
	}
}
