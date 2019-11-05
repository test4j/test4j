/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.test4j.hamcrest.matcher.mockito;

import org.hamcrest.Description;

import java.io.Serializable;
import java.util.regex.Pattern;


public class Find extends ArgumentMatcher<String> implements Serializable {

    private static final long serialVersionUID = 8895781429480404872L;
    private final String regex;

    public Find(String regex) {
        this.regex = regex;
    }

    public boolean matches(Object actual) {
        return actual != null && Pattern.compile(regex).matcher((String) actual).find();
    }

    public void describeTo(Description description) {
        description.appendText("find(\"" + regex.replaceAll("\\\\", "\\\\\\\\") + "\")");
    }
}