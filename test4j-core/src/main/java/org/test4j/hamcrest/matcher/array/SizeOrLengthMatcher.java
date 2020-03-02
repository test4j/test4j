package org.test4j.hamcrest.matcher.array;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.test4j.tools.commons.ArrayHelper;

import java.util.Collection;

public class SizeOrLengthMatcher extends BaseMatcher<Collection<?>> {
    private int size;

    private SizeOrLengthMatcherType type;

    public SizeOrLengthMatcher(int size, SizeOrLengthMatcherType type) {
        this.size = size;
        this.type = type;
    }

    int actualSize = 0;

    public boolean matches(Object actual) {
        if (actual == null) {
            return false;
        }
        actualSize = ArrayHelper.sizeOf(actual);

        switch (type) {
            case EQ:
                return actualSize == size;
            case GT:
                return actualSize > size;
            case GE:
                return actualSize >= size;
            case LT:
                return actualSize < size;
            case LE:
                return actualSize <= size;
            default:
                return actualSize != size;
        }
    }

    public void describeTo(Description description) {
        description.appendText(String.format(type.description(), size));
        description.appendText(", but actual size is[" + actualSize + "].");
    }

    public static enum SizeOrLengthMatcherType {
        EQ {
            @Override
            public String description() {
                return "size of collection or array must equal to %d";
            }
        },
        GT {
            @Override
            public String description() {
                return "size of collection or array must be greater then %d";
            }
        },
        GE {
            @Override
            public String description() {
                return "size of collection or array must equal to or greater then %d";
            }
        },
        LT {
            @Override
            public String description() {
                return "size of collection or array must be less then %d";
            }
        },
        LE {
            @Override
            public String description() {
                return "size of collection or array must equal to or less then %d";
            }
        },
        NE {
            @Override
            public String description() {
                return "size of collection or array must not equal to %d";
            }
        };

        public abstract String description();
    }
}
