package org.test4j.hamcrest.object;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.object.HasToString.hasToString;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.AbstractMatcherTest;


public class HasToStringTest extends AbstractMatcherTest {
    private static final String TO_STRING_RESULT = "toString result";
    private static final Object ARG = new Object() {
        @Override
        public String toString() {
            return TO_STRING_RESULT;
        }
    };

    @Override
    protected Matcher<?> createMatcher() {
        return hasToString(equalTo("irrelevant"));
    }

    @Test
    public void testPassesResultOfToStringToNestedMatcher() {
        assertThat(ARG, hasToString(equalTo(TO_STRING_RESULT)));
        assertThat(ARG, not(hasToString(equalTo("OTHER STRING"))));
    }

    @Test
    public void testProvidesConvenientShortcutForHasToStringEqualTo() {
        assertThat(ARG, hasToString(TO_STRING_RESULT));
        assertThat(ARG, not(hasToString("OTHER STRING")));
    }

    @Test
    public void testHasReadableDescription() {
        Matcher<? super String> toStringMatcher = equalTo(TO_STRING_RESULT);
        Matcher<Matcher<String>> matcher = hasToString(toStringMatcher);

        want.string(descriptionOf(matcher)).isEqualTo("with toString() " + descriptionOf(toStringMatcher));
    }

    @Test
    public void testMismatchContainsToStringValue() {
        String expectedMismatchString = "toString() was \"Cheese\"";
        assertMismatchDescription(expectedMismatchString, hasToString(TO_STRING_RESULT), "Cheese");
    }

    private String descriptionOf(Matcher<?> matcher) {
        return StringDescription.asString(matcher);
    }
}
