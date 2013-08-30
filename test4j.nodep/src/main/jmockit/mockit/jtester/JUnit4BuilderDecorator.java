package mockit.jtester;

import java.lang.reflect.Constructor;

import mockit.Mock;
import mockit.MockClass;

import org.junit.internal.builders.JUnit4Builder;
import org.junit.runner.Runner;

@MockClass(realClass = JUnit4Builder.class)
public class JUnit4BuilderDecorator {
    @SuppressWarnings("rawtypes")
    @Mock
    public Runner runnerForClass(Class<?> testClass) throws Throwable {
        Constructor constructor = Class.forName("org.jtester.junit.JTesterRunner").getDeclaredConstructor(Class.class);
        return (Runner) constructor.newInstance(testClass);
        //return new JTesterRunner(testClass);
    }
}
