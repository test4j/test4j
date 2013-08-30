package org.jtester.spec.steps;

import org.jtester.spec.Steps;
import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.Then;
import org.jtester.spec.annotations.When;
import org.jtester.spec.steps.StepsSpec.MyName;

/**
 * @author darui.wudr 2013-8-22 下午1:58:35
 */
public class StepsDemo1 extends Steps.Default<MyName> {

    private String name;

    @Given
    public void method1(final @Named("测试数据") String name) {
        this.name = name;
        super.shared.setName(name);
    }

    @When
    public void doing() throws Exception {
        this.name = "hello, " + this.name;
    }

    @Then
    public void checkPara(final @Named("验证数据") String expected//<br>
    ) throws Exception {
        want.string(name).isEqualTo(expected);
    }
}
