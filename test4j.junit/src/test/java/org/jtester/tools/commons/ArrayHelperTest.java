package org.jtester.tools.commons;

import java.util.Arrays;
import java.util.HashMap;

import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

public class ArrayHelperTest implements JTester {
    @Test
    public void convert_charArr() {
        char[] chs = { 'a', 'b' };
        want.array(chs).sizeIs(2);
        Object[] os = ArrayHelper.toArray(chs);
        want.array(os).sizeIs(2);
        want.array(os).hasAllItems('a', 'b');
    }

    @Test
    public void convert_booleanArr() {
        boolean[] bls = { true, false, true };
        want.array(bls).sizeIs(3);
        Object[] os = ArrayHelper.toArray(bls);
        want.array(os).sizeIs(3);
        want.array(os).hasItems(true);
    }

    @Test
    public void convert_byteArr() {
        byte[] bytes = { Byte.MAX_VALUE, Byte.MIN_VALUE };
        want.array(bytes).sizeIs(2);
        Object[] os = ArrayHelper.toArray(bytes);
        want.array(os).sizeIs(2);
        want.array(os).hasAllItems(Byte.MAX_VALUE, Byte.MIN_VALUE);
    }

    @Test
    public void convert_shortArr() {
        short[] shorts = { 2, 4, 5 };
        want.array(shorts).sizeIs(3);
        Object[] os = ArrayHelper.toArray(shorts);
        want.array(os).sizeIs(3);
        want.array(os).hasItems((short) 2);

        // want.number((short)5).isEqualTo(5);
    }

    @Test
    public void convert_intArr() {
        int[] ints = { 2, 4, 5 };
        want.array(ints).sizeIs(3);
        Object[] os = ArrayHelper.toArray(ints);
        want.array(os).sizeIs(3);
        want.array(os).hasItems(4);
    }

    @Test
    public void convert_longArr() {
        long[] longs = { 2L, 421355L, 51255L };
        want.array(longs).sizeIs(3);
        Object[] os = ArrayHelper.toArray(longs);
        want.array(os).sizeIs(3);
        want.array(os).hasItems(51255L);
    }

    @Test
    public void convert_floatArr() {
        float[] fs = { 2.0f, 4.0f, 5.1f };
        want.array(fs).sizeIs(3);
        Object[] os = ArrayHelper.toArray(fs);
        want.array(os).sizeIs(3);
        want.array(os).hasItems(5.1f);
    }

    @Test
    public void convert_doubleArr() {
        double[] ds = { 2.0d, 4.0d, 5.1d };
        want.array(ds).sizeIs(3);
        Object[] os = ArrayHelper.toArray(ds);
        want.array(os).sizeIs(3);
        want.array(os).hasItems(5.1d);
    }

    @Test
    @DataFrom("dataOfSized")
    public void testSizeOf(Object o, int size) {
        int actual = ArrayHelper.sizeOf(o);
        want.number(actual).isEqualTo(size);
    }

    @SuppressWarnings("rawtypes")
    public static Object[][] dataOfSized() {
        return new Object[][] { { new HashMap(), 0 },// <br>
                { new int[] { 1 }, 1 }, /** <br> */
                { Arrays.asList("", ""), 2 } /** <br> */
        };
    }
}
