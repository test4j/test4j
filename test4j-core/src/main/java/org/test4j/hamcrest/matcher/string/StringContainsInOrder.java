package org.test4j.hamcrest.matcher.string;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.test4j.tools.commons.StringHelper.DOUBLE_QUOTATION;

/**
 * StringContainsInOrder
 *
 * @author wudarui
 */
public class StringContainsInOrder extends TypeSafeMatcher<String> {
    private final Iterable<String> substrings;

    private final StringMode[] modes;

    public StringContainsInOrder(Iterable<String> substrings) {
        this.substrings = substrings;
        this.modes = new StringMode[]{};
    }

    public StringContainsInOrder(Iterable<String> substrings, StringMode[] modes) {
        this.substrings = substrings;
        this.modes = modes;
    }

    String message = "";

    @Override
    public boolean matchesSafely(String s) {
        if (s == null) {
            message = "the actual can't be null.";
            return false;
        }
        String actual = StringMode.getStringByMode(s, modes);
        int fromIndex = 0;

        for (String substring : substrings) {
            if (substring == null) {
                message = "the sub string can't be null.";
                return false;
            }
            String expected = StringMode.getStringByMode(substring, modes);
            int index = actual.indexOf(expected, fromIndex);
            if (index == -1) {
                message = String.format("the string[%s] not contain substring[%s] from index %d.", actual, expected,
                        fromIndex);
                return false;
            } else {
                fromIndex = index + expected.length();
            }
        }
        return true;
    }

    @Override
    public void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendText(DOUBLE_QUOTATION).appendText(item).appendText(DOUBLE_QUOTATION);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string containing ").appendValueList("", ", ", "", substrings);
        if (modes != null && modes.length > 0) {
            description.appendText(" in order by modes" + StringMode.toString(modes));
        }
        if ("".endsWith(message) == false) {
            description.appendText(", but ").appendText(message);
        }
    }

    @Factory
    public static Matcher<String> stringContainsInOrder(Iterable<String> substrings) {
        return new StringContainsInOrder(substrings);
    }
}
