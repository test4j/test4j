package mockit.jtester;

import mockit.Mock;
import mockit.MockClass;

import org.jtester.junit.JTesterRunner;
import org.junit.internal.builders.JUnit4Builder;
import org.junit.runner.Runner;

@MockClass(realClass = JUnit4Builder.class)
public class JUnit4BuilderDecorator {
	@Mock
	public Runner runnerForClass(Class<?> testClass) throws Throwable {
		return new JTesterRunner(testClass);
	}
}
