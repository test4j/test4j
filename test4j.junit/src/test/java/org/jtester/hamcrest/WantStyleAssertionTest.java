package org.jtester.hamcrest;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.jtester.hamcrest.iassert.object.intf.IArrayAssert;
import org.jtester.hamcrest.iassert.object.intf.IBooleanAssert;
import org.jtester.hamcrest.iassert.object.intf.IByteAssert;
import org.jtester.hamcrest.iassert.object.intf.ICharacterAssert;
import org.jtester.hamcrest.iassert.object.intf.IDoubleAssert;
import org.jtester.hamcrest.iassert.object.intf.IFileAssert;
import org.jtester.hamcrest.iassert.object.intf.IFloatAssert;
import org.jtester.hamcrest.iassert.object.intf.IIntegerAssert;
import org.jtester.hamcrest.iassert.object.intf.ILongAssert;
import org.jtester.hamcrest.iassert.object.intf.IShortAssert;
import org.jtester.hamcrest.iassert.object.intf.IStringAssert;
import org.jtester.junit.JTester;
import org.junit.Test;

public class WantStyleAssertionTest implements JTester {
    @Test
    public void wantAssert() {
        want.object(want.string(new String())).clazIs(IStringAssert.class);
        want.object(want.bool(true)).clazIs(IBooleanAssert.class);
        want.object(want.bool(Boolean.TRUE)).clazIs(IBooleanAssert.class);
        // // number
        want.object(want.number(Short.valueOf("1"))).clazIs(IShortAssert.class);
        want.object(want.number(1)).clazIs(IIntegerAssert.class);
        want.object(want.number(1L)).clazIs(ILongAssert.class);
        want.object(want.number(1f)).clazIs(IFloatAssert.class);
        want.object(want.number(1d)).clazIs(IDoubleAssert.class);
        want.object(want.character('c')).clazIs(ICharacterAssert.class);
        want.object(want.bite(Byte.MAX_VALUE)).clazIs(IByteAssert.class);

        want.object(want.array(new boolean[] {})).clazIs(IArrayAssert.class);
        want.object(want.file(new File(""))).clazIs(IFileAssert.class);
    }

    @Test(expected = AssertionError.class)
    public void wantAssert_Failure() {
        want.fail("error message");
    }

    @Test(expected = AssertionError.class)
    public void wantAssert_Failure2() {
        want.bool(true).isEqualTo(false);
    }

    @Test
    public void wantNumber_BigDecimal() {
        want.number(new BigDecimal("100.256")).isEqualTo(new BigDecimal("100.256"));
    }

    @Test
    public void wantNumber_BigInteger() {
        want.number(new BigInteger("10111111111111")).isEqualTo(new BigInteger("10111111111111"));
    }

    @Test
    public void wantNumber_Byte() {
        want.number(new Byte("127")).isEqualTo(new Byte("127"));
    }
}
