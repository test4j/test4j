package org.jtester.module.jmockit.extend;

import mockit.Verifications;
import mockit.internal.expectations.argumentMatching.ArgumentMatcher;

import org.jtester.hamcrest.matcher.JMockitAdapter;
import org.jtester.module.jmockit.utility.ExpectationsUtil;
import org.jtester.tools.reflector.MethodAccessor;

import ext.jtester.hamcrest.Matcher;

@SuppressWarnings({ "rawtypes" })
public class JMocketVerifications extends Verifications {

	public JMocketVerifications() {
		super();
		ExpectationsUtil.register(this);
	}

	public JMocketVerifications(int numberOfIterations) {
		super(numberOfIterations);
		ExpectationsUtil.register(this);
	}

	final static MethodAccessor methodAccessor = new MethodAccessor(Verifications.class, "addMatcher",
			ArgumentMatcher.class);

	protected final <T> T with(Matcher argumentMatcher) {
		JMockitAdapter adapter = JMockitAdapter.create(argumentMatcher);
		methodAccessor.invokeUnThrow(this, new Object[] { adapter });
		return null;
	}

	protected final <T> T with(T argValue, Matcher argumentMatcher) {
		JMockitAdapter adapter = JMockitAdapter.create(argumentMatcher);
		methodAccessor.invokeUnThrow(this, new Object[] { adapter });
		return argValue;
	}
}
