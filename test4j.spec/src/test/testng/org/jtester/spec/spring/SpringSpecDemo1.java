package org.jtester.spec.spring;

import java.util.List;

import org.jtester.hamcrest.matcher.property.reflection.EqMode;
import org.jtester.module.spring.annotations.SpringBeanByName;
import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StorySource;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.annotations.Then;
import org.jtester.spec.annotations.When;
import org.jtester.spec.inner.IScenario;
import org.testng.annotations.Test;

@StoryFile(type = StoryType.TXT, source = StorySource.ClassPath)
public class SpringSpecDemo1 extends SpringBaseDemo {

    @SpringBeanByName
    UserService userService;

    @Given
    public void readyData() {
        System.out.println("====");
    }

    @When
    public void queryUser(@Named("user") String name) {
        this.users = this.userService.getUserByName(name);
        System.out.println("xxxxxxx");
    }

    @Then
    public void checkData(@Named("userList") String users) {
        String[] arr = users.split(",");
        want.list(this.users).reflectionEq(arr, EqMode.IGNORE_ORDER);
        System.out.println("sssssss===");
        // throw new RuntimeException("xxxxxx");
    }

    List<String> users;

    @Test(dataProvider = "story")
    @Override
    public void runScenario(IScenario scenario) throws Throwable {
        this.run(scenario);
    }
}
