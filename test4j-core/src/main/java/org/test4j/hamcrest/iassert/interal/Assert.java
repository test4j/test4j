package org.test4j.hamcrest.iassert.interal;

import lombok.Getter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.test4j.exception.Test4JException;
import org.test4j.hamcrest.TheStyleAssertion;
import org.test4j.hamcrest.matcher.LinkMatcher;
import org.test4j.hamcrest.matcher.clazz.ClassAssignFromMatcher;
import org.test4j.tools.commons.PrimitiveHelper;
import org.test4j.tools.reflector.MethodAccessor;


@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Assert<T, E extends IAssert> extends BaseMatcher<T> implements IAssert<T, E> {
    protected static final TheStyleAssertion the = new TheStyleAssertion();

    @Getter
    protected AssertObject assertObject;


    public Assert() {
    }

    public Assert(Class<? extends IAssert> expectedClass) {
        this.assertObject = new AssertObject(AssertType.MatcherStyle, null, expectedClass)
                .setLink(new LinkMatcher<>());
    }

    public Assert(Class<T> targetClass, Class<? extends IAssert> expectedClass) {
        this.assertObject = new AssertObject(AssertType.MatcherStyle, null, targetClass)
                .setExpectedClass(expectedClass)
                .setLink(new LinkMatcher<>());
    }

    public Assert(T value, Class<? extends IAssert> expectedClass) {
        this.assertObject = new AssertObject(AssertType.AssertStyle, value, expectedClass);
    }

    public Assert(T value, Class<T> targetClass, Class<? extends IAssert> expectedClass) {
        this.assertObject = new AssertObject(AssertType.AssertStyle, value, expectedClass)
                .setTargetClass(targetClass);
    }

    public T wanted() {
        if (this.assertObject.getType() == AssertType.AssertStyle) {
            throw new Test4JException("is not an Expectations");
        }
        return (T) PrimitiveHelper.getPrimitiveDefaultValue(this.assertObject.getTargetClass());
    }

    public <F> F wanted(Class<F> claz) {
        if (this.assertObject.getType() == AssertType.AssertStyle) {
            throw new Test4JException("is not an Expectations");
        }
        if (claz.isPrimitive() == false) {
            assertThat(new ClassAssignFromMatcher(claz));
        }
        return (F) PrimitiveHelper.getPrimitiveDefaultValue(claz);
    }

    public void describeTo(Description description) {
        if (this.assertObject.getLink() != null && this.assertObject.assertTypeIs(AssertType.MatcherStyle)) {
            this.assertObject.getLink().describeTo(description);
        } else if (this.assertObject.getValue() != null) {
            description.appendText(this.assertObject.getValue().toString());
        }
    }

    public E assertThat(Matcher matcher) {
        if (this.assertObject.getType() == AssertType.AssertStyle) {
            MatcherAssert.assertThat(this.assertObject.getValue(), matcher);
        } else {
            this.assertObject.getLink().add(matcher);
        }
        return (E) this;
    }

    public E assertThat(String message, Matcher matcher) {
        if (this.assertObject.getType() == AssertType.AssertStyle) {
            MatcherAssert.assertThat(message, this.assertObject.getValue(), matcher);
        } else {
            this.assertObject.getLink().add(message, matcher);
        }
        return (E) this;
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

    public boolean matches(Object item) {
        if (this.assertObject.getType() == AssertType.MatcherStyle && this.assertObject.getValue() instanceof String) {
            String value = MethodAccessor.invokeMethodUnThrow(item, (String) item);
            return this.assertObject.getLink().matches(value);
        } else {
            return this.assertObject.getLink().matches(item);
        }
    }

    @Override
    public boolean equals(Object obj) {
        throw new Test4JException("the method can't be used,please use isEqualTo() instead");
    }

    @Override
    public int hashCode() {
        throw new Test4JException("the method can't be used!");
    }
}
