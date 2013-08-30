package org.jtester.json.decoder.spec;

import java.lang.reflect.Method;

import org.jtester.json.JSON;
import org.jtester.testng.JTester;
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
