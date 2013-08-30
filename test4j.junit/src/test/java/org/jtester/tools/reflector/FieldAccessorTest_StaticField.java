package org.jtester.tools.reflector;

import org.jtester.fortest.reflector.TestObject;
import org.jtester.junit.JTester;
import org.jtester.tools.exception.NoSuchFieldRuntimeException;
import org.junit.Before;
import org.junit.Test;

public class FieldAccessorTest_StaticField implements JTester {

    private FieldAccessor<Integer> aStaticPrivate;

    @Before
    public void setUp() throws Exception {
        aStaticPrivate = new FieldAccessor<Integer>(TestObject.class, "aStaticPrivate");
    }

    @Test(expected = NoSuchFieldRuntimeException.class)
    public void testStaticFieldAccessor() {
        new FieldAccessor<Integer>(Object.class, "missing");
    }

    @Test(expected = NullPointerException.class)
    public void testStaticFieldAccessor2() {
        new FieldAccessor<Integer>(null, "missing");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticFieldAccessor3() {
        FieldAccessor<Integer> accessor = new FieldAccessor<Integer>(TestObject.class, "aPrivate");
        accessor.setStatic(1);
        want.fail("The field should not be static: StaticFieldAccessor only accepts static fields");
    }

    @Test
    public void testStaticFieldAccessor4() {
        FieldAccessor<Integer> accessor = new FieldAccessor<Integer>(TestObject.class, "aSuperStaticPrivate");
        accessor.setStatic(1);
    }

    /**
     * Test method for {@link com.j2speed.accessor.FieldAccessor#get()}.
     */
    @Test
    public void testGet() {
        aStaticPrivate.setStatic(27022008);
        want.number(aStaticPrivate.getStatic().intValue()).isEqualTo(27022008);
    }

    /**
     * Test method for
     * {@link com.j2speed.accessor.FieldAccessor#set(java.lang.Object)}.
     */
    @Test
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
