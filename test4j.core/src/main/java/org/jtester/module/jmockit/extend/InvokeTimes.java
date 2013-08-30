package org.jtester.module.jmockit.extend;

import mockit.internal.expectations.transformation.ActiveInvocations;

public class InvokeTimes extends ExpectationsResult {
	InvokeTimes(JTesterInvocations expectations) {
		super(expectations);
	}

	public ExpectationsResult callExactly(int times) {
		ActiveInvocations.times(times);
		return this;
	}

	public ExpectationsResult callMinimal(int times) {
		ActiveInvocations.minTimes(times);
		return this;
	}

	public ExpectationsResult callMaximal(int times) {
		ActiveInvocations.maxTimes(times);
		return this;
	}

	public ExpectationsResult callBetween(int minTimes, int maxTimes) {
		ActiveInvocations.minTimes(minTimes);
		ActiveInvocations.maxTimes(maxTimes);
		return this;
	}

	/**
	 * the expected API will be allowing call any times
	 * 
	 * @return
	 */
	public ExpectationsResult callIgnoreTimes() {
		ActiveInvocations.minTimes(0);
		ActiveInvocations.maxTimes(-1);
		return this;
	}

	/**
	 * the expected API will be never call
	 */
	public void callNeverOccur() {
		ActiveInvocations.maxTimes(0);
	}
}
