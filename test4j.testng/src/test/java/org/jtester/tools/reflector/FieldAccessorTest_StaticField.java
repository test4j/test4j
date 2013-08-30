package org.jtester.tools.reflector;

import org.jtester.fortest.reflector.TestObject;
import org.jtester.testng.JTester;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = "jtester")
public class FieldAccessorTest_StaticField extends JTester {

	private FieldAccessor<Integer> aStaticPrivate;

	@BeforeMethod
	public void setUp() throws Exception {
		aStaticPrivate = new FieldAccessor<Integer>(TestObject.class, "aStaticPrivate");
	}

	@Test(expectedExceptions = NoSuchFieldRuntimeException.class)
	public void testStaticFieldAccessor() {
		new FieldAccessor<Integer>(Object.class, "missing");
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testStaticFieldAccessor2() {
		new FieldAccessor<Integer>(null, "missing");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testStaticFieldAccessor3() {
		FieldAccessor<Integer> accessor = new FieldAccessor<Integer>(TestObject.class, "aPrivate");
		accessor.setStatic(1);
		want.fail("The field should not be static: StaticFieldAccessor only accepts static fields");
	}

	@Test
	// (expectedExceptions = RuntimeException.class)
	public void testStaticFieldAccessor4() {
		FieldAccessor<Integer> accessor = new FieldAccessor<Integer>(TestObject.class, "aSuperStaticPrivate");
		accessor.setStatic(1);
		// want.fail("The field should not be declared in the specified class: StaticFieldAccessor only accepts static fields declared in the specified class");
	}

	/**
	 * Test method for {@link com.j2speed.accessor.FieldAccessor#get()}.
	 */
	@Test
	public void testGet() {
		want.number(aStaticPrivate.getStatic().intValue()).isEqualTo(27022008);
	}

	/**
	 * Test method for
	 * {@link com.j2speed.accessor.FieldAccessor#set(java.lang.Object)}.
	 */
	@Test(dependsOnMethods = "testGet")
	public void testSet() {
		int newValue = 26072007;

		aStaticPrivate.setStatic(Integer.valueOf(newValue));
		want.number(aStaticPrivate.getStatic().intValue()).isEqualTo(newValue);
		try {
			aStaticPrivate.setStatic(null);
			want.fail();
		} catch (Throwable e) {
			String info = e.getMessage();
			want.string(info).contains("to set field").contains("into target").contains("error");
		}
	}
}
