package org.test4j.spec.steps;

import org.test4j.junit.JSpec;
import org.test4j.spec.annotations.Given;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.Then;
import org.test4j.spec.annotations.When;

public class BeforeScenarioDemo extends JSpec {
    private Integer var = 0;

    @Given
    public void setVar(final @Named("变量值") Integer var//<br>
    ) throws Exception {
        this.var = var;
    }

    @When
    public void addVar(final @Named("增量") Integer inc//<br>
    ) throws Exception {
        this.var += inc;
    }

    @Then
    public void checkVar(final @Named("变量值") Integer expected//<br>
    )throws Exception {
    	want.number(var).isEqualTo(expected);
    }
}
