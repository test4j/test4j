package org.test4j.hamcrest.iassert.impl;


import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.interal.IAssert;
import org.test4j.hamcrest.iassert.intf.INumberAssert;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NumberAssert<T extends Number & Comparable<T>, E extends INumberAssert<T, ?>>
        extends Assert<T, E> implements INumberAssert<T, E> {

    public NumberAssert(Class<? extends IAssert<?, ?>> clazE) {
        super(clazE);
    }

    public NumberAssert(T value, Class<? extends IAssert<?, ?>> clazE) {
        super(value, clazE);
    }

    public NumberAssert(T value, Class<? extends IAssert<?, ?>> clazE, Class valueClazz) {
        super(value, valueClazz, clazE);
    }
}
