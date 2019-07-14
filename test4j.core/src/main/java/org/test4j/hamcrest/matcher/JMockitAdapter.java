package org.test4j.hamcrest.matcher;

import mockit.internal.expectations.argumentMatching.ArgumentMatcher;
import mockit.internal.expectations.argumentMatching.ArgumentMismatch;
import mockit.internal.reflection.FieldReflection;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsSame;
import org.hamcrest.number.OrderingComparison;

import javax.annotation.Nonnull;


@SuppressWarnings({"rawtypes"})
public final class JMockitAdapter implements ArgumentMatcher {
    private final Matcher hamcrestMatcher;

    public static JMockitAdapter create(final Matcher matcher) {
        return new JMockitAdapter(matcher);
    }

    private JMockitAdapter(Matcher matcher) {
        hamcrestMatcher = matcher;
    }

    @Override
    public boolean same(@Nonnull ArgumentMatcher other) {
        return false;//TODO
    }

    @Override
    public boolean matches(Object item) {
        return hamcrestMatcher.matches(item);
    }

    @Override
    public void writeMismatchPhrase(ArgumentMismatch description) {
        Description strDescription = new StringDescription();
        hamcrestMatcher.describeTo(strDescription);
        description.append(strDescription.toString());
    }

    public Object getInnerValue() {
        Matcher innerMatcher = hamcrestMatcher;

        while (innerMatcher instanceof Is
                || innerMatcher instanceof IsNot) {
            innerMatcher = FieldReflection.getField(innerMatcher.getClass(), Matcher.class, innerMatcher);
        }

        if (innerMatcher instanceof IsEqual || innerMatcher instanceof IsSame
                || innerMatcher instanceof OrderingComparison) {
            return FieldReflection.getField(innerMatcher.getClass(), Object.class, innerMatcher);
        } else {
            return null;
        }
    }
}
