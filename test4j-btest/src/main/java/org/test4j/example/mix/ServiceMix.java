package org.test4j.example.mix;

import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.example.spring.ServiceA;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Given;
import org.test4j.module.spec.annotations.Then;
import org.test4j.tools.Logger;

public class ServiceMix implements IMix {
    @Autowired
    private ServiceA serviceA;

    @Given("执行前置条件，输入参数{1}")
    public void do_given_method(String condition) {
        Logger.info(condition);
    }

    @Then("后置校验结果，输入参数{1},返回结果{0}")
    public String do_then_method(String sayHello) {
        String result = serviceA.say(sayHello);
        want.string(result).eq(sayHello);
        return "success";
    }
}