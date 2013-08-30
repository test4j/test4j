package org.jtester.module.jmockit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import mockit.Mocked;

import org.jtester.fortest.beans.ComplexObject;
import org.jtester.junit.JTester;
import org.jtester.module.inject.annotations.Inject;
import org.jtester.module.jmockit.ReturnValueTest.SomeInterface;
import org.jtester.module.jmockit.ReturnValueTest.SomeService;
import org.junit.Test;

@SuppressWarnings({ "rawtypes" })
public class ReturnValueTest_Assert implements JTester {
    public SomeService   someService = new SomeService();

    @Mocked
    @Inject(targets = "someService")
    public SomeInterface someInterface;

    /**
     * 断言与值混用
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testJMockit() throws IOException {
        new Expectations() {
            {
                someInterface.someCallException();

                someInterface.someCall("darui.wu", (List) any, (HashMap) any);
                result = ComplexObject.instance();

            }
        };
        someInterface.someCallException();

        String result = this.someService.call("darui.wu");
        want.string(result).contains("name=");
    }
}
