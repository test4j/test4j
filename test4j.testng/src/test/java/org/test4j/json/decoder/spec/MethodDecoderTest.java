package org.test4j.json.decoder.spec;

import java.lang.reflect.Method;

import org.test4j.json.JSON;
import org.test4j.testng.JTester;
import org.testng.annotations.Test;

@Test(groups = "json")
public class MethodDecoderTest extends JTester {

	@Test(groups = "broken")
	public void testDecodeMethod() {
		String json = "{methodName:'demo2Para',declaredBy:'org.jtester.json.encoder.object.spec.MethodJsonDemo',paraType:['java.lang.String','java.lang.Integer']}";
		Method method = JSON.toObject(json, Method.class);
		want.object(method).notNull();
		want.string(method.getName()).isEqualTo("demo2Para");
	}
}
