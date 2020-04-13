/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.test4j.hamcrest.matcher.mockito;

import org.hamcrest.Description;

import java.io.Serializable;


public class EqualsWithDelta extends ArgumentMatcher<Number> implements Serializable {
    private static final long serialVersionUID = 5066980489920383664L;

    private final Number wanted;

    private final Number delta;

    public EqualsWithDelta(Number value, Number delta) {
        this.wanted = value;
        this.delta = delta;
    }

    @Override
    public boolean matches(Object actual) {
        Number actualNumber = (Number) actual;
        return wanted.doubleValue() - delta.doubleValue() <= actualNumber.doubleValue()
                && actualNumber.doubleValue() <= wanted.doubleValue()
                + delta.doubleValue();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("eq(" + wanted + ", " + delta + ")");
    }
}
