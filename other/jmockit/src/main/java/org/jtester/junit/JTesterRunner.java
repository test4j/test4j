package org.test4j.junit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class Test4JRunner extends BlockJUnit4ClassRunner {
	public Test4JRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}
}
