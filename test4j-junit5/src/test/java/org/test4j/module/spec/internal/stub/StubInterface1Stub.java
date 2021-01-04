package org.test4j.module.spec.internal.stub;

import org.test4j.mock.MockUp;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.test4j.example.spring.ServiceA;
import org.test4j.example.stub.StubInterface1;
import org.test4j.exception.Test4JException;

/**
 * @author generate code
 */
@Component
public class StubInterface1Stub extends MockUp<StubInterface1Stub> implements StubInterface1 {

	@Override
	public int aFunctionMethod(Function<List<Map<String, ?>>, String> arg1, List<? extends Set<ServiceA>> arg2) {
		throw new AssertionError("not mock");
	}

	@Override
	public void aVoidMethod(int arg1, Double arg2) throws Test4JException {
		throw new AssertionError("not mock");
	}
}