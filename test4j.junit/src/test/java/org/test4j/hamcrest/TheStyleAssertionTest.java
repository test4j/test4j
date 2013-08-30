package org.test4j.hamcrest;

import org.junit.Test;
import org.test4j.hamcrest.iassert.object.intf.IArrayAssert;
import org.test4j.hamcrest.iassert.object.intf.IBooleanAssert;
import org.test4j.hamcrest.iassert.object.intf.IByteAssert;
import org.test4j.hamcrest.iassert.object.intf.ICharacterAssert;
import org.test4j.hamcrest.iassert.object.intf.ICollectionAssert;
import org.test4j.hamcrest.iassert.object.intf.IDateAssert;
import org.test4j.hamcrest.iassert.object.intf.IDoubleAssert;
import org.test4j.hamcrest.iassert.object.intf.IFileAssert;
import org.test4j.hamcrest.iassert.object.intf.IFloatAssert;
import org.test4j.hamcrest.iassert.object.intf.IIntegerAssert;
import org.test4j.hamcrest.iassert.object.intf.ILongAssert;
import org.test4j.hamcrest.iassert.object.intf.IMapAssert;
import org.test4j.hamcrest.iassert.object.intf.INumberAssert;
import org.test4j.hamcrest.iassert.object.intf.IObjectAssert;
import org.test4j.hamcrest.iassert.object.intf.IShortAssert;
import org.test4j.hamcrest.iassert.object.intf.IStringAssert;
import org.test4j.junit.Test4J;

public class TheStyleAssertionTest implements Test4J {
    @Test
    public void theAssert() {
        want.object(the.string()).clazIs(IStringAssert.class);
        want.object(the.bool()).clazIs(IBooleanAssert.class);
        want.object(the.number()).clazIs(INumberAssert.class);
        want.object(the.integer()).clazIs(IIntegerAssert.class);
        want.object(the.longnum()).clazIs(ILongAssert.class);
        want.object(the.doublenum()).clazIs(IDoubleAssert.class);
        want.object(the.floatnum()).clazIs(IFloatAssert.class);
        want.object(the.shortnum()).clazIs(IShortAssert.class);
        want.object(the.character()).clazIs(ICharacterAssert.class);
        want.object(the.bite()).clazIs(IByteAssert.class);
        want.object(the.array()).clazIs(IArrayAssert.class);
        want.object(the.map()).clazIs(IMapAssert.class);
        want.object(the.collection()).clazIs(ICollectionAssert.class);
        want.object(the.object()).clazIs(IObjectAssert.class);
        want.object(the.file()).clazIs(IFileAssert.class);
        want.object(the.calendar()).clazIs(IDateAssert.class);
        want.object(the.date()).clazIs(IDateAssert.class);
    }

    @Test
    public void testString() {
        want.object(new Integer(1)).eqToString("1");
    }
}
