package org.test4j.hamcrest.iassert.impl;


import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IBooleanAssert;

public class BooleanAssert extends Assert<Boolean, IBooleanAssert> implements IBooleanAssert {

    public BooleanAssert() {
        super(Boolean.class, IBooleanAssert.class);
    }

    public BooleanAssert(Boolean value) {
        super(value, Boolean.class, IBooleanAssert.class);
    }

    @Override
    public IBooleanAssert is(boolean bl) {
        return this.isEqualTo(bl);
    }

    @Override
    public IBooleanAssert is(String message, boolean bl) {
        Matcher<?> matcher = IsEqual.equalTo(bl);
        return this.assertThat(message, matcher);
    }
}
