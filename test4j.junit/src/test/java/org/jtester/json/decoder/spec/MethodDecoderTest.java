package org.jtester.json.decoder.spec;

import java.lang.reflect.Method;

import org.jtester.json.JSON;
import org.jtester.junit.JTester;
import org.junit.Ignore;
import org.junit.Test;

public class MethodDecoderTest implements JTester {

    @Test
    @Ignore
    public void testDecodeMethod() {
        String json = "{methodName:'demo2Para',declaredBy:'org.jtester.json.encoder.object.spec.MethodJsonDemo',paraType:['java.lang.String','java.lang.Integer']}";
        Method method = JSON.toObject(json, Method.class);
        want.object(method).notNull();
        want.string(method.getName()).isEqualTo("demo2Para");
    }
}
