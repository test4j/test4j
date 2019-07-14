package org.test4j.junit;


import mockit.Mock;
import mockit.MockUp;
import org.junit.internal.builders.JUnit4Builder;
import org.junit.runner.Runner;
import org.test4j.module.core.utility.MessageHelper;

public class Test4JBuilder extends MockUp<JUnit4Builder> {
    public Test4JBuilder(){
        MessageHelper.info("\n\n========Test4JBuilder Constructor========\n\n");
    }
    /**
     * 覆盖junit原生执行器
     *
     * @param testClass
     * @return
     * @throws Throwable
     */
    @Mock
    public Runner runnerForClass(Class<?> testClass) throws Throwable {
        MessageHelper.info("\n\n========Test4JBuilder fake runnerForClass========\n\n");
        return new Test4JProxyRunner(testClass);
    }
}
