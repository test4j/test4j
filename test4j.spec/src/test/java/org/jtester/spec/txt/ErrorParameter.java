package org.jtester.spec.txt;

import java.util.Map;

import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.inner.IScenario;

/**
 * 参数错误
 * 
 * @author darui.wudr 2013-8-22 下午4:14:32
 */
public class ErrorParameter extends JSpec {
    @Override
    public void runScenario(IScenario scenario) throws Throwable {
        try {
            super.runScenario(scenario);
            want.fail();
        } catch (Exception e) {
            want.string(e.getMessage()).contains("covert to parameter @Named(\"参数\") error");
        }
    }

    @Given
    public void errorParameter(final @Named("参数") Map<String, String> para//<br>
    ) throws Exception {
    }
}
