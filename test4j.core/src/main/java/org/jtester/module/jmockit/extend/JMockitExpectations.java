package org.jtester.module.jmockit.extend;

import mockit.Expectations;
import mockit.Mocked;
import mockit.internal.expectations.argumentMatching.ArgumentMatcher;
import mockit.internal.expectations.transformation.ActiveInvocations;

import org.jtester.hamcrest.TheStyleAssertion;
import org.jtester.hamcrest.matcher.JMockitAdapter;
import org.jtester.module.jmockit.utility.ExpectationsUtil;
import org.jtester.tools.reflector.MethodAccessor;

import ext.jtester.hamcrest.Matcher;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JMockitExpectations extends Expectations implements JTesterInvocations {
    @Mocked(methods = { "" })
    protected InvokeTimes             invokerTimes;

    @Mocked(methods = { "" })
    protected ExpectationsResult      expectationsResult;

    @Mocked(methods = { "" })
    protected final TheStyleAssertion the;

    public JMockitExpectations() {
        super();
        ExpectationsUtil.register(this);
        this.the = new TheStyleAssertion();
    }

    public JMockitExpectations(int numberOfIterations, Object... classesOrObjectsToBePartiallyMocked) {
        super(numberOfIterations, classesOrObjectsToBePartiallyMocked);
        ExpectationsUtil.register(this);
        this.the = new TheStyleAssertion();
    }

    public JMockitExpectations(Object... classesOrObjectsToBePartiallyMocked) {
        super(classesOrObjectsToBePartiallyMocked);
        ExpectationsUtil.register(this);
        this.the = new TheStyleAssertion();
    }

    public <T> InvokeTimes when(T o) {
        return new InvokeTimes(this);
    }

    public void thenReturn(Object value) {
        super.returns(value);
    }

    public void thenThrow(Throwable e) {
        ActiveInvocations.addResult(e);
    }

    public void thenReturn(Object firstValue, Object... remainingValues) {
        super.returns(firstValue, remainingValues);
    }

    public void thenDo(Delegate delegate) {
        super.returns(delegate);
    }

    public <T> T any(Class<T> claz) {
        T o = the.object().any().wanted(claz);
        return o;
    }

    public <T> T is(T value) {
        T o = (T) the.object().reflectionEq(value).wanted();
        return o;
    }

    final static MethodAccessor methodAccessor = new MethodAccessor(Expectations.class, "addMatcher",
                                                       ArgumentMatcher.class);

    protected final <T> T with(Matcher argumentMatcher) {
        JMockitAdapter adapter = JMockitAdapter.create(argumentMatcher);
        methodAccessor.invokeUnThrow(this, new Object[] { adapter });

        Object argValue = adapter.getInnerValue();
        return (T) argValue;
    }

    protected final <T> T with(T argValue, Matcher argumentMatcher) {
        JMockitAdapter adapter = JMockitAdapter.create(argumentMatcher);
        methodAccessor.invokeUnThrow(this, new Object[] { adapter });
        return argValue;
    }

    public static interface Delegate extends mockit.Delegate {
    }
}
