package org.test4j.hamcrest.matcher.string;

public class StringOccursMatcher extends StringMatcher {
    private int times;
    private int occurs = 0;

    public StringOccursMatcher(int times, String expected) {
        super(expected);
        this.times = times;
    }

    @Override
    protected boolean match(String expected, String actual) {
        if (expected == null) {
            return false;
        }
        int len = expected.length();
        int index = -len;
        while (true) {
            index = actual.indexOf(expected, index + len);
            if (index >= 0) {
                this.occurs++;
            } else {
                break;
            }
        }
        return this.occurs == times;
    }

    @Override
    protected String relationship() {
        return String.format("expected the sub string[%s] occurs times is %d, but is %d.",
                expected, times, occurs);
    }
}
