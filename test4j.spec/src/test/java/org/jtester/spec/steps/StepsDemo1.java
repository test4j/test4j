package org.jtester.spec.steps;

import org.jtester.spec.steps.StepsSpec.MyName;
import org.test4j.spec.Steps;
import org.test4j.spec.annotations.Given;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.Then;
import org.test4j.spec.annotations.When;

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
