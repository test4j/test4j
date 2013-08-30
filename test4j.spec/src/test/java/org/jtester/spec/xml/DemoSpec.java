package org.jtester.spec.xml;

import org.jtester.junit.JSpec;
import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.Step;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.When;

/**
 * @author darui.wudr 2013-1-10 下午11:51:13
 */
@StoryFile(type = StoryType.XML)
public class DemoSpec extends JSpec {
    private int num1;
    private int num2;

    @Given
    public void initParameter(@Named("参数1") Integer num1, @Named("参数2") Integer num2) {
        want.number(num1).isEqualTo(1234);
        want.number(num2).isEqualTo(4567);
        this.num1 = num1;
        this.num2 = num2;
    }

    @When
    @Step
    public void doSomething() {

    }

    @Step
    public void verifyResult(@Named("总和") Integer total) {
        want.number(num1 + num2).isEqualTo(total);
    }
}
