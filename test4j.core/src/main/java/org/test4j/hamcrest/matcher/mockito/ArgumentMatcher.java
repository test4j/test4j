/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.test4j.hamcrest.matcher.mockito;


import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public abstract class ArgumentMatcher<T> extends BaseMatcher<T> {

    /**
     * Returns whether this matcher accepts the given argument.
     * <p>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     *
     * @param argument the argument
     * @return whether this matcher accepts the given argument.
     */
    public abstract boolean matches(Object argument);

    /*
     * By default this method decamlizes matchers name to promote meaningful names for matchers.
     * <p>
     * For example <b>StringWithStrongLanguage</b> matcher will generate 'String with strong language' description in case of failure.
     * <p>
     * You might want to override this method to
     * provide more specific description of the matcher (useful when
     * verification failures are reported).
     *
     * @param description the description to which the matcher description is
     * appended.
     */
    public void describeTo(Description description) {
        String className = getClass().getSimpleName();
        description.appendText(Decamelizer.decamelizeMatcher(className));
    }
}