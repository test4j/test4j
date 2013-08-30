package org.jtester.spec.txt;

import org.jtester.junit.JSpec;
import org.test4j.spec.annotations.Given;
import org.test4j.spec.inner.IScenario;

/**
 * @author darui.wudr 2013-8-22 下午3:07:35
 */
public class MethodErrorSpec extends JSpec {
    @Override
    public void runScenario(IScenario scenario) throws Throwable {
        try {
            super.runScenario(scenario);
            want.fail();
        } catch (Exception e) {
            want.string(e.getClass().getName()).isEqualTo(RuntimeException.class.getName());
        }
    }

    @Given
    public void errorMethod() throws Exception {
        throw new RuntimeException();
    }
}
