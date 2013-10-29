package org.test4j.spec.steps;

import org.test4j.module.spring.annotations.SpringBeanByName;
import org.test4j.spec.Steps;
import org.test4j.spec.annotations.Given;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.Then;
import org.test4j.spec.annotations.When;
import org.test4j.spec.spring.UserService;
import org.test4j.spec.steps.StepsSpec.MyName;

/**
 * @author darui.wudr 2013-8-22 下午1:58:35
 */
public class StepsDemo1 extends Steps.Default<MyName> {

    private String name;
    /**
     * 验证框架自动给Steps的子类注入spring bean的功能
     */
    @SpringBeanByName
    UserService    userService;

    @Given
    public void method1(final @Named("测试数据") String name) {
        want.object(userService).notNull();
        this.name = name;
        super.shared.setName(name);
    }

    @When
    public void doing() throws Exception {
        want.object(userService).notNull();
        this.name = "hello, " + this.name;
    }

    @Then
    public void checkPara(final @Named("验证数据") String expected//<br>
    ) throws Exception {
        want.object(userService).notNull();
        want.string(name).isEqualTo(expected);
    }
}
