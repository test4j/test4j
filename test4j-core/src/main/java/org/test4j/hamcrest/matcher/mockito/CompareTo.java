/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.test4j.hamcrest.matcher.mockito;


import org.hamcrest.Description;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class CompareTo<T extends Comparable<T>> extends ArgumentMatcher<T> {
    private final Comparable<T> wanted;

    public CompareTo(Comparable<T> value) {
        this.wanted = value;
    }

    @Override
    public boolean matches(Object actual) {

        if (!(actual instanceof Comparable)) {
            return false;
        }

        return matchResult(((Comparable) actual).compareTo(wanted));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(getName() + "(" + wanted + ")");
    }

    protected abstract String getName();

    protected abstract boolean matchResult(int result);
}
