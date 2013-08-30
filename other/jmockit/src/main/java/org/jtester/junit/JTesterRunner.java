package org.jtester.junit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class JTesterRunner extends BlockJUnit4ClassRunner {
	public JTesterRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}
}
