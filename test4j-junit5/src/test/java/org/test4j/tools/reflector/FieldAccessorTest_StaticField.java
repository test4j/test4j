package org.test4j.tools.reflector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test4j.exception.NoSuchFieldRuntimeException;
import org.test4j.junit5.Test4J;
import org.test4j.reflector.TestObject;

public class FieldAccessorTest_StaticField extends Test4J {

    private FieldAccessor aStaticPrivate;

    @BeforeEach
    public void setUp() throws Exception {
        aStaticPrivate = FieldAccessor.field(TestObject.class, "aStaticPrivate");
    }

    @Test
    public void testStaticFieldAccessor() {
        want.exception(() ->
                        FieldAccessor.field(Object.class, "missing")
                , NoSuchFieldRuntimeException.class);
    }

    @Test
    public void testStaticFieldAccessor2() {
        want.exception(() ->
                        FieldAccessor.field(null, "missing")
                , NullPointerException.class);
    }

    @Test
    public void testStaticFieldAccessor3() {
        want.exception(() -> {
                    FieldAccessor accessor = FieldAccessor.field(TestObject.class, "aPrivate");
                    accessor.setStatic(1);
                }
                , IllegalArgumentException.class);
    }

    @Test
    public void testStaticFieldAccessor4() {
        FieldAccessor accessor = FieldAccessor.field(TestObject.class, "aSuperStaticPrivate");
        accessor.setStatic(1);
    }


    @Test
    public void testGet() {
        aStaticPrivate.setStatic(27022008);
        want.number(aStaticPrivate.<Integer>getStatic().intValue()).isEqualTo(27022008);
    }

    /**
     * Test method for
     */
    @Test
    public void testSet() {
        int newValue = 26072007;

        aStaticPrivate.setStatic(Integer.valueOf(newValue));
        want.number(aStaticPrivate.<Integer>getStatic().intValue()).isEqualTo(newValue);
        try {
            aStaticPrivate.setStatic(null);
            want.fail();
        } catch (Throwable e) {
            String info = e.getMessage();
            want.string(info).contains("to set field").contains("into target").contains("error");
        }
    }
}
