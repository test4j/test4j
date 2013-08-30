package org.jtester.tools.reflector;

import org.jtester.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class ReflectorTest extends JTester {

	public void setField_privateStaticField() {
		reflector.setStaticField(ClazzForReflectTest.class, "myStatic", "zzzzz");
		String ret = ClazzForReflectTest.getMyStatic();
		want.string(ret).isEqualTo("zzzzz");
	}

	public void getField_privateStaticField() {
		ClazzForReflectTest.setMyStatic("bbbb");
		String ret = (String) reflector.getStaticField(ClazzForReflectTest.class, "myStatic");

		want.string(ret).isEqualTo("bbbb");
	}

	public void setField_privateField() {
		ClazzForReflectTest tested = new ClazzForReflectTest();
		reflector.setField(tested, "myPrivate", "zzzzz");
		String ret = tested.getMyPrivate();
		want.string(ret).isEqualTo("zzzzz");
	}

	public void getField_privateField() {
		ClazzForReflectTest tested = new ClazzForReflectTest();
		tested.setMyPrivate("bbbb");
		String ret = (String) reflector.getField(tested, "myPrivate");

		want.string(ret).isEqualTo("bbbb");
	}

	public void invoke_privateMethod() throws Exception {
		ClazzForReflectTest tested = new ClazzForReflectTest();
		String ret = (String) reflector.invoke(tested, "privateMethod");
		want.string(ret).isEqualTo("private method");
	}

	public void invoke_PrivateStaticMethod() throws Exception {
		String ret = (String) reflector.invokeStatic(ClazzForReflectTest.class, "myStaticMethod");
		want.string(ret).isEqualTo("static method");
	}
}
