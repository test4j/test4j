package org.jtester.spec.util;

import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.Then;
import org.jtester.spec.annotations.When;
import org.jtester.spec.inner.IScenario;
import org.jtester.testng.JSpec;
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
