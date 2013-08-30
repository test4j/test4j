package org.jtester.spec.util;

import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StoryType;
import org.test4j.spec.annotations.Given;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.annotations.Then;
import org.test4j.spec.annotations.When;
import org.test4j.spec.inner.IScenario;
import org.test4j.testng.JSpec;
import org.testng.annotations.Test;

@StoryFile(value = "SpecInvokeDemo.story", type = StoryType.TXT)
public class SpecInvokeDemo extends JSpec {

    @Test(dataProvider = "story")
    @Override
    public void runScenario(IScenario scenario) throws Throwable {
        this.run(scenario);
    }

    StringBuilder buff;

    @Given
    public void givenMethod(final @Named("欢迎词") String initValue// <br>
    ) throws Exception {
        buff = new StringBuilder();
        buff.append(initValue);
    }

    @Then
    public void thenMethod(final @Named("结果值") String result) throws Exception {
        want.string(buff.toString()).isEqualTo(result);
    }

    @When
    public void whenMethod(final @Named("客人1") String user1, // <br>
                           final @Named("客人2") String user2) throws Exception {
        buff.append(", ").append(user1).append(" and ").append(user2);
    }
}
