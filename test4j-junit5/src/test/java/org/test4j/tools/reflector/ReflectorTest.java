package org.test4j.tools.reflector;

import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class ReflectorTest extends Test4J {
    @Test
    public void setField_privateStaticField() {
        reflector.setFieldValue(ClazzForReflectTest.class, "myStatic", "zzzzz");
        String ret = ClazzForReflectTest.getMyStatic();
        want.string(ret).isEqualTo("zzzzz");
    }

    @Test
    public void getField_privateStaticField() {
        ClazzForReflectTest.setMyStatic("bbbb");
        String ret = (String) reflector.getFieldValue(ClazzForReflectTest.class, "myStatic");

        want.string(ret).isEqualTo("bbbb");
    }

    @Test
    public void setField_privateField() {
        ClazzForReflectTest tested = new ClazzForReflectTest();
        reflector.setFieldValue(tested, "myPrivate", "zzzzz");
        String ret = tested.getMyPrivate();
        want.string(ret).isEqualTo("zzzzz");
    }

    @Test
    public void getField_privateField() {
        ClazzForReflectTest tested = new ClazzForReflectTest();
        tested.setMyPrivate("bbbb");
        String ret = (String) reflector.getFieldValue(tested, "myPrivate");

        want.string(ret).isEqualTo("bbbb");
    }

    @Test
    public void invoke_privateMethod() throws Exception {
        ClazzForReflectTest tested = new ClazzForReflectTest();
        String ret = (String) reflector.invoke(tested, "privateMethod");
        want.string(ret).isEqualTo("private method");
    }

    @Test
    public void invoke_PrivateStaticMethod() throws Exception {
        String ret = (String) reflector.invoke(ClazzForReflectTest.class, "myStaticMethod");
        want.string(ret).isEqualTo("static method");
    }
}
