package org.test4j.tools.reflector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.reflector.TestObject;
import org.test4j.tools.exception.NoSuchFieldRuntimeException;

@SuppressWarnings("rawtypes")
public class FieldAccessorTest extends Test4J {

    private FieldAccessor aPrivate;

    @BeforeEach
    public void setUp() throws Exception {
        aPrivate = FieldAccessor.field(TestObject.class, "aPrivate");
    }

    @AfterEach
    public void tearDown() throws Exception {
        aPrivate = null;
    }

    @Test
    public void testFieldAccessor1() {
        want.exception(() ->
                        FieldAccessor.field(Object.class, "missing")
                , NoSuchFieldRuntimeException.class);
    }

    @Test
    public void testFieldAccessor2() {
        want.exception(() ->
                        FieldAccessor.field("missing", null)
                , NullPointerException.class);
    }

    @Test
    public void testFieldAccessor3() {
        FieldAccessor accessor = FieldAccessor.field(TestObject.class, "aSuperStaticPrivate");
        accessor.setStatic(1);
    }


    @Test
    public void testGet() {
        TestObject toTest = new TestObject();
        int actual = ((Integer) aPrivate.get(toTest)).intValue();
        want.number(actual).isEqualTo(26071973);
    }


    @Test
    public void testSet() {
        TestObject toTest = new TestObject();
        int newValue = 26072007;
        aPrivate.set(toTest, Integer.valueOf(newValue));
        int actual = ((Integer) aPrivate.get(toTest)).intValue();
        want.number(actual).isEqualTo(newValue);
        try {
            aPrivate.set(toTest, null);
            want.fail();
        } catch (Throwable e) {
            String info = e.getMessage();
            want.string(info).contains("to set field").contains("into target").contains("error");
        }
    }
}
