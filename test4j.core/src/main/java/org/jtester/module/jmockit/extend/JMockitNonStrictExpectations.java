package org.jtester.module.jmockit.extend;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.internal.expectations.argumentMatching.ArgumentMatcher;
import mockit.internal.expectations.transformation.ActiveInvocations;

import org.jtester.hamcrest.TheStyleAssertion;
import org.jtester.hamcrest.matcher.JMockitAdapter;
import org.jtester.module.jmockit.utility.ExpectationsUtil;
import org.jtester.tools.reflector.MethodAccessor;

import ext.jtester.hamcrest.Matcher;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JMockitNonStrictExpectations extends NonStrictExpectations implements JTesterInvocations {

    @Mocked(methods = { "" })
    protected InvokeTimes             invokerTimes;

    @Mocked(methods = { "" })
    protected ExpectationsResult      expectationsResult;

    @Mocked(methods = { "" })
    protected final TheStyleAssertion the;

    public JMockitNonStrictExpectations() {
        super();
        ExpectationsUtil.register(this);
        this.the = new TheStyleAssertion();
    }

    public JMockitNonStrictExpectations(int numberOfIterations, Object... classesOrObjectsToBePartiallyMocked) {
        super(numberOfIterations, classesOrObjectsToBePartiallyMocked);
        ExpectationsUtil.register(this);
        this.the = new TheStyleAssertion();
    }

    public JMockitNonStrictExpectations(Object... classesOrObjectsToBePartiallyMocked) {
        super(classesOrObjectsToBePartiallyMocked);
        ExpectationsUtil.register(this);
        this.the = new TheStyleAssertion();
    }

    public <T> ExpectationsResult when(T o) {
        ActiveInvocations.maxTimes(-1);
        return new ExpectationsResult(this);
    }

    @Override
    public void thenReturn(Object value) {
        super.returns(value);
    }

    @Override
    public void thenReturn(Object firstValue, Object... remainingValues) {
        super.returns(firstValue, remainingValues);
    }

    final static MethodAccessor methodAccessor = new MethodAccessor(NonStrictExpectations.class, "addMatcher",
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
}
