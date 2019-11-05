/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.test4j.hamcrest.matcher.mockito;

import org.hamcrest.Description;

import java.io.Serializable;


public class Matches extends ArgumentMatcher<Object> implements Serializable {

    private static final long serialVersionUID = 8787704593379472029L;
    private final String regex;

    public Matches(String regex) {
        this.regex = regex;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String) && ((String) actual).matches(regex);
    }

    public void describeTo(Description description) {
        description.appendText("matches(\"" + regex.replaceAll("\\\\", "\\\\\\\\") + "\")");
    }
}
