package org.jtester.junit.demo.features;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

@SuppressWarnings("deprecation")
public class IgnoreLeadingFailure implements MethodRule {
	private final static String PROPERTY_FILE_NAME = System.getProperty("user.dir")
			+ "/src/test/resources/org/jtester/junit/demo/features/activatedTests.properties";

	private final static Properties activatedTests = new Properties();

	static {
		try {
			activatedTests.load(new FileInputStream(PROPERTY_FILE_NAME));
		} catch (IOException e) {
			// actually this is to be expected on the first run
			System.out.println("Couldn't load Properties from file" + e);
		}
	}

	public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
		final String methodName = getFullTestMethodName(method, target);
		if (activatedTests.containsKey(methodName)) {
			return base;
		} else {
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					try {
						base.evaluate();
						activateTest(methodName);
					} catch (Throwable t) {
						throw new AssumptionViolatedException(
								"This test never succeeded before, and failed again with: " + t.toString());
					}
				}

				private void activateTest(String fullTestMethodName) {
					activatedTests.put(fullTestMethodName, new SimpleDateFormat().format(new Date()));
					try {
						activatedTests.store(new FileOutputStream(PROPERTY_FILE_NAME),
								"tests that ran successfully at least once");
					} catch (IOException io) {
						System.out.println("failed to store properties" + io);
					}
				}
			};
		}
	}

	private String getFullTestMethodName(final FrameworkMethod method, Object target) {
		return target.getClass().getName() + "." + method.getName();
	}
}
