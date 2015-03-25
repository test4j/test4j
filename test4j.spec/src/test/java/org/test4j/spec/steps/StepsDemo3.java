package org.test4j.spec.steps;

import org.test4j.spec.annotations.Step;

public class StepsDemo3 {
    @Step
    public void printSomething3() {
        System.out.println("print name, no shared data");
    }
}
