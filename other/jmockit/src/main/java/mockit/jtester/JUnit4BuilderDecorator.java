package mockit.test4j;

import mockit.Mock;
import mockit.MockClass;

import org.test4j.junit.Test4JRunner;
import org.junit.internal.builders.JUnit4Builder;
import org.junit.runner.Runner;

@MockClass(realClass = JUnit4Builder.class)
public class JUnit4BuilderDecorator {
	@Mock
	public Runner runnerForClass(Class<?> testClass) throws Throwable {
		return new Test4JRunner(testClass);
	}
}
