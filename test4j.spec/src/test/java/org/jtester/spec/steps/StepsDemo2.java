package org.jtester.spec.steps;

import org.jtester.spec.Steps;
import org.jtester.spec.annotations.Step;
import org.jtester.spec.steps.StepsSpec.MyName;

/**
 * @author darui.wudr 2013-8-22 下午2:40:54
 */
public class StepsDemo2 extends Steps.Default<MyName> {

    @Step
    public void printSomething() {
        System.out.println("print name:" + super.shared.getName());
    }
}
