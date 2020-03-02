package org.test4j.hamcrest.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.test4j.tools.commons.StringHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class LinkMatcher<T> extends BaseMatcher<T> {
    private final List<MessageMatcher> matchers = new ArrayList<MessageMatcher>();

    public void add(Matcher<?> matcher) {
        this.add(null, matcher);
    }

    public void add(String message, Matcher<?> matcher) {
        if (matcher instanceof MessageMatcher) {
            this.matchers.add((MessageMatcher) matcher);
        } else {
            this.matchers.add(new MessageMatcher(message, matcher));
        }
    }

    public boolean matches(Object obj) {
        for (Matcher<?> matcher : matchers) {
            if (!matcher.matches(obj)) {
                return false;
            }
        }
        return true;
    }

    public void describeTo(Description description) {
        description.appendList("(", " and ", ")", matchers);
    }

    private static class MessageMatcher extends BaseMatcher {
        private Matcher<?> matcher;

        private String message;

        private Object actual;

        public MessageMatcher(String message, Matcher<?> matcher) {
            this.matcher = matcher;
            this.message = message;
        }

        public boolean matches(Object item) {
            this.actual = item;
            return this.matcher.matches(item);
        }

        public void describeTo(Description description) {
            if (StringHelper.isBlankOrNull(message)) {
                this.matcher.describeTo(description);
            } else {
                description.appendText(message) /** <br> */
                        .appendText("\nExpected: ")/** <br> */
                        .appendDescriptionOf(matcher)/** <br> */
                        .appendText("\n     got: ")/** <br> */
                        .appendValue(this.actual)/** <br> */
                        .appendText("\n");
            }
        }
    }
}
