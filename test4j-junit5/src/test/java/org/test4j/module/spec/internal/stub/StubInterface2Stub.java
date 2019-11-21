package org.test4j.module.spec.internal.stub;

import mockit.Mock;
import mockit.MockUp;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.test4j.example.stub.StubInterface2;

/**
 * @author generate code
 */
@Component
public class StubInterface2Stub extends MockUp<StubInterface2Stub> implements StubInterface2 {

	@Override
	public <E> Set<? super BigDecimal> function3(Class<E> arg1) {
		throw new AssertionError("not mock");
	}

	@Override
	public <T extends Serializable & Comparable, F extends Date> F newDate(Class<? super T> arg1) {
		throw new AssertionError("not mock");
	}
}