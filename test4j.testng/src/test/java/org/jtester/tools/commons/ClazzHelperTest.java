package org.jtester.tools.commons;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.jtester.json.encoder.beans.test.TestedClazz;
import org.jtester.json.encoder.beans.test.TestedIntf;
import org.jtester.testng.JTester;
import org.jtester.tools.commons.ClazzHelper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("rawtypes")
@Test(groups = { "JTester" })
public class ClazzHelperTest extends JTester {

	@Test(dataProvider = "provideClazzName")
	public void getPackFromClassName(String clazz, String pack) {
		want.string(ClazzHelper.getPackFromClassName(clazz)).isEqualTo(pack);
	}

	@DataProvider
	public Object[][] provideClazzName() {
		return new String[][] { { "", "" }, { "EefErr", "" },
				{ "org.jtester.utility.ClazzUtilTest", "org.jtester.utility" } };
	}

	@Test(dataProvider = "dataProvider_testGetPathFromPath")
	public void testGetPathFromPath(String clazName, String path) {
		String _path = ClazzHelper.getPathFromPath(clazName);
		want.string(_path).isEqualTo(path);
	}

	@DataProvider
	public Object[][] dataProvider_testGetPathFromPath() {
		return new Object[][] { { "a.b.c.ImplClazz", "a/b/c" }, // <br>
				{ "ImplClazz", "" }, /** <br> **/
				{ ".ImplClazz", "" }
		/** <br> **/
		};
	}

	@Test
	public void testGetBytes() {
		byte[] bytes = ClazzHelper.getBytes(ClazzHelper.class);
		want.array(bytes).notNull().sizeGt(1);
	}

	@Test(dataProvider = "proxy_types")
	public void testGetUnProxyType(Class type, Class expected) {
		Class actual = ClazzHelper.getUnProxyType(type);
		want.object(actual).isEqualTo(expected);
	}

	@DataProvider
	public Object[][] proxy_types() {
		Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { TestedIntf.class },
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return null;
					}
				});
		return new Object[][] { { TestedClazz.class, TestedClazz.class },// <br>
				{ new TestedClazz() {
					{
					}
				}.getClass(), TestedClazz.class }, // <br>
				{ new TestedIntf() {
					{

					}
				}.getClass(), Object.class },// <br>
				{ proxy.getClass(), Object.class } // <br>
		};
	}
}