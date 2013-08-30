package org.jtester.tools.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.jtester.fortest.beans.User;
import org.jtester.junit.JTester;
import org.jtester.junit.annotations.DataFrom;
import org.junit.Test;

@SuppressWarnings({ "rawtypes" })
public class ArrayHelperTest_2 implements JTester {
    @Test
    @DataFrom("arrayProvider")
    public void isArray(Object array) {
        want.bool(ArrayHelper.isArray(array)).is(true);

    }

    @Test
    public void isArray_False() {
        want.bool(ArrayHelper.isArray(null)).is(false);
        want.bool(ArrayHelper.isArray(1)).is(false);
    }

    @Test
    @DataFrom("collProvider")
    public void convert(Collection<?> coll) {
        want.bool(ArrayHelper.isArray(ArrayHelper.toArray(coll))).is(true);
    }

    @Test
    @DataFrom("collProvider")
    public void isCollection(Collection<?> coll) {
        want.bool(ListHelper.isCollection(coll)).is(true);
    }

    @Test
    public void isCollection_False() {
        want.bool(ListHelper.isCollection(null)).is(false);
        want.bool(ListHelper.isCollection(true)).is(false);
        want.bool(ListHelper.isCollection(Integer.valueOf(1))).is(false);
    }

    @Test
    @DataFrom("collProvider")
    public void sizeOf_collection(Object coll) {
        want.number(ArrayHelper.sizeOf(coll)).isEqualTo(2);
    }

    @Test
    @DataFrom("arrayProvider")
    public void sizeOf_array(Object array) {
        want.number(ArrayHelper.sizeOf(array)).isEqualTo(2);
    }

    @Test
    public void sizeOf_One() {
        want.number(ArrayHelper.sizeOf(null)).isEqualTo(0);
        want.number(ArrayHelper.sizeOf(1)).isEqualTo(1);
    }

    public static Object[][] collProvider() {
        return new Object[][] { { Arrays.asList('a', 'b') }, /** <br> */
        { Arrays.asList(true, false) }, /** <br> */
        { Arrays.asList(1, 2) }, /** <br> */
        { Arrays.asList(1L, 2L) }, /** <br> */
        { Arrays.asList(null, null) }, /** <br> */
        { Arrays.asList(1f, 2f) } };
    }

    public static Object[][] arrayProvider() {
        return new Object[][] { { new char[] { 'a', 'b' } }, /** <br> */
        { new boolean[] { true, false } }, /** <br> */
        { new byte[] { Byte.MAX_VALUE, Byte.MIN_VALUE } }, /** <br> */
        { new short[] { 1, 2 } }, /** <br> */
        { new int[] { 1, 2 } }, /** <br> */
        { new long[] { 1L, 2L } }, /** <br> */
        { new float[] { 1f, 2f } }, /** <br> */
        { new double[] { 1d, 2d } }, /** <br> */
        { new Object[] { null, null } } };
    }

    @Test
    @DataFrom("testIsCollOrArray_data")
    public void testIsCollOrArray(Object o, boolean isSet) {
        boolean actual = ArrayHelper.isCollOrArray(o);
        want.bool(actual).isEqualTo(isSet);
    }

    public static Object[][] testIsCollOrArray_data() {
        return new Object[][] { { new int[] {}, true },// <br>
                { new boolean[] { true }, true },// <br>
                { new ArrayList(), true },// <br>
                { new HashSet(), true },// <br>
                { new HashMap(), false },// <br>
                { new User(), false } // <br>
        };
    }
}
