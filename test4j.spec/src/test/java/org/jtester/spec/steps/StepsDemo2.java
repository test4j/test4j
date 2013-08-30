package org.jtester.spec.steps;

import org.jtester.spec.steps.StepsSpec.MyName;
import org.test4j.spec.Steps;
import org.test4j.spec.annotations.Step;

/**
 * @author darui.wudr 2013-8-22 下午2:40:54
 */
public class StepsDemo2 extends Steps.Default<MyName> {

    @Step
    public void printSomething() {
        System.out.println("print name:" + super.shared.getName());
    }
}
