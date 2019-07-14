package org.test4j.hamcrest.iassert.impl;


import org.hamcrest.Matcher;
import org.test4j.hamcrest.iassert.interal.Assert;
import org.test4j.hamcrest.iassert.intf.IStringAssert;
import org.test4j.hamcrest.matcher.mockito.Matches;
import org.test4j.hamcrest.matcher.string.StringOccursMatcher;

@SuppressWarnings({"rawtypes"})
public class StringAssert<E extends IStringAssert>
        extends Assert<String, IStringAssert>
        implements IStringAssert {
    public StringAssert() {
        super(String.class, IStringAssert.class);
    }

    protected StringAssert(Class<? extends IStringAssert> klass) {
        super(String.class, klass);
    }

    public StringAssert(boolean toString) {
        super(String.class, IStringAssert.class);
        if (toString) {
            this.getAssertObject().setValue("toString");
        }
    }

    public StringAssert(String str) {
        super(str, String.class, IStringAssert.class);
    }

    public StringAssert(String str, Class<? extends IStringAssert> klass) {
        super(str, String.class, klass);
    }

    public IStringAssert regular(String regex) {
        Matcher matcher = new Matches(regex);
        return (IStringAssert) this.assertThat(matcher);
    }

    @Override
    public IStringAssert occurs(int times, String sub) {
        Matcher matcher = new StringOccursMatcher(times, sub);
        return this.assertThat(matcher);
    }
}
