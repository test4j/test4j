package org.test4j.json.encoder.single.fixed;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;
import org.test4j.json.encoder.EncoderTest;
import org.test4j.json.encoder.beans.test.TestedClazz;
import org.test4j.json.encoder.beans.test.TestedIntf;
import org.test4j.json.encoder.single.fixed.ClazzEncoder;
import org.test4j.junit.annotations.DataFrom;

@SuppressWarnings("rawtypes")
public class ClazzEncoderTest extends EncoderTest {

	@Test
	@DataFrom("clazz_data")
	public void testClazzEncoder(Class type, String name) {
		ClazzEncoder encoder = ClazzEncoder.instance;
		this.setUnmarkFeature(encoder);
		encoder.encode(type, writer, references);

		String json = writer.toString();
		want.string(json).isEqualTo("'" + name + "'");
	}

	public static Object[][] clazz_data() {
		Object proxy = Proxy.newProxyInstance(ClazzEncoderTest.class.getClassLoader(),
				new Class[] { TestedIntf.class }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return null;
					}
				});

		return new Object[][] { { ClazzEncoderTest.class, ClazzEncoderTest.class.getName() },// <br>
				{ new TestedClazz() {
				}.getClass(), TestedClazz.class.getName() },// <br>
				{ proxy.getClass(), "java.lang.Object" } };
	}
}
